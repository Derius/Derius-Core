
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
		//Bukkit.broadcastMessage("level:" + String.valueOf(level));
		Entry<Integer, ? extends Number> ceil = null;
		Entry<Integer, ? extends Number> floor = null;
		
		for (Entry<Integer, ? extends Number> entry : settings.entrySet())
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
		
		//Bukkit.broadcastMessage("ceil:" + String.valueOf(ceil));
		//Bukkit.broadcastMessage("floor:" + String.valueOf(floor));
		
		if (floor == null) return OptionalDouble.empty();
		if (ceil == null) return OptionalDouble.of(floor.getValue().doubleValue());
		
		int lvlDiff = ceil.getKey() - ceil.getKey();
		double valueDiff = ceil.getValue().doubleValue() - floor.getValue().doubleValue();
		double diffPerLvl = valueDiff/lvlDiff;
		
		if (Double.isNaN(diffPerLvl)) diffPerLvl = 0;
		
		//Bukkit.broadcastMessage("lvlDiff:" + String.valueOf(lvlDiff));
		//Bukkit.broadcastMessage("valueDiff:" + String.valueOf(valueDiff));
		//Bukkit.broadcastMessage("diffPerLvl:" + String.valueOf(diffPerLvl));
		
		int toFloor = level - floor.getKey();
		double base = floor.getValue().doubleValue();
		double buff = toFloor * diffPerLvl;
		
		if (Double.isNaN(buff)) buff = 0;
		
		//Bukkit.broadcastMessage("toFloor:" + String.valueOf(toFloor));
		//Bukkit.broadcastMessage("base:" + String.valueOf(base));
		//Bukkit.broadcastMessage("buff:" + String.valueOf(buff));
		
		return OptionalDouble.of(base + buff);
	}

}
