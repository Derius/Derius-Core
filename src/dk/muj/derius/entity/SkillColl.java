package dk.muj.derius.entity;

import java.util.Collection;

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
		return get().getAll();
	}
	
}
