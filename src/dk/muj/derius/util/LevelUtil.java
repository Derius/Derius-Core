package dk.muj.derius.util;

import java.util.Map;
import java.util.Map.Entry;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

public final class LevelUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	
	private LevelUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// LEVEL SETTINGS
	// -------------------------------------------- //
	
	public static <S> S getLevelSetting(Map<Integer, S> settings, int level)
	{
		Entry<Integer, S> most = null;
		
		for (Entry<Integer, S> entry : settings.entrySet())
		{
			if ( ! (entry.getKey() <= level)) continue;
			if ( most != null && entry.getKey() < most.getKey()) continue;
			most = entry;
		}
		
		if (most == null) return null;
		return most.getValue();
	}
	
	public static Double getLevelSettingFloat(Map<Integer, Double> settings, int level)
	{
		Entry<Integer, Double> ceil = null;
		Entry<Integer, Double> floor = null;
		
		for (Entry<Integer, Double> entry : settings.entrySet())
		{	
			// First iteration
			if (ceil == null && floor == null)
			{
				ceil = entry;
				floor = entry;
			}
			
			// Must be higher than level, but smaller than ceil
			if (entry.getKey() >= level && entry.getKey() <= ceil.getKey()) ceil = entry;
			
			// Must be lower than level but higher than floor
			else if (entry.getKey() <= level && entry.getKey() >= floor.getKey()) floor = entry;
		}
		
		int lvlDiff = ceil.getKey() - ceil.getKey();
		double valueDiff = ceil.getValue() - floor.getValue();
		double diffPerLvl = valueDiff/lvlDiff;
		
		int toFloor = level - floor.getKey();
		double base = floor.getValue();
		double buff = toFloor * diffPerLvl;
		
		return base + buff;
	}
	
	public static Double getLevelSettingInt(Map<Integer, Integer> settings, int level)
	{
		Entry<Integer, Integer> ceil = null;
		Entry<Integer, Integer> floor = null;
		
		for (Entry<Integer, Integer> entry : settings.entrySet())
		{	
			// First iteration
			if (ceil == null && floor == null)
			{
				ceil = entry;
				floor = entry;
			}
			
			// Must be higher than level, but smaller than ceil
			if (entry.getKey() >= level && entry.getKey() <= ceil.getKey()) ceil = entry;
			
			// Must be lower than level but higher than floor
			else if (entry.getKey() <= level && entry.getKey() >= floor.getKey()) floor = entry;
		}
		
		if (ceil == null) return (double) floor.getValue();
		if (floor == null) return null;
		
		int lvlDiff = ceil.getKey() - ceil.getKey();
		int valueDiff = ceil.getValue() - floor.getValue();
		double diffPerLvl = valueDiff/lvlDiff;
		
		int toFloor = level - floor.getKey();
		double base = floor.getValue();
		double buff = toFloor * diffPerLvl;
		
		return base + buff;
	}
	
	// -------------------------------------------- //
	// MPLAYER RELATED
	// -------------------------------------------- //
	
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
