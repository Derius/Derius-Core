package dk.muj.derius.mixin;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.mixin.MaxLevelMixin;

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
		if (dplayer.isSpecialisedIn(skill)) return skill.getHardCap();
		else return skill.getSoftCap();
	}

}
