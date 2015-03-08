package dk.muj.derius.adapter;

import dk.muj.derius.api.ability.DeriusAbility;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;

public final class GsonAbility extends DeriusAbility
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public GsonAbility()
	{
		
	}

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
	public String getLvlDescriptionMsg(int lvl)
	{
		return null;
	}

	@Override
	public Object onActivate(DPlayer p, Object other)
	{
		return null;
	}

	@Override
	public void onDeactivate(DPlayer p, Object other)
	{
		
	}

}
