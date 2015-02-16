package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;

public class ReqIsEnabled implements Req
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsEnabled i = new ReqIsEnabled();
	public static ReqIsEnabled get() { return i; }
	private ReqIsEnabled() {}
	

	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		return skill.isEnabled();
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse(MLang.get().skillDisabled, skill.getDisplayName(sender));
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		return ability.isEnabled();
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return Txt.parse(MLang.get().abilityDisabled, ability.getDisplayName(sender));
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
