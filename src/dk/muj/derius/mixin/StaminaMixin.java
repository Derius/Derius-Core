package dk.muj.derius.mixin;

import dk.muj.derius.api.DPlayer;

public interface StaminaMixin
{
	public double getMaxUniversal(DPlayer dplayer);
	public double getMax(DPlayer dpalyer);
	public double getPerMinute(DPlayer dplayer);
}
