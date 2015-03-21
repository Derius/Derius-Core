package dk.muj.derius.adapter;

import java.lang.reflect.Type;
import java.util.OptionalInt;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.lvl.LvlStatus;
import dk.muj.derius.api.lvl.LvlStatusDefault;

public class LvlStatusAdapter implements JsonDeserializer<LvlStatus>, JsonSerializer<LvlStatus>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static LvlStatusAdapter i = new LvlStatusAdapter();
	public static LvlStatusAdapter get() { return i; }
	public LvlStatusAdapter() {}
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String LEVEL = "Level";
	
	public static final String EXP = "Exp";
	
	public static final String EXP_TO_NEXT = "Exp to next level";
	
	// -------------------------------------------- //
	// SERIALIZE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(LvlStatus src, Type typeOfSrc, JsonSerializationContext context)
	{	
		if (src == null) return null;
		
		JsonObject ret = new JsonObject();
		JsonElement val;
		
		val = DeriusPlugin.get().gson.toJsonTree(src.getLvl());
		ret.add(LEVEL, val);
		
		if (src.getExp().isPresent())
		{
			val = DeriusPlugin.get().gson.toJsonTree(src.getExp().getAsInt());
			ret.add(EXP, val);
		}
		
		if (src.getExpToNextLvl().isPresent())
		{
			val = DeriusPlugin.get().gson.toJsonTree(src.getExpToNextLvl().getAsInt());
			ret.add(EXP_TO_NEXT, val);
		}
		
		return ret;
	}

	// -------------------------------------------- //
	// DESERIALIZE
	// -------------------------------------------- //
	
	@Override
	public LvlStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{	
		if (json == null) return null;
	
		// It must be an object!
		if ( ! json.isJsonObject())
		{
			DeriusAPI.debug(10000, "Non jsonObject %s", json);
			return null;
		}
		
		JsonObject jsonStatus = json.getAsJsonObject();
	
		int level;
		OptionalInt exp = OptionalInt.empty();
		OptionalInt expToNext = OptionalInt.empty();
		
		JsonElement val;
		
		
		if (jsonStatus.has(LEVEL))
		{
			val = jsonStatus.get(LEVEL);
			level = DeriusPlugin.get().gson.fromJson(val, int.class);
		}
		else
		{
			level = 0;
		}
		
		if (jsonStatus.has(EXP))
		{
			val = jsonStatus.get(EXP);
			exp = OptionalInt.of(DeriusPlugin.get().gson.fromJson(val, int.class));
		}
		
		if (jsonStatus.has(EXP_TO_NEXT))
		{
			val = jsonStatus.get(EXP_TO_NEXT);
			expToNext = OptionalInt.of(DeriusPlugin.get().gson.fromJson(val, int.class));
		}
		
		return LvlStatusDefault.valueOf(level, exp, expToNext);
	}

}
