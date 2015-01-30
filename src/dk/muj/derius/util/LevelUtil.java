package dk.muj.derius.util;

import java.util.Map;
import java.util.Map.Entry;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

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
