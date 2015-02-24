package dk.muj.derius.req.util;

import org.bukkit.command.CommandSender;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Req;

public interface ReqNoAbility extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, Ability ability)
	{
		throw new UnsupportedOperationException("This req doesn't support abilities.");
	}
	
	@Override
	public default String createErrorMessage(CommandSender sender, Ability ability)
	{
		throw new UnsupportedOperationException("This req doesn't support abilities.");
	}
	
}
