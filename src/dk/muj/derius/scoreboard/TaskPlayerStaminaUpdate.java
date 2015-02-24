package dk.muj.derius.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;

public class TaskPlayerStaminaUpdate extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskPlayerStaminaUpdate i = new TaskPlayerStaminaUpdate();
	public static TaskPlayerStaminaUpdate get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: MODULO REPEAT TASK
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusCore.get();
	}

	@Override
	public void invoke(long now)
	{
		long millis = this.getDelayMillis();
		
		for (Player player : MUtil.getOnlinePlayers())
		{
			DPlayer dplayer = DeriusAPI.getDPlayer(player);
			double stamina = DeriusCore.getStaminaMixin().getPerMinute(dplayer) * millis / TimeUnit.MILLIS_PER_MINUTE;
			
			dplayer.addStamina(stamina);
		}
	}

}
