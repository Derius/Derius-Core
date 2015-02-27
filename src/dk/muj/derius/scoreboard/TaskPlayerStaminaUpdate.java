package dk.muj.derius.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.lib.scheduler.RepeatingTask;

public class TaskPlayerStaminaUpdate extends RepeatingTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskPlayerStaminaUpdate i = new TaskPlayerStaminaUpdate();
	public static TaskPlayerStaminaUpdate get() { return i; }
	private TaskPlayerStaminaUpdate() { super(100); }
	
	// -------------------------------------------- //
	// OVERRIDE: ENGINE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusCore.get();
	}

	// -------------------------------------------- //
	// OVERRIDE: TASK
	// -------------------------------------------- //
	
	@Override
	public void invoke(long now)
	{
		long millis = this.getDelayMillis();
		long timesPerMinute = TimeUnit.MILLIS_PER_MINUTE / millis;
		
		for (Player player : MUtil.getOnlinePlayers())
		{
			DPlayer dplayer = DeriusAPI.getDPlayer(player);
			

			// EXAMPLE 300*600 / 60000 = 3
			double stamina = (dplayer.getStaminaMax()*timesPerMinute) / DeriusAPI.staminaRegenTime(player);
			
			stamina *= DeriusAPI.getStaminaMultiplier(player);
			
			dplayer.addStamina(stamina);
		}
		return;
	}
	


}
