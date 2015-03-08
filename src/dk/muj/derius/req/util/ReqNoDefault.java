package dk.muj.derius.req.util;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.player.DPlayer;

public interface ReqNoDefault extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(DPlayer dplayer)
	{
		throw new UnsupportedOperationException("This req doesn't support default.");
	}
	
	@Override
	public default String createErrorMessage(DPlayer dplayer)
	{
		throw new UnsupportedOperationException("This req doesn't support default.");
	}

}
