package dk.muj.derius.req.util;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.player.DPlayer;

public interface ReqNoAbility extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(DPlayer dplayer, Ability ability)
	{
		throw new UnsupportedOperationException("This req doesn't support abilities.");
	}
	
	@Override
	public default String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		throw new UnsupportedOperationException("This req doesn't support abilities.");
	}
	
}
