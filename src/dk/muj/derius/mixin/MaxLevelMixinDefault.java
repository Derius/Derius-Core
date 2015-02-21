package dk.muj.derius.mixin;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MConf;

public class MaxLevelMixinDefault implements MaxLevelMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MaxLevelMixinDefault i = new MaxLevelMixinDefault();
	public static MaxLevelMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getMaxLevel(DPlayer dplayer, Skill skill)
	{
		if (dplayer.isSpecialisedIn(skill)) return MConf.get().hardCap;
		else return MConf.get().softCap;
	}

}
