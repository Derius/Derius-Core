package dk.muj.derius.req;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqStandStill implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqStandStill get(long millis) { return new ReqStandStill(millis); }
	private ReqStandStill(long millis) { this.setMillis(millis); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private long millis;
	public long getMillis() { return millis; }
	public void setMillis(long millis){ this.millis = millis; }
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.HIGH;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(sender);
		if (mplayer == null) return false;
		return mplayer.isSpecialisationCooldownExpired();
	}
	

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(sender);
		if (mplayer == null) return null;
		long spMillis = mplayer.getSpecialisationCooldownExpire() - System.currentTimeMillis();
		
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(spMillis, TimeUnit.getAllButMillisAndSeconds()), 3);
		String spDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
		String msg = "%s\n%s";
		msg = msg.replaceFirst("%s", MLang.get().specialisationCantChange);
		msg = msg.replaceFirst("%s", String.format(MLang.get().specialisationChangeCooldown, spDesc) );
		
		return Txt.parse(msg);

	}
	
}
