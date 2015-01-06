package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.PermUtil;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.util.DeriusPermUtil;

public class ReqHasPerm implements Req
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqHasPerm get(String perm) { return new ReqHasPerm(perm); }
	public ReqHasPerm(String perm) { this.perm = perm; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String perm;
	public String getPerm() { return this.perm; }
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		return PermUtil.has(sender, perm);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return DeriusPermUtil.getDeniedMessage(perm);
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
	public boolean apply(CommandSender sender, MassiveCommand cmd)
	{
		return this.apply(sender);
	}
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand cmd)
	{
		return this.createErrorMessage(sender);
	}

	
}
