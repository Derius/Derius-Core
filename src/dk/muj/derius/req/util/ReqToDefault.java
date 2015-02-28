package dk.muj.derius.req.util;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;

public interface ReqToDefault extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(DPlayer dplayer, Skill skill)
	{
		return this.apply(dplayer);
	}

	@Override
	public default String createErrorMessage(DPlayer dplayer, Skill skill)
	{
		return this.createErrorMessage(dplayer);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(DPlayer dplayer, Ability ability)
	{
		return this.apply(dplayer);
	}

	@Override
	public default String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		return this.createErrorMessage(dplayer);
	}
	
}
