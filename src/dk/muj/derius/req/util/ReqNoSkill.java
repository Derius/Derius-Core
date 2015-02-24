package dk.muj.derius.req.util;

import org.bukkit.command.CommandSender;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;

public interface ReqNoSkill extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(CommandSender sender, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills.");
	}

	@Override
	public default String createErrorMessage(CommandSender sender, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills.");
	}
	
}
