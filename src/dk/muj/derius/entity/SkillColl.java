package dk.muj.derius.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.DeriusConst;
import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.adapter.GsonSkill;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.skill.Skill;

public final class SkillColl extends Coll<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SkillColl i = new SkillColl();
	public static SkillColl get() { return i; }
	private SkillColl()
	{
		super(DeriusConst.COLLECTION_SKILLS, Skill.class, MStore.getDb(), DeriusPlugin.get());
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
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public Skill createNewInstance()
	{
		return new GsonSkill();
	}
	
	@Override
	public void copy(Object ofrom, Object oto)
	{
		if (ofrom == null) throw new IllegalArgumentException("ofrom");
		if (oto == null) throw new IllegalArgumentException("oto");
		
		if ( ! (ofrom instanceof Skill)) throw new IllegalArgumentException("ofrom must be a skill");
		if ( ! (oto instanceof Skill)) throw new IllegalArgumentException("ofrom must be a skillcd");
		
		Skill sfrom = (Skill) ofrom;
		Skill sto = (Skill) oto;
		
		sto.load(sfrom);
	}
	
	@Override
	public String fixId(Object oid)
	{
		if (oid instanceof String) return (String) oid;
		if (oid instanceof Skill) return ((Skill) oid).getId();
		if (oid instanceof Ability) return ((Ability<?>) oid).getSkill().getId();
		return null;
	}
	
}
