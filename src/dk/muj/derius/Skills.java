package dk.muj.derius;

import java.util.ArrayList;
import java.util.List;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;

public final class Skills
{
	//This is what we consider a static method
	//So it should not be instantiated.
	private Skills(){};
	
	//A list of skill which we get from different sources.
	private static List<Skill> skillList = new ArrayList<Skill>();
	
	/**
	 * Adds a skill to our system.
	 * We will instantiate the correct fields.
	 * You still have to add exp & enforce the abilities players get.
	 * This should be done on server startup.
	 * @param {Skill} The skill you want to add
	 */
	public static void AddSkill(Skill skill)
	{
		skillList.add(skill);
		for (MPlayer p: MPlayerColl.get().getAll())
			p.InstantiateSkill(skill.getId());
	}
	
	/**
	 * Gets a skill from its id. 
	 * This is the best way to get a skill, since the id never changes.
	 * @param {String} The id of the skill you wanted to get.
	 * @return{Skill} The skill which has this id
	 */
	public static Skill GetSkillById(String skillId)
	{
		for(Skill skill: Skills.skillList)
		{
			if(skill.getId().equals(skillId))
				return skill;
		}
		return null;
	}
	
	/**
	 * Gets a skill from its name.
	 * This should only be done by players. Since they don't know the id
	 * @param {String} The name of the skill you wanted to get.
	 * @return{Skill} The skill which starts with this name
	 */
	public static Skill GetSkillByName(String skillName)
	{
		for(Skill skill: skillList)
		{
			if(skill.getName().startsWith(skillName))
				return skill;
		}
		return null;
	}
	
	public static List<Skill> GetAllSkills()
	{
		return new ArrayList<Skill>(skillList);
	}
}
