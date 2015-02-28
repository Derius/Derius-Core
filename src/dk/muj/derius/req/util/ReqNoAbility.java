package dk.muj.derius.req.util;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;

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
