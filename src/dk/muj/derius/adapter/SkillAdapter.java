package dk.muj.derius.adapter;

import java.lang.reflect.Type;
import java.util.List;

import org.bukkit.Material;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.skill.Skill;

public class SkillAdapter implements JsonDeserializer<Skill>, JsonSerializer<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SkillAdapter i = new SkillAdapter();
	public static SkillAdapter get() { return i; }
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	// Enabled
	public static final String ENABLED = "Enabled";
	
	// Descriptive fields
	public static final String NAME = "Name";
	public static final String DESC				= "Description";
	public static final String EARN_EXP_DESCS	= "Earn experience descriptions";
	public static final String ICON				= "Icon";
	
	// Specialisation settings
	public static final String SP_AUTO_ASSIGNED	= "Specialisation Automatically Assigned";
	public static final String SP_BLACKLISTED	= "Specialisation BlackListed";
	
	// Worlds where it can be earned.
	public static final String WORLDS_EARN		= "Worlds Earn Exp Enabled";
	
	// Custom configuration for each skill.
	public static final String CONFIGURATION	= "Configuration";
	
	// -------------------------------------------- //
	// SERIALIZE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(Skill src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src == null) return null;
		
		JsonObject ret = new JsonObject();
		JsonElement val;
		
		// Enabled
		val = DeriusPlugin.get().gson.toJsonTree(src.isEnabled());
		ret.add(ENABLED, val);
		
		// Name
		val = DeriusPlugin.get().gson.toJsonTree(src.getName());
		ret.add(NAME, val);
		
		// Description
		val = DeriusPlugin.get().gson.toJsonTree(src.getDesc());
		ret.add(DESC, val);
		
		// Icon
		val = DeriusPlugin.get().gson.toJsonTree(src.getIcon());
		ret.add(ICON, val);
		
		// Specialisation auto assigned
		val = DeriusPlugin.get().gson.toJsonTree(src.isSpAutoAssigned());
		ret.add(SP_AUTO_ASSIGNED, val);
		
		// Specialisation blacklisted
		val = DeriusPlugin.get().gson.toJsonTree(src.isSpBlackListed());
		ret.add(SP_BLACKLISTED, val);
		
		// Earn experience descriptions
		val = DeriusPlugin.get().gson.toJsonTree(src.getEarnExpDescs());
		ret.add(EARN_EXP_DESCS, val);
		
		// World earn
		val = DeriusPlugin.get().gson.toJsonTree(src.getWorldsEarn());
		ret.add(WORLDS_EARN, val);
		
		// Config
		val = DeriusPlugin.get().gson.toJsonTree(src.getConfiguration());
		ret.add(CONFIGURATION, val);
		
		return ret;
	}

	// -------------------------------------------- //
	// DESERIALIZE
	// -------------------------------------------- //
	
	@Override
	public Skill deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		
		// It must be an object!
		if ( ! json.isJsonObject())
		{
			DeriusAPI.debug(10000, "Non jsonObject %s", json);
			return null;
		}
		JsonObject jsonSkill = json.getAsJsonObject();
		
		Skill ret = new GsonSkill();
		JsonElement val;
		
		if (jsonSkill.has(ENABLED))
		{
			val = jsonSkill.get(ENABLED);
			boolean enabled = DeriusPlugin.get().gson.fromJson(val, Boolean.class);
			ret.setEnabled(enabled);
		}
		
		if (jsonSkill.has(NAME))
		{
			val = jsonSkill.get(NAME);
			String name = DeriusPlugin.get().gson.fromJson(val, String.class);
			ret.setName(name);
		}
		
		if (jsonSkill.has(DESC))
		{
			val = jsonSkill.get(DESC);
			String desc = DeriusPlugin.get().gson.fromJson(val, String.class);
			ret.setDesc(desc);
		}
		
		if (jsonSkill.has(EARN_EXP_DESCS))
		{
			val = jsonSkill.get(EARN_EXP_DESCS);
			List<String> descs = DeriusPlugin.get().gson.fromJson(val, new TypeToken<List<String>>(){}.getType());
			ret.setEarnExpDescs(descs);
		}
		
		if (jsonSkill.has(ICON))
		{
			val = jsonSkill.get(ICON);
			Material icon = DeriusPlugin.get().gson.fromJson(val, Material.class);
			ret.setIcon(icon);
		}
		
		if (jsonSkill.has(SP_AUTO_ASSIGNED))
		{
			val = jsonSkill.get(SP_AUTO_ASSIGNED);
			boolean assigned = DeriusPlugin.get().gson.fromJson(val, Boolean.class);
			ret.setSpAutoAssiged(assigned);
		}
		
		if (jsonSkill.has(SP_BLACKLISTED))
		{
			val = jsonSkill.get(SP_BLACKLISTED);
			boolean blacklisted = DeriusPlugin.get().gson.fromJson(val, Boolean.class);
			ret.setSpBlackListed(blacklisted);
		}
		
		if (jsonSkill.has(WORLDS_EARN))
		{
			val = jsonSkill.get(WORLDS_EARN);
			WorldExceptionSet worlds = DeriusPlugin.get().gson.fromJson(val, WorldExceptionSet.class);
			ret.setWorldsEarn(worlds);
		}
		
		if (jsonSkill.has(CONFIGURATION))
		{
			val = jsonSkill.get(CONFIGURATION);
			JsonObject conf = DeriusPlugin.get().gson.fromJson(val, JsonObject.class);
			ret.setConfiguration(conf);
		}
		
		return ret;
	}

}
