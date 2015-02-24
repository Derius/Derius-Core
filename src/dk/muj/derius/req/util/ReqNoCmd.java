package dk.muj.derius.req.util;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;

import dk.muj.derius.api.Req;

public interface ReqNoCmd extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: COMMAND
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, MassiveCommand cmd)
	{
		throw new UnsupportedOperationException("This req doesn't support commands.");
	}
	
	@Override
	public default String createErrorMessage(CommandSender sender, MassiveCommand cmd)
	{
		throw new UnsupportedOperationException("This req doesn't support commands.");
	}
}
