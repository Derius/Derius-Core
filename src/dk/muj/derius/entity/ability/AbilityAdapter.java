package dk.muj.derius.entity.ability;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import dk.muj.derius.DeriusCore;

/**
 * This adapter exists so we can have a MStore coll with abilities
 * but still keep ability abstract so everyone is forced to implement required methods.
 */
public class AbilityAdapter implements JsonDeserializer<DeriusAbility>, JsonSerializer<DeriusAbility>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AbilityAdapter i = new AbilityAdapter();
	public static AbilityAdapter get() { return i; }
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	// Enabled
	public static final String ENABLED			= "Enabled";
	
	// Descriptive fields
	public static final String NAME				= "Name";
	public static final String DESC				= "Description";
	
	// Cooldown
	public static final String TICKS_COOLDOWN	= "Cooldown-ticks";
	
	// Worlds use
	public static final String WORLDS_USE		= "Worlds-use-enabled";
	
	// -------------------------------------------- //
	// SERIALIZE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(DeriusAbility src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src == null) return null;
		
		JsonObject ret = new JsonObject();
		JsonElement val;
		
		// Enabled
		val = DeriusCore.get().gson.toJsonTree(src.isEnabled());
		ret.add(ENABLED, val);
		
		// Name
		val = DeriusCore.get().gson.toJsonTree(src.getName());
		ret.add(NAME, val);
		
		// Description
		val = DeriusCore.get().gson.toJsonTree(src.getDesc());
		ret.add(DESC, val);
		
		// Name
		val = DeriusCore.get().gson.toJsonTree(src.getCooldownTicks());
		ret.add(TICKS_COOLDOWN, val);
		
		// Description
		val = DeriusCore.get().gson.toJsonTree(src.getWorldsUse());
		ret.add(WORLDS_USE, val);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// DESERIALIZE
	// -------------------------------------------- //
	
	@Override
	public DeriusAbility deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		
		// It must be an object!
		if ( ! json.isJsonObject()) return null;
		JsonObject jsonAbility = json.getAsJsonObject();
		
		DeriusAbility ret = new GsonAbility();
		JsonElement val;
		
		if (jsonAbility.has(ENABLED))
		{
			val = jsonAbility.get(ENABLED);
			boolean enabled = DeriusCore.get().gson.fromJson(val, Boolean.class);
			ret.setEnabled(enabled);
		}
		
		if (jsonAbility.has(NAME))
		{
			val = jsonAbility.get(NAME);
			String name = DeriusCore.get().gson.fromJson(val, String.class);
			ret.setName(name);
		}
		
		if (jsonAbility.has(DESC))
		{
			val = jsonAbility.get(DESC);
			String desc = DeriusCore.get().gson.fromJson(val, String.class);
			ret.setDesc(desc);
		}
		
		if (jsonAbility.has(TICKS_COOLDOWN))
		{
			val = jsonAbility.get(TICKS_COOLDOWN);
			int ticks = DeriusCore.get().gson.fromJson(val, Integer.class);
			ret.setTicksCooldown(ticks);
		}
		
		if (jsonAbility.has(WORLDS_USE))
		{
			val = jsonAbility.get(WORLDS_USE);
			WorldExceptionSet worlds = DeriusCore.get().gson.fromJson(val, WorldExceptionSet.class);
			ret.setWorldsUse(worlds);
		}
		
		return ret;
	}
}
