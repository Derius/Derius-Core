package dk.muj.derius.mixin;

import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MPlayer;

public interface MaxLevelMixin
{
	public int getMaxLevel(MPlayer mplayer, Skill skill);
}
