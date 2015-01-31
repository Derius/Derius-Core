package dk.muj.derius.mixin;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

public interface MaxLevelMixin
{
	public int getMaxLevel(MPlayer mplayer, Skill skill);
}
