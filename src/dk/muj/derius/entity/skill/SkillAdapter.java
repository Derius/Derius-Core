package dk.muj.derius.entity.skill;

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

import dk.muj.derius.DeriusCore;

public class SkillAdapter implements JsonDeserializer<DeriusSkill>, JsonSerializer<DeriusSkill>
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
	public static final String ENABLED			= "Enabled";
	
	// Descriptive fields
	public static final String NAME				= "Name";
	public static final String DESC				= "Description";
	public static final String EARN_EXP_DESCS	= "Earn-experience-descriptions";
	public static final String ICON				= "icon";
	
	// Specialisation settings
	public static final String SP_AUTO_ASSIGNED	= "Specialisation-auto-assigned";
	public static final String SP_BLACKLISTED	= "Specialisation-blacklisted";
	
	// Worlds where it can be earned.
	public static final String WORLDS_EARN		= "Worlds-earn-enabled";
	
	// Custom configuration for each skill.
	public static final String CONFIGURATION	= "Configuration";
	
	// -------------------------------------------- //
	// SERIALIZE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(DeriusSkill src, Type typeOfSrc, JsonSerializationContext context)
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
		
		// Icon
		val = DeriusCore.get().gson.toJsonTree(src.getIcon());
		ret.add(ICON, val);
		
		// Specialisation auto assigned
		val = DeriusCore.get().gson.toJsonTree(src.isSpAutoAssigned());
		ret.add(SP_AUTO_ASSIGNED, val);
		
		// Specialisation blacklisted
		val = DeriusCore.get().gson.toJsonTree(src.isSpBlackListed());
		ret.add(SP_BLACKLISTED, val);
		
		// Earn experience descriptions
		val = DeriusCore.get().gson.toJsonTree(src.getEarnExpDescs());
		ret.add(EARN_EXP_DESCS, val);
		
		// World earn
		val = DeriusCore.get().gson.toJsonTree(src.getWorldsEarn());
		ret.add(WORLDS_EARN, val);
		
		// Config
		val = DeriusCore.get().gson.toJsonTree(src.getConfiguration());
		ret.add(CONFIGURATION, val);
		
		return ret;
	}

	// -------------------------------------------- //
	// DESERIALIZE
	// -------------------------------------------- //
	
	@Override
	public DeriusSkill deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		
		// It must be an object!
		if ( ! json.isJsonObject()) return null;
		JsonObject jsonSkill = json.getAsJsonObject();
		
		DeriusSkill ret = new GsonSkill();
		JsonElement val;
		
		if (jsonSkill.has(ENABLED))
		{
			val = jsonSkill.get(ENABLED);
			boolean enabled = DeriusCore.get().gson.fromJson(val, Boolean.class);
			ret.setEnabled(enabled);
		}
		
		if (jsonSkill.has(NAME))
		{
			val = jsonSkill.get(NAME);
			String name = DeriusCore.get().gson.fromJson(val, String.class);
			ret.setName(name);
		}
		
		if (jsonSkill.has(DESC))
		{
			val = jsonSkill.get(DESC);
			String desc = DeriusCore.get().gson.fromJson(val, String.class);
			ret.setDesc(desc);
		}
		
		if (jsonSkill.has(EARN_EXP_DESCS))
		{
			val = jsonSkill.get(EARN_EXP_DESCS);
			List<String> descs = DeriusCore.get().gson.fromJson(val, new TypeToken<List<String>>(){}.getType());
			ret.setEarnExpDescs(descs);
		}
		
		if (jsonSkill.has(ICON))
		{
			val = jsonSkill.get(ICON);
			Material icon = DeriusCore.get().gson.fromJson(val, Material.class);
			ret.setIcon(icon);
		}
		
		if (jsonSkill.has(SP_AUTO_ASSIGNED))
		{
			val = jsonSkill.get(SP_AUTO_ASSIGNED);
			boolean assigned = DeriusCore.get().gson.fromJson(val, Boolean.class);
			ret.setSpAutoAssiged(assigned);
		}
		
		if (jsonSkill.has(SP_BLACKLISTED))
		{
			val = jsonSkill.get(SP_BLACKLISTED);
			boolean blacklisted = DeriusCore.get().gson.fromJson(val, Boolean.class);
			ret.setSpBlackListed(blacklisted);
		}
		
		if (jsonSkill.has(WORLDS_EARN))
		{
			val = jsonSkill.get(WORLDS_EARN);
			WorldExceptionSet worlds = DeriusCore.get().gson.fromJson(val, WorldExceptionSet.class);
			ret.setWorldsEarn(worlds);
		}
		
		if (jsonSkill.has(CONFIGURATION))
		{
			val = jsonSkill.get(CONFIGURATION);
			JsonObject conf = DeriusCore.get().gson.fromJson(val, JsonObject.class);
			ret.setConfiguration(conf);
		}
		
		return ret;
	}

}
