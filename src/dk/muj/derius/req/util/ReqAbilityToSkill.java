package dk.muj.derius.req.util;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;

public interface ReqAbilityToSkill extends Req
{
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public default boolean apply(DPlayer dplayer, Ability ability)
	{
		return this.apply(dplayer, ability.getSkill());	
	}

	@Override
	public default String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		return this.createErrorMessage(dplayer, ability.getSkill());
	}
	
}
