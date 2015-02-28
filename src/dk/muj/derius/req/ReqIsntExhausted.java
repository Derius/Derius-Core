package dk.muj.derius.req;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqIsntExhausted implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqIsntExhausted i = new ReqIsntExhausted();
	public static ReqIsntExhausted get() { return i; }
	private ReqIsntExhausted() {}
	
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
		long expire = dplayer.getCooldownExpireIn();
		LinkedHashMap<TimeUnit, Long> expireUnit = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(expire, TimeUnit.getAll()), 2);
		String expireDesc = TimeDiffUtil.formatedVerboose(expireUnit, "<i>");
		
		return Txt.parse(MLang.get().prefix + MLang.get().exhausted, expireDesc);
	}
	
}
