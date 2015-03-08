package dk.muj.derius.req;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqCooldownIsExpired implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqCooldownIsExpired i = new ReqCooldownIsExpired();
	public static ReqCooldownIsExpired get() { return i; }
	private ReqCooldownIsExpired() {}
	
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
		return dplayer.isCooldownExpired();
	}
	
	@Override
	public String createErrorMessage(DPlayer dplayer)
	{
		long expireMillis = dplayer.getCooldownExpireIn();
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.unitcounts(expireMillis, TimeUnit.getAllButMillis());
		String expireDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
		
		return Txt.parse(DLang.get().getPrefix() + " " + DLang.get().getExhausted().replaceAll("{time}", expireDesc));
	}
	
}
