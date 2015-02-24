package dk.muj.derius.req.util;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;

public interface ReqToDefault extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, Skill skill)
	{
		return this.apply(sender);
	}

	@Override
	public default String createErrorMessage(CommandSender sender, Skill skill)
	{
		return this.createErrorMessage(sender);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, Ability ability)
	{
		return this.apply(sender);
	}

	@Override
	public default String createErrorMessage(CommandSender sender, Ability ability)
	{
		return this.createErrorMessage(sender);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COMMANDS
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, MassiveCommand cmd)
	{
		return this.apply(sender);
	}

	@Override
	public default String createErrorMessage(CommandSender sender, MassiveCommand cmd)
	{
		return this.createErrorMessage(sender);
	}

}
