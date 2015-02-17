package dk.muj.derius.util;

import org.apache.commons.lang.Validate;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;


public class SkillUtil
{
	// -------------------------------------------- //
	// DOUBLE DROP
	// -------------------------------------------- //
	
	/**
	 * Returns whether or not double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * @param {int} The level you want to check for.
	 * @param {int} levels required to get 1% chance, twice that amount means 2% etc
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean shouldDoubleDropOccur(int level, int levelsPerPercent)
	{
		Validate.isTrue(levelsPerPercent > 0, "levelsPerPercent must be positive");
		
		double chance = (double) level/levelsPerPercent;
		double random = (int) ((Math.random()*100)+1);
		if (chance >= random)
		{
			return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// SKILL REQUIREMENTS
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player can see said skill.
	 * This is based on the skill requirements
	 * @param {DPlayer} the player you want to check
	 * @param {Skill} the skill to check for
	 * @param {boolean} tell the player if they can't
	 * @return {boolean} true if the player can see said skill
	 */
	public static boolean canPlayerSeeSkill(DPlayer dplayer, Skill skill, boolean verbooseNot)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(skill, "skill mustn't be null");
		
 		for (Req req : skill.getSeeRequirements())
 		{
			if ( ! req.apply(dplayer.getSender(), skill))
			{
				if (verbooseNot) dplayer.sendMessage(req.createErrorMessage(dplayer.getSender(), skill));
				return false;
			}
 		}
 		return true;
 	}
	
	/**
	 * Tells whether or not the player can learn said skill.
	 * This is based on the skill requirements
	 * @param {DPlayer} the player you want to check
	 * @param {Skill} the skill to check for
	 * @param {boolean} tell the player if they can't
	 * @return {boolean} true if the player can learn said skill
	 */
	public static boolean canPlayerLearnSkill(DPlayer dplayer, Skill skill, boolean verbooseNot)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(skill, "skill mustn't be null");
		
 		for (Req req : skill.getLearnRequirements())
 		{
			if ( ! req.apply(dplayer.getSender(), skill))
			{
				if (verbooseNot) dplayer.sendMessage(req.createErrorMessage(dplayer.getSender(), skill));
				return false;
			}
 		}
 		return true;
 	}
	
	/**
	 * Tells whether or not the player can specialise in said skill.
	 * This is based on the skill requirements
	 * @param {DPlayer} the player you want to check
	 * @param {Skill} the skill to check for
	 * @param {boolean} tell the player if they can't
	 * @return {boolean} true if the player can specialise in said skill
	 */
	public static boolean canPlayerSpecialiseSkill(DPlayer dplayer, Skill skill, boolean verbooseNot)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(skill, "skill mustn't be null");
		
 		for (Req req : skill.getSpecialiseRequirements())
 		{
			if ( ! req.apply(dplayer.getSender(), skill))
			{
				if (verbooseNot) dplayer.sendMessage(req.createErrorMessage(dplayer.getSender(), skill));
				return false;
			}
 		}
 		return true;
 	}
	
}
