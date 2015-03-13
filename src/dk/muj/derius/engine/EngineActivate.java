package dk.muj.derius.engine;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.ability.AbilityDurabilityMultiplier;
import dk.muj.derius.api.ability.AbilitySpecialItem;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.util.AbilityUtil;

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
			AbilityUtil.activateAbility(dplayer, ability, event, VerboseLevel.ALWAYS);
		}
		
	}
	
	// -------------------------------------------- //
	// SPECIAL ITEMS
	// -------------------------------------------- //
	
	@EventHandler
	public void activateSpecialItem(BlockBreakEvent event)
	{
		// Get fields
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		Optional<Material> optTool = dplayer.getPreparedTool();
		if ( ! optTool.isPresent()) return;
		Material tool = optTool.get();
		Material block = event.getBlock().getType();
		
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
			break; // We are done now.
		}
	}
	
}
