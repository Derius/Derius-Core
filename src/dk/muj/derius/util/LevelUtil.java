package dk.muj.derius.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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
	
	public static <S> Optional<S> getLevelSetting(Map<Integer, S> settings, int level)
	{
		Entry<Integer, S> most = null;
		
		for (Entry<Integer, S> entry : settings.entrySet())
		{
			if ( ! (entry.getKey() <= level)) continue;
			if ( most != null && entry.getKey() < most.getKey()) continue;
			most = entry;
		}
		
		if (most == null) return Optional.empty();
		else return Optional.of(most.getValue());
	}
	
	public static Optional<Double> getLevelSettingFloat(Map<Integer, Number> settings, int level)
	{
		Entry<Integer, Number> ceil = null;
		Entry<Integer, Number> floor = null;
		
		for (Entry<Integer, Number> entry : settings.entrySet())
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
		
		if (floor == null) return Optional.of(0D);
		if (ceil == null) return Optional.of(floor.getValue().doubleValue());
		
		int lvlDiff = ceil.getKey() - ceil.getKey();
		double valueDiff = ceil.getValue().doubleValue() - floor.getValue().doubleValue();
		double diffPerLvl = valueDiff/lvlDiff;
		
		int toFloor = level - floor.getKey();
		double base = floor.getValue().doubleValue();
		double buff = toFloor * diffPerLvl;
		
		return Optional.of(base + buff);
	}

}
