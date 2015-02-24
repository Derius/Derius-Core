package dk.muj.derius.req.util;

import org.bukkit.command.CommandSender;

import dk.muj.derius.api.Req;

public interface ReqNoDefault extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender)
	{
		throw new UnsupportedOperationException("This req doesn't support default.");
	}
	
	@Override
	public default String createErrorMessage(CommandSender sender)
	{
		throw new UnsupportedOperationException("This req doesn't support default.");
	}

}
