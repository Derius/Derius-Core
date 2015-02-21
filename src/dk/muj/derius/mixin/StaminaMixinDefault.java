package dk.muj.derius.mixin;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.entity.MConf;

public class StaminaMixinDefault implements StaminaMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static StaminaMixinDefault i = new StaminaMixinDefault();
	public static StaminaMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: StaminaMIxin
	// -------------------------------------------- //

	@Override
	public double getMaxUniversal(DPlayer dplayer)
	{
		return this.getMax(dplayer);
	}
	
	@Override
	public double getMax(DPlayer dplayer)
	{
		return MConf.get().staminaMax + dplayer.getBonusStamina();
	}

	@Override
	public double getPerMinute(DPlayer dplayer)
	{
		return MConf.get().staminaPerMinute;
	}



}
