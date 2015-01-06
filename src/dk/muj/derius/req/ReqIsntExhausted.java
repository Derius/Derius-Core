package dk.muj.derius.req;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class ReqIsntExhausted implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

    private static ReqIsntExhausted i = new ReqIsntExhausted();
	public static ReqIsntExhausted get() { return i; }
	private ReqIsntExhausted() {}
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender)
	{
		return MPlayer.get(sender).hasCooldownExpired();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		MPlayer p = MPlayer.get(sender);
		long expire = p.getCooldownExpireIn();
		LinkedHashMap<TimeUnit, Long> expireUnit = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(expire, TimeUnit.getAll()), 2);
		String expireDesc = TimeDiffUtil.formatedVerboose(expireUnit, "<i>");
		
		return Txt.parse(MConf.get().msgPrefix + MConf.get().msgExhausted, expireDesc);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
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
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
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
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVECOMMAND
	// -------------------------------------------- //
	
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
