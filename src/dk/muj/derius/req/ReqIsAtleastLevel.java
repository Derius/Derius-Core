package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class ReqIsAtleastLevel implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsAtleastLevel get(int level) { return new ReqIsAtleastLevel(level); }
	public ReqIsAtleastLevel(int level) { this.level = level; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final int level;
	public int getlevel() { return this.level; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	

	@Override
	public boolean apply(CommandSender arg0)
	{
		return false;
	}
	

	@Override
	public String createErrorMessage(CommandSender arg0)
	{
		return Txt.parse("<b>You are no the required level to do this.");
	}

	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		if (skill.getLvlStatusFromExp(MPlayer.get(sender).getExp(skill)).getLvl() >= level) return true;
		return false;
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return this.createErrorMessage(sender);
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
		return this.createErrorMessage(sender);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVECOMMAND
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender arg0, MassiveCommand arg1)
	{
		return false;
	}

	@Override
	public String createErrorMessage(CommandSender arg0, MassiveCommand arg1)
	{
		return Txt.parse("<b>This should not happen, a bug occured. A COMMAND WAS PASSED INSTEAD OF SKILL");
	}

}
