package dk.muj.derius.entity.adapter;

import dk.muj.derius.entity.DeriusSkill;

public final class GsonSkill extends DeriusSkill
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public GsonSkill()
	{
		
	}

	// -------------------------------------------- //
	// OVERRIDE
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
	
}
