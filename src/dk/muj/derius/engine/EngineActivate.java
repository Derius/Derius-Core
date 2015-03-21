package dk.muj.derius.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.BlockBreakExpGain;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.ability.AbilityDoubleDrop;
import dk.muj.derius.api.ability.AbilityDurabilityMultiplier;
import dk.muj.derius.api.ability.AbilitySpecialItem;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.api.util.LevelUtil;
import dk.muj.derius.api.util.SkillUtil;

public class EngineActivate extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineActivate i = new EngineActivate();
	public static EngineActivate get() { return i; }
	private EngineActivate() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusPlugin.get();
	}
	
	// -------------------------------------------- //
	// CAREFUL SOMETHING
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void changeDurability(final PlayerItemDamageEvent event)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		Material tool = event.getPlayer().getItemInHand().getType();
		// The whole things as a stream.
		DeriusAPI.getAllAbilities().stream()
		
		// It must be a AbilityDurabilityMultiplier
		.filter(ability -> (ability instanceof AbilityDurabilityMultiplier))
		
		// Cast it to AbilityDurabilityMultiplier
		.map(ability -> (AbilityDurabilityMultiplier) ability)
		
		// The tool type must match
		.filter(ability -> ability.getToolTypes().contains(tool))
		
		// Get the setting for that level. Keep the ability for later reference.
		.map(ability -> new Couple<>(ability, LevelUtil.getLevelSettingFloat(ability.getDurabilityMultiplier(), dplayer.getLvl(ability.getSkill()))))
		
		// The setting must be present (AKA there must be a change for that level.)
		.filter(couple -> couple.getSecond().isPresent())
		
		// Activation must succeed (AKA not return CANCEL)
		.filter(couple -> AbilityUtil.CANCEL != AbilityUtil.activateAbility(dplayer, couple.getFirst(), couple.getSecond().getAsDouble(), VerboseLevel.ALWAYS))
		
		// Calculate the difference. EXAMPLE 1 / 3 = 0.333
		.mapToInt(couple -> (int) MUtil.probabilityRound(event.getDamage() / couple.getSecond().getAsDouble()))
		
		// Set damage in the event
		.forEach(damage -> event.setDamage(damage));
	}
	
	// -------------------------------------------- //
	// BLOCK BREAK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void handeBlockBreak(BlockBreakEvent event)
	{
		if (DeriusAPI.isBlockPlacedByPlayer(event.getBlock())) return;
		//System.out.println(DeriusAPI.isBlockPlacedByPlayer(event.getBlock()));
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		this.activateSpecialItem(event, dplayer);
		this.handleDoubleDrop(event, dplayer);
		this.handleExpgain(event, dplayer);
	}
	
	private static List<BlockBreakExpGain> expGainers = new ArrayList<>();
	public static void registerExpGain(BlockBreakExpGain gainer) { expGainers.add(gainer); }
	
	private void handleExpgain(BlockBreakEvent event, DPlayer dplayer)
	{
		Material block = event.getBlock().getType();
		Player player = event.getPlayer();
		for (BlockBreakExpGain gainer : expGainers)
		{
			if ( ! gainer.getBlockTypes().containsKey(block)) continue;
			if ( ! gainer.getToolTypes().contains(player.getItemInHand().getType())) continue;
			if ( ! SkillUtil.canPlayerLearnSkill(dplayer, gainer.getSkill(), VerboseLevel.HIGHEST)) continue;
			
			dplayer.addExp(gainer.getSkill(), gainer.getBlockTypes().get(block).doubleValue());
		}
	}
	
	private void activateSpecialItem(BlockBreakEvent event, DPlayer dplayer)
	{
		// Get fields
		Optional<Material> optTool = dplayer.getPreparedTool();
		if ( ! optTool.isPresent()) return;
		Material tool = optTool.get();
		Material block = event.getBlock().getType();
		
		ItemStack inHand = event.getPlayer().getItemInHand();
		if (inHand == null || inHand.getType() == Material.AIR) return;
		
		// Check in all abilities
		for (AbilitySpecialItem ability : getSpecialItemAbilities())
		{
			// ...and their activation tools contains the player preparedtool...
			if ( ! ability.getToolTypes().contains(tool)) continue;
			// ...and their action blocks contains the borken block...
			if ( ! ability.getBlockTypes().contains(block)) continue;
			// ...activate
			AbilityUtil.activateAbility(dplayer, ability, event.getBlock(), VerboseLevel.LOW);
			
			// Their tool is no longer prepared.
			dplayer.setPreparedTool(Optional.empty());
			break; // We are done now.
		}
	}
	
	private Collection<AbilitySpecialItem> getSpecialItemAbilities()
	{
		List<AbilitySpecialItem> ret = new ArrayList<>();
		// Check in all abilities
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			// If they are for special items...
			if ( ! (ability instanceof AbilitySpecialItem)) continue;
			ret.add((AbilitySpecialItem) ability);
		}
		
		return ret;
	}
	
	private void handleDoubleDrop(BlockBreakEvent event, DPlayer dplayer)
	{
		// Get fields
		Block block = event.getBlock();
		Material oreType = block.getType();
		//ItemStack item = event.getPlayer().getItemInHand();
		
		// Check in all abilities
		for (AbilityDoubleDrop ability : getDoubleDropAbilities())
		{
			if ( ! SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(ability.getSkill()), ability.getLevelsPerPercent())) continue;
			
			//TODO: Enable this, when we are sure all the skills implemnt it.
			//if ( ! ability.getToolTypes().contains(item.getType())) continue;
			
			// ...and their action blocks contains the borken block...
			if ( ! ability.getBlockTypes().contains(oreType)) continue;
			// ...activate
			AbilityUtil.activateAbility(dplayer, ability, event.getBlock(), VerboseLevel.HIGH);
			break; // We are done now.
		}
	}
	
	
	private Collection<AbilityDoubleDrop> getDoubleDropAbilities()
	{
		List<AbilityDoubleDrop> ret = new ArrayList<>();
		// Check in all abilities
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			// If they are for special items...
			if ( ! (ability instanceof AbilityDoubleDrop)) continue;
			ret.add((AbilityDoubleDrop) ability);
		}
		
		return ret;
	}
	
}
