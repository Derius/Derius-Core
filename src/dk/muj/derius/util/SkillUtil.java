package dk.muj.derius.util;


public class SkillUtil
{
	
	/**
	 * Returns whether or not double drop should appear based on passed level.
	 * This is based on randomness, so result varies.
	 * @param {int} The level you want to check for.
	 * @param {int} levels required to get 1% chance, twice that amount means 2% etc
	 * @return {boolean} true if a double drop should occur. This is random
	 */
	public static boolean shouldDoubleDropOccur(int level, int levelsPerPercent)
	{
		double chance = (double) level/levelsPerPercent;
		double random = (int) ((Math.random()*100)+1);
		if (chance >= random)
		{
			return true;
		}
		return false;
	}
	
}
