package dk.muj.derius.entity.skill;


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
	public DeriusSkill load(DeriusSkill that)
	{
		DeriusSkill ret = super.load(that);
		ret.setEnabled(false);
		this.setEnabled(false);
		
		return ret;
	}
	
}
