package dk.muj.derius.req;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

public class ReqStandStill implements Req
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
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		MPlayer mplayer = MPlayer.get(sender);
		if (mplayer == null) return false;
		return mplayer.isSpecialisationCooldownExpired();
	}
	

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		MPlayer mplayer = MPlayer.get(sender);
		if (mplayer == null) return null;
		long spMillis = mplayer.getSpecialisationCooldownExpire() - System.currentTimeMillis();
		
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(spMillis, TimeUnit.getAllButMillisAndSeconds()), 3);
		String spDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
		String msg = "%s\n%s";
		msg = msg.replaceFirst("%s", MLang.get().specialisationCantChange);
		msg = msg.replaceFirst("%s", String.format(MLang.get().specialisationChangeCooldown, spDesc) );
		
		return Txt.parse(msg);

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
