package dk.muj.derius.entity.ability;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;

public class GsonAbility extends DeriusAbility
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
	public DeriusAbility load(DeriusAbility that)
	{
		DeriusAbility ret = super.load(that);
		ret.setEnabled(false);
		this.setEnabled(false);
		
		return ret;
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
		return "none";
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
