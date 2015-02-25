package dk.muj.derius.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import dk.muj.derius.api.mixin.StaminaMixin;
import dk.muj.derius.entity.MConf;

public class StaminaMixinDefault implements StaminaMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static StaminaMixinDefault i = new StaminaMixinDefault();
	public static StaminaMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public long regenTime(Object senderObject)
	{
		return MConf.get().staminaRegenSeconds*TimeUnit.MILLIS_PER_SECOND;
	}
	
	@Override
	public double sprintMultiplier(Player player)
	{
		return MConf.get().staminaSprintMultiplier;
	}
	
	@Override
	public double walkMultiplier(Player player)
	{
		return MConf.get().staminaWalkMultiplier;
	}
	
	@Override
	public double sneakMultiplier(Player player)
	{
		return MConf.get().staminaSneakMultiplier;
	}
	
	@Override
	public double standStillMultiplier(Player player)
	{
		return MConf.get().staminaStandStillMultiplier;
	}

	@Override
	public boolean standsStill(Player player)
	{
		return PlayerUtil.getStandStillMillis(player) > MConf.get().millisToStandStill;
	}
	
}
