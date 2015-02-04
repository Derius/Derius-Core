package dk.muj.derius.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.Const;
import dk.muj.derius.Derius;

public class SkillColl extends Coll<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SkillColl i = new SkillColl();
	public static SkillColl get() { return i; }
	private SkillColl()
	{
		super(Const.COLLECTION_SKILLS, Skill.class, MStore.getDb(), Derius.get());
		this.setLowercasing(true);
		this.setCreative(false);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public static Collection<Skill> getAllSkills()
	{
		Set<Skill> skills = new HashSet<Skill>();
		
		for (Skill skill : get().getAll())
		{
			if (skill == null) continue;
			skills.add(skill);
		}
		
		return skills;
	}
	
}
