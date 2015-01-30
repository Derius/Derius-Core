package dk.muj.derius.req.sp;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.Req;
import dk.muj.derius.skill.Skill;

public class ReqIsntBlacklisted implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ReqIsntBlacklisted i = new ReqIsntBlacklisted();
	public static ReqIsntBlacklisted get() { return i; }
	private ReqIsntBlacklisted() {}
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		return MConf.get().specialisationBlacklist.contains(skill.getId());
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse(MLang.get().specialisationBlacklisted, skill.getDisplayName(sender));
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		return this.apply(sender, ability.getSkill());
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return this.createErrorMessage(sender, ability.getSkill());
	}
	
	// -------------------------------------------- //
	// OVERRIDE: OTHER
	// -------------------------------------------- //
	
	@Override
	public String createErrorMessage(CommandSender arg0)
	{
		throw new IllegalArgumentException("This requirement does not support default");
	}

	@Override
	public boolean apply(CommandSender arg0)
	{
		throw new IllegalArgumentException("This requirement does not support default");
	}
	
	@Override
	public boolean apply(CommandSender arg0, MassiveCommand arg1)
	{
		throw new IllegalArgumentException("This requirement does not support commands");
	}
	
	@Override
	public String createErrorMessage(CommandSender arg0, MassiveCommand arg1)
	{
		throw new IllegalArgumentException("This requirement does not support commands");
	}
	
}
