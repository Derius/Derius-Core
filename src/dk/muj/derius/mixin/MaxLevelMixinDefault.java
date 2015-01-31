package dk.muj.derius.mixin;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

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
	public int getMaxLevel(MPlayer mplayer, Skill skill)
	{
		if (mplayer.isSpecialisedIn(skill)) return MConf.get().hardCap;
		else return MConf.get().softCap;
	}

}
