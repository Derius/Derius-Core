package dk.muj.derius.req.util;

import org.bukkit.command.CommandSender;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Req;

public interface ReqAbilityToSkill extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, Ability ability)
	{
		return this.apply(sender, ability.getSkill());	
	}

	@Override
	public default String createErrorMessage(CommandSender sender, Ability ability)
	{
		return this.createErrorMessage(sender, ability.getSkill());
	}
	
}
