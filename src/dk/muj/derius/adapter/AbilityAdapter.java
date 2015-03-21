package dk.muj.derius.adapter;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.ability.Ability.AbilityType;

/**
 * This adapter exists so we can have a MStore coll with abilities
 * but still keep ability abstract so everyone is forced to implement required methods.
 */
public class AbilityAdapter implements JsonDeserializer<Ability>, JsonSerializer<Ability>
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
	public static final String ENABLED = "Enabled";
	
	// Descriptive fields
	public static final String NAME = "Name";
	public static final String DESC = "Description";
	
	// Cooldown
	public static final String MILLIS_COOLDOWN = "Cooldown Millis";
	
	// Stamina
	public static final String STAMINA_USAGE = "Stamina Usage";
	public static final String STAMINA_MULTIPLIER = "Stamina Multiplier";
	// Worlds use
	public static final String WORLDS_USE = "Worlds Use Enabled";
	
	// -------------------------------------------- //
	// SERIALIZE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(Ability osrc, Type typeOfSrc, JsonSerializationContext context)
	{
		if (osrc == null) return null;
		
		Ability src = osrc;
		
		JsonObject ret = new JsonObject();
		JsonElement val;
		
		// Enabled
		val = DeriusPlugin.get().gson.toJsonTree(src.getEnabled());
		ret.add(ENABLED, val);
		
		// Name
		val = DeriusPlugin.get().gson.toJsonTree(src.getName());
		ret.add(NAME, val);
		
		// Description
		val = DeriusPlugin.get().gson.toJsonTree(src.getDesc());
		ret.add(DESC, val);
		
		// Ticks Cooldown
		val = DeriusPlugin.get().gson.toJsonTree(src.getCooldownMillis());
		ret.add(MILLIS_COOLDOWN, val);
		
		// stamina usage
		val = DeriusPlugin.get().gson.toJsonTree(src.getStaminaUsage());
		ret.add(STAMINA_USAGE, val);
		
		// Since this only makes sense for active abilities. We won't confuse our users.
		if (src.getType() == AbilityType.ACTIVE)
		{
			val = DeriusPlugin.get().gson.toJsonTree(src.getStaminaMultiplier());
			ret.add(STAMINA_MULTIPLIER, val);
		}
		
		// Description
		val = DeriusPlugin.get().gson.toJsonTree(src.getWorldsUse());
		ret.add(WORLDS_USE, val);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// DESERIALIZE
	// -------------------------------------------- //
	
	@Override
	public Ability deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		
		// It must be an object!
		if ( ! json.isJsonObject())
		{
			DeriusAPI.debug(10000, "Non jsonObject %s", json);
			return null;
		}
		JsonObject jsonAbility = json.getAsJsonObject();
		
		Ability ret = new GsonAbility();
		JsonElement val;
		
		if (jsonAbility.has(ENABLED))
		{
			val = jsonAbility.get(ENABLED);
			boolean enabled = DeriusPlugin.get().gson.fromJson(val, Boolean.class);
			ret.setEnabled(enabled);
		}
		
		if (jsonAbility.has(NAME))
		{
			val = jsonAbility.get(NAME);
			String name = DeriusPlugin.get().gson.fromJson(val, String.class);
			ret.setName(name);
		}
		
		if (jsonAbility.has(DESC))
		{
			val = jsonAbility.get(DESC);
			String desc = DeriusPlugin.get().gson.fromJson(val, String.class);
			ret.setDesc(desc);
		}
		
		if (jsonAbility.has(MILLIS_COOLDOWN))
		{
			val = jsonAbility.get(MILLIS_COOLDOWN);
			int millis = DeriusPlugin.get().gson.fromJson(val, Integer.class);
			ret.setCooldownMillis(millis);
		}
		
		if (jsonAbility.has(STAMINA_USAGE))
		{
			val = jsonAbility.get(STAMINA_USAGE);
			double stamina = DeriusPlugin.get().gson.fromJson(val, Double.class);
			ret.setStaminaUsage(stamina);
		}
		
		if (jsonAbility.has(STAMINA_MULTIPLIER))
		{
			val = jsonAbility.get(STAMINA_MULTIPLIER);
			double multiplier = DeriusPlugin.get().gson.fromJson(val, Double.class);
			ret.setStaminaMultiplier(multiplier);
		}
		
		if (jsonAbility.has(WORLDS_USE))
		{
			val = jsonAbility.get(WORLDS_USE);
			WorldExceptionSet worlds = DeriusPlugin.get().gson.fromJson(val, WorldExceptionSet.class);
			ret.setWorldsUse(worlds);
		}
		
		return ret;
	}

}
