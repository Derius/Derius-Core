package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;

public class ReqIsAtleastLevel implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsAtleastLevel get(int level) { return new ReqIsAtleastLevel(level); }
	private ReqIsAtleastLevel(int level) { this.level = level; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final int level;
	public int getlevel() { return this.level; }
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		return (skill.getLvlStatusFromExp(DeriusAPI.getDPlayer(sender).getExp(skill)).getLvl() >= level);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse(MLang.get().skillLevelIsTooLow, this.getlevel(), skill.getDisplayName(sender));
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
	public boolean apply(CommandSender arg0, MassiveCommand arg1)
	{
		throw new UnsupportedOperationException("This req doesn't support commands");
	}

	@Override
	public String createErrorMessage(CommandSender arg0, MassiveCommand arg1)
	{
		throw new UnsupportedOperationException("This req doesn't support commands");
	}
	
	
	@Override
	public boolean apply(CommandSender sender)
	{
		throw new UnsupportedOperationException("This req doesn't support default");
	}

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		throw new UnsupportedOperationException("This req doesn't support default");
	}

}
