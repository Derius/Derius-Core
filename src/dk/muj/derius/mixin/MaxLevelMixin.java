package dk.muj.derius.mixin;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;

public interface MaxLevelMixin
{
	public int getMaxLevel(DPlayer mplayer, Skill skill);
}
