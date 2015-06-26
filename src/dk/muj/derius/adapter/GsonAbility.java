package dk.muj.derius.adapter;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.lib.optional.Optional;

public final class GsonAbility extends AbilityAbstract<Object>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public GsonAbility() { }

	// -------------------------------------------- //
	// OVERRIDE: ACTUAL
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return null;
	}
	
	@Override
	public boolean isEnabled()
	{
		return false;
	}

	// -------------------------------------------- //
	// OVERRIDE: REST
	// -------------------------------------------- //
	
	@Override
	public Skill getSkill()
	{
		return null;
	}

	@Override
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		return Optional.empty();
	}

	@Override
	public Object onActivate(DPlayer dplayer, Object other)
	{
		return null;
	}

	@Override
	public void onDeactivate(DPlayer dplayer, Object other)
	{
		
	}

}
