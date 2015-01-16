package dk.muj.derius.req.sp;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.req.Req;
import dk.muj.derius.skill.Skill;

public class ReqIsSpecialised implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ReqIsSpecialised i = new ReqIsSpecialised();
	public static ReqIsSpecialised get() { return i; }
	private ReqIsSpecialised() {}
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		MPlayer player = MPlayer.get(sender);
		return player.isSpecialisedIn(skill);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse(MConf.get().msgSkillSpecialisationIsnt, skill.getDisplayName(sender));
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
	// OVERRIDE: DEFAULT
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

	// -------------------------------------------- //
	// OVERRIDE: CMD
	// -------------------------------------------- //
	
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