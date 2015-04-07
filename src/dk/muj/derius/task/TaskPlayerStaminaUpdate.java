package dk.muj.derius.task;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.player.DPlayer;

public final class TaskPlayerStaminaUpdate extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskPlayerStaminaUpdate i = new TaskPlayerStaminaUpdate();
	public static TaskPlayerStaminaUpdate get() { return i; }
	private TaskPlayerStaminaUpdate() { super(200); }
	
	// -------------------------------------------- //
	// OVERRIDE: ENGINE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusPlugin.get();
	}

	// -------------------------------------------- //
	// OVERRIDE: TASK
	// -------------------------------------------- //
	
	@Override
	public void invoke(long now)
	{
		long millis = now - this.getPreviousMillis();
		double timesPerMinute = (double) TimeUnit.MILLIS_PER_MINUTE / millis;
		
		for (Player player : MUtil.getOnlinePlayers())
		{
			DPlayer dplayer = DeriusAPI.getDPlayer(player);
			
			// EXAMPLE 300*600 / 60000 = 3
			double stamina = (dplayer.getStaminaMax() * timesPerMinute) / DeriusAPI.staminaRegenTime(player);
			
			stamina *= DeriusAPI.getStaminaMultiplier(player);
			stamina *= dplayer.getActivatedAbility().map(Ability::getStaminaMultiplier).orElse(1D);
			
			dplayer.addStamina(stamina);
		}
		return;
	}
	
}
