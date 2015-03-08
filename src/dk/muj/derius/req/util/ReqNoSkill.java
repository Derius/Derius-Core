package dk.muj.derius.req.util;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;

public interface ReqNoSkill extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(DPlayer dplayer, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills.");
	}

	@Override
	public default String createErrorMessage(DPlayer dplayer, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills.");
	}
	
}
