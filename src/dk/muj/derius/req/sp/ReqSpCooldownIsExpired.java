package dk.muj.derius.req.sp;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqSpCooldownIsExpired implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqSpCooldownIsExpired i = new ReqSpCooldownIsExpired();
	public static ReqSpCooldownIsExpired get() { return i; }
	private ReqSpCooldownIsExpired() {}
	
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
	public boolean apply(DPlayer dplayer)
	{
		return dplayer.isSpecialisationCooldownExpired();
	}
	

	@Override
	public String createErrorMessage(DPlayer dplayer)
	{
		long spMillis = dplayer.getSpecialisationCooldownExpire() - System.currentTimeMillis();
		
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(spMillis, TimeUnit.getAllButMillisAndSeconds()), 3);
		String spDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
		String msg = "%s\n%s";
		msg = msg.replaceFirst("%s", DLang.get().getSpecialisationCantChange());
		msg = msg.replaceFirst("%s", DLang.get().getSpecialisationChangeCooldown().replaceAll("{time}", spDesc));
		
		return Txt.parse(msg);

	}

}
