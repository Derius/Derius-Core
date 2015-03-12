package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.ability.AbilityDurabilityMultiplier;
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
	// CAREFUL X
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void changeDurability(PlayerItemDamageEvent event)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			if (ability instanceof AbilityDurabilityMultiplier)
			AbilityUtil.activateAbility(dplayer, ability, event, VerboseLevel.ALWAYS);
		}
		
	}
	
}
