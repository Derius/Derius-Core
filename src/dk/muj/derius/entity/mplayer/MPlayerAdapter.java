package dk.muj.derius.entity.mplayer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.DeriusPlugin;

public class MPlayerAdapter implements JsonDeserializer<MPlayer>, JsonSerializer<MPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MPlayerAdapter i = new MPlayerAdapter();
	public static MPlayerAdapter get() { return i; }
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String EXP = "Experience";
	
	public static final String SPECIALISED = "Specialised";
	
	public static final String SPECIALISED_MILLIS = "Specialised-millis";
	
	public static final String SPECIALISATION_BONUS = "specialisation-bonus";
	
	public static final String STAMINA = "stamina";
	
	public static final String BONUS_STAMINA = "bonus-stamina";
	
	public static final String STAMINA_BOARD_STAY = "stamina-board-stay";
	
	public static final String BOARD_SHOW_AT_ALL = "board-show-at-all";
	
	// -------------------------------------------- //
	// OVERRIDE: SERIALIZE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(MPlayer src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src == null) return null;
		
		JsonObject ret = new JsonObject();
		JsonElement val;
		
		val = DeriusPlugin.get().gson.toJsonTree(src.exp);
		ret.add(EXP, val);
		
		val = DeriusPlugin.get().gson.toJsonTree(src.specialised);
		ret.add(SPECIALISED, val);
		
		val = DeriusPlugin.get().gson.toJsonTree(src.getSpecialisationChangeMillis());
		ret.add(SPECIALISED_MILLIS, val);
		
		val = DeriusPlugin.get().gson.toJsonTree(src.getSpecialisationSlotBonus());
		ret.add(SPECIALISATION_BONUS, val);

		val = DeriusPlugin.get().gson.toJsonTree(src.getStaminaBonus());
		ret.add(BONUS_STAMINA, val);
		
		val = DeriusPlugin.get().gson.toJsonTree(src.getStaminaBoardStay());
		ret.add(STAMINA_BOARD_STAY, val);
		
		val = DeriusPlugin.get().gson.toJsonTree(src.getBoardShowAtAll());
		ret.add(BOARD_SHOW_AT_ALL, val);
		
		return ret;
	}

	// -------------------------------------------- //
	// OVERRIDE: DESERIALIZE
	// -------------------------------------------- //
	
	@Override
	public MPlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		
		// It must be an object!
		if ( ! json.isJsonObject()) return null;
		JsonObject jsonSkill = json.getAsJsonObject();
		
		MPlayer ret = new MPlayer();
		JsonElement val;
		
		if (jsonSkill.has(EXP))
		{
			val = jsonSkill.get(EXP);
			Map<String, Long> exp = DeriusPlugin.get().gson.fromJson(val, new TypeToken<Map<String, Long>>(){}.getType());
			ret.exp = exp;
		}
		
		if (jsonSkill.has(SPECIALISED))
		{
			val = jsonSkill.get(SPECIALISED);
			Set<String> specialised = DeriusPlugin.get().gson.fromJson(val, new TypeToken<Set<String>>(){}.getType());
			ret.specialised = specialised;
		}
		
		if (jsonSkill.has(SPECIALISED_MILLIS))
		{
			val = jsonSkill.get(SPECIALISED_MILLIS);
			long millis = DeriusPlugin.get().gson.fromJson(val, Long.TYPE);
			ret.setSpecialisationChangeMillis(millis);
		}
		
		if (jsonSkill.has(SPECIALISATION_BONUS))
		{
			val = jsonSkill.get(SPECIALISATION_BONUS);
			Map<String, Integer> bonus = DeriusPlugin.get().gson.fromJson(val, new TypeToken<Map<String, Integer>>(){}.getType());
			ret.specialisationBonus = bonus;
		}
		
		if (jsonSkill.has(BONUS_STAMINA))
		{
			val = jsonSkill.get(BONUS_STAMINA);
			Map<String, Double> staminaBonus = DeriusPlugin.get().gson.fromJson(val, new TypeToken<Map<String, Double>>(){}.getType());
			ret.staminaBonus = staminaBonus;
		}
		
		if (jsonSkill.has(STAMINA_BOARD_STAY))
		{
			val = jsonSkill.get(STAMINA_BOARD_STAY);
			boolean staminaBoardStay = DeriusPlugin.get().gson.fromJson(val, Boolean.TYPE);
			ret.staminaBoardStay = staminaBoardStay;
		}
		
		if (jsonSkill.has(BOARD_SHOW_AT_ALL))
		{
			val = jsonSkill.get(BOARD_SHOW_AT_ALL);
			boolean boardShowAtAll = DeriusPlugin.get().gson.fromJson(val, Boolean.TYPE);
			ret.boardShowAtAll = boardShowAtAll;
		}
		
		return ret;
	}
	
}
