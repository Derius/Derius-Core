package dk.muj.derius.engine;

import java.util.Optional;
import java.util.OptionalDouble;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.DeriusCore;
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
	public EngineActivate() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusCore.get();
	}
	
	// -------------------------------------------- //
	// CAREFUL SOMETHING
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void changeDurability(PlayerItemDamageEvent event)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			if ( ! (ability instanceof AbilityDurabilityMultiplier)) continue;
			AbilityDurabilityMultiplier abilitydm = (AbilityDurabilityMultiplier) ability;
			
			if ( ! abilitydm.getToolTypes().contains(event.getItem().getType())) return ;
			
			int level = dplayer.getLvl(abilitydm.getSkill());
			OptionalDouble optMultiplier = LevelUtil.getLevelSettingFloat(abilitydm.getDurabilityMultiplier(), level);
			if ( ! optMultiplier.isPresent()) return;
			double multiplier = optMultiplier.getAsDouble();
			
			AbilityUtil.activateAbility(dplayer, ability, multiplier, VerboseLevel.ALWAYS);
		}
		
	}
	
	// -------------------------------------------- //
	// BLOCK BREAK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void handeBlockBreak(BlockBreakEvent event)
	{
		
	}
	
	public void activateSpecialItem(BlockBreakEvent event)
	{
		// Get fields
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		Optional<Material> optTool = dplayer.getPreparedTool();
		if ( ! optTool.isPresent()) return;
		Material tool = optTool.get();
		Material block = event.getBlock().getType();
		
		ItemStack inHand = event.getPlayer().getItemInHand();
		if (inHand == null || inHand.getType() == Material.AIR) return;
		
		// Check in all abilities
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			// If they are for special items...
			if ( ! (ability instanceof AbilitySpecialItem)) continue;
			AbilitySpecialItem abilitySi = (AbilitySpecialItem) ability;
			
			// ...and their activation tools contains the player preparedtool...
			if ( ! abilitySi.getToolTypes().contains(tool)) continue;
			// ...and their action blocks contains the borken block...
			if ( ! abilitySi.getBlockTypes().contains(block)) continue;
			// ...activate
			AbilityUtil.activateAbility(dplayer, ability, event.getBlock(), VerboseLevel.LOW);
			
			// Their tool is no longer prepared.
			dplayer.setPreparedTool(Optional.empty());
			
			break; // We are done now.
		}
	}
	
	public void handleDoubleDrop(BlockBreakEvent event)
	{
		// Get fields
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		Block block = event.getBlock();
		Material oreType = block.getType();
		
		if (DeriusAPI.isBlockPlacedByPlayer(block)) return;
		
		// Check in all abilities
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			// If they are for double drop...
			if ( ! (ability instanceof AbilityDoubleDrop)) continue;
			AbilityDoubleDrop abilityDd = (AbilityDoubleDrop) ability;
			
			if ( ! abilityDd.getBlockTypes().contains(oreType)) continue;
			if ( ! SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(abilityDd.getSkill()), abilityDd.getLevelsPerPercent())) continue;
			
			// ...and their action blocks contains the borken block...
			if ( ! abilityDd.getBlockTypes().contains(block)) continue;
			// ...activate
			AbilityUtil.activateAbility(dplayer, ability, event.getBlock(), VerboseLevel.HIGH);
			
			// Their tool is no longer prepared.
			dplayer.setPreparedTool(Optional.empty());
			
			break; // We are done now.
		}
	}
	
}
