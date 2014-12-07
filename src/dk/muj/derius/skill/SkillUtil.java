package dk.muj.derius.skill;

import dk.muj.derius.entity.MPlayer;

public class SkillUtil
{

	/**
	 * Returns whether or not the player should get a double drop.
	 * This is based on randomness, so result varies.
	 * 100 levels in said skill = 10% chance to get true;
	 * @param {MPlayer} The player you want to check for
	 * @param {String} id of the skill
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean PlayerGetDoubleDrop(MPlayer mplayer, Skill skill)
	{
		return SkillUtil.PlayerGetDoubleDrop(mplayer.getLvl(skill), skill);
	}
	
	/**
	 * Returns whether or nota double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * 100 levels in said skill = 10% chance to get true;
	 * @param {int} The level you want to check for.
	 * @param {String} id of the skill
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean PlayerGetDoubleDrop(int level, Skill skill)
	{
		double chance = level/10.0;
		double random = (int) ((Math.random()*100)+1);
		if(chance >= random)
		{
			return true;
		}
		return false;
	}
}
