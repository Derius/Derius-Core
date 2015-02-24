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
	public boolean apply(CommandSender sender)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(sender);
		if (mplayer == null) return false;
		return mplayer.isCooldownExpired();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(sender);
		if (dplayer == null) return null;
		long expireMillis = dplayer.getCooldownExpireIn();
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.unitcounts(expireMillis, TimeUnit.getAll());
		String expireDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
		
		return Txt.parse(Txt.parse(MLang.get().exhausted, expireDesc));
	}
	
}
