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
	public boolean apply(CommandSender sender)
	{
		return DeriusAPI.getDPlayer(sender).isCooldownExpired();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(sender);
		long expire = mplayer.getCooldownExpireIn();
		LinkedHashMap<TimeUnit, Long> expireUnit = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(expire, TimeUnit.getAll()), 2);
		String expireDesc = TimeDiffUtil.formatedVerboose(expireUnit, "<i>");
		
		return Txt.parse(MLang.get().prefix + MLang.get().exhausted, expireDesc);
	}
	
}
