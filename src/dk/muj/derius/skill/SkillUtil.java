package dk.muj.derius.skill;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;

public class SkillUtil
{

	/**
	 * Returns whether or not the player should get a double drop.
	 * This is based on randomness, so result varies.
	 * @param {MPlayer} The player you want to check for
	 * @param {Skill} the skill
	 * @param {int} levels required to get 1% chance, twice that amount means 2%
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean PlayerGetDoubleDrop(MPlayer mplayer, Skill skill)
	{
		return SkillUtil.PlayerGetDoubleDrop(mplayer, skill, 10);
	}
	
	/**
	 * Returns whether or not the player should get a double drop.
	 * This is based on randomness, so result varies.
	 * 100 levels in said skill = 10% chance to get true;
	 * @param {MPlayer} The player you want to check for
	 * @param {Skill} the skill
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean PlayerGetDoubleDrop(MPlayer mplayer, Skill skill, int levelsPerPercent)
	{
		return SkillUtil.PlayerGetDoubleDrop(mplayer.getLvl(skill), skill);
	}
	
	/**
	 * Returns whether or not double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * 100 levels in said skill = 10% chance to get true;
	 * @param {int} The level you want to check for.
	 * @param {Skill} the skill
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean PlayerGetDoubleDrop(int level, Skill skill)
	{
		return SkillUtil.PlayerGetDoubleDrop(level,skill,10);
	}
	
	/**
	 * Returns whether or not double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * @param {int} The level you want to check for.
	 * @param {Skill} the skill
	 * @param {int} levels required to get 1% chance, twice that amount means 2%
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean PlayerGetDoubleDrop(int level, Skill skill, int levelsPerPercent)
	{
		double chance = (double) level/levelsPerPercent;
		double random = (int) ((Math.random()*100)+1);
		if(chance >= random)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a colorcode from the MConf for the Txt.parse method,
	 * based on the players ability to learn stated skill or not.
	 * @param {Skill} The Skill we want to check for.
	 * @param {MPlayer} The MPlayer we want to check for.
	 * @return {String} The colorcode for the txt.parse method.
	 */
	@Deprecated
	public static String CanPlayerLearnSkillColor (Skill skill, MPlayer mplayer)
	{
		if (skill.CanPlayerLearnSkill(mplayer))
		{
			return MConf.get().msgCanPlayerLearnSkillColorYes;
		}
		else
		{
			return MConf.get().msgCanPlayerLearnSkillColorNo;
		}
	}
}
