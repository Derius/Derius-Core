
package dk.muj.derius.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;

import org.apache.commons.lang.Validate;

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
		Validate.notNull(settings, "settings mustn't be null");
		
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
	
	public static OptionalDouble getLevelSettingFloat(Map<Integer, ? extends Number> settings, int level)
	{
		Validate.notNull(settings, "settings mustn't be null");
		Entry<Integer, ? extends Number> ceil = null;
		Entry<Integer, ? extends Number> floor = null;
		
		for (Entry<Integer, ? extends Number> entry : settings.entrySet())
		{	
			// It can be ceil
			if (entry.getKey() > level)
			{
				if (ceil == null || entry.getKey() <= ceil.getKey()) ceil = entry;
			}
			
			// It can be floor
			else if (entry.getKey() < level)
			{
				if (floor == null || entry.getKey() >= floor.getKey()) floor = entry;
			}
		}
		
		if (floor == null) return OptionalDouble.empty();
		if (ceil == null) return OptionalDouble.of(floor.getValue().doubleValue());
		
		int lvlDiff = ceil.getKey() - floor.getKey();
		double valueDiff = ceil.getValue().doubleValue() - floor.getValue().doubleValue();
		double diffPerLvl = valueDiff/lvlDiff;
		
		if (Double.isNaN(diffPerLvl)) diffPerLvl = 0;
		
		int toFloor = level - floor.getKey();
		double base = floor.getValue().doubleValue();
		double buff = toFloor * diffPerLvl;
		
		if (Double.isNaN(buff)) buff = 0;
		
		return OptionalDouble.of(base + buff);
	}

}
