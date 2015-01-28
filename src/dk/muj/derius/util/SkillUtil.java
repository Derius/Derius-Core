package dk.muj.derius.util;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

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
	public static boolean shouldPlayerGetDoubleDrop(MPlayer mplayer, Skill skill)
	{
		return SkillUtil.shouldPlayerGetDoubleDrop(mplayer, skill, 10);
	}
	
	/**
	 * Returns whether or not the player should get a double drop.
	 * This is based on randomness, so result varies.
	 * 100 levels in said skill = 10% chance to get true;
	 * @param {MPlayer} The player you want to check for
	 * @param {Skill} the skill
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean shouldPlayerGetDoubleDrop(MPlayer mplayer, Skill skill, int levelsPerPercent)
	{
		return SkillUtil.shouldPlayerGetDoubleDrop(mplayer.getLvl(skill), skill, levelsPerPercent);
	}
	
	/**
	 * Returns whether or not double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * 100 levels in said skill = 10% chance to get true;
	 * @param {int} The level you want to check for.
	 * @param {Skill} the skill
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean shouldPlayerGetDoubleDrop(int level, Skill skill)
	{
		return SkillUtil.shouldPlayerGetDoubleDrop(level,skill,10);
	}
	
	/**
	 * Returns whether or not double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * @param {int} The level you want to check for.
	 * @param {Skill} the skill
	 * @param {int} levels required to get 1% chance, twice that amount means 2%
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean shouldPlayerGetDoubleDrop(int level, Skill skill, int levelsPerPercent)
	{
		double chance = (double) level/levelsPerPercent;
		double random = (int) ((Math.random()*100)+1);
		if(chance >= random)
		{
			return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// MPLAYER RELATED
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player has this skill initiated.
	 * @param {Skill} the skill
	 * @param {MPlayer} the player we want to check for
	 * @return true if the player has something in this skill (even 0)
	 */
	public static boolean hasSkill(Skill skill, MPlayer mplayer) { return mplayer.getRawExpData().containsKey(skill.getId()); }
	
	/**
	 * Instantiates this skill for the player
	 * if not already instantiated
	 * @param {Skill} the skill
	 * @param {MPlayer} the player we want to check for
	 */
	public static void InstantiateSkill(Skill skill, MPlayer mplayer)
	{
		if ( ! SkillUtil.hasSkill(skill, mplayer))
		{
			mplayer.getRawExpData().put(skill.getId(), new Long(0));
		}
	}
	
	/**
	 * The maximum level a player can reach in said skill
	 * @param {Skill} skill to check for
	 * @return {int} the level the player can reach
	 */
	public static int getMaxLevel(Skill skill, MPlayer mplayer)
	{
		if (mplayer.isSpecialisedIn(skill))
		{
			return MConf.get().hardCap;
		}
		return MConf.get().softCap;
	}
	
}
