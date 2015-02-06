package dk.muj.derius.req;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;

public class ReqCooldownIsExpired implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ReqCooldownIsExpired i = new ReqCooldownIsExpired();
	public static ReqCooldownIsExpired get() { return i; }
	private ReqCooldownIsExpired() {}
	
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
		DPlayer mplayer = DeriusAPI.getDPlayer(sender);
		if (mplayer == null) return null;
		long expireMillis = mplayer.getCooldownExpireIn();
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.unitcounts(expireMillis, TimeUnit.getAll());
		String expireDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
		
		return Txt.parse(Txt.parse(MLang.get().exhausted, expireDesc));
	}

	// -------------------------------------------- //
	// OVERRIDE: OTHER
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		return this.apply(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return this.createErrorMessage(sender);
	}
	
	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		return this.apply(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return this.createErrorMessage(sender);
	}
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand arg1)
	{
		return this.apply(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand arg1)
	{
		return this.createErrorMessage(sender);
	}
	
}
