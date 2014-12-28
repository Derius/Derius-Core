package dk.muj.derius.skill;

import java.util.Comparator;

public class SkillComparator implements Comparator<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SkillComparator i = new SkillComparator();
	public static SkillComparator get(){return i;}
	private SkillComparator(){}
	
	@Override
	public int compare(Skill s1, Skill s2)
	{
		int id1 = s1.getId();
		int id2 = s2.getId();
		
		if(id1 > id2)
			return 1;
		else if(id1 < id2)
			return -1;
		return -0;
	}

}
