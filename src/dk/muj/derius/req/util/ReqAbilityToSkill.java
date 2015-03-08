package dk.muj.derius.req.util;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.player.DPlayer;

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
