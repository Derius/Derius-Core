package dk.muj.derius.entity.skill;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.DeriusConst;
import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Skill;

public class SkillColl extends Coll<DeriusSkill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SkillColl i = new SkillColl();
	public static SkillColl get() { return i; }
	private SkillColl()
	{
		super(DeriusConst.COLLECTION_SKILLS, DeriusSkill.class, MStore.getDb(), DeriusCore.get());
		this.setLowercasing(true);
		this.setCreative(false);
	}
	
	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public DeriusSkill createNewInstance()
	{
		return new GsonSkill();
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public static Collection<Skill> getAllSkills()
	{
		Set<Skill> skills = new HashSet<>();
		
		for (Skill skill : get().getAll())
		{
			if (skill == null) continue;
			if (skill instanceof GsonSkill) continue;
			skills.add(skill);
		}
		
		return skills;
	}
	
}
