package dk.muj.derius.comparator;

import java.util.Comparator;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.LvlStatus;
import dk.muj.derius.api.Skill;

public class SkillComparatorLvl implements Comparator<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static SkillComparatorLvl get(DPlayer dplayer) { return new SkillComparatorLvl(dplayer); }
	public SkillComparatorLvl(DPlayer dplayer)
	{
		this.dplayer = dplayer;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final DPlayer dplayer;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	@Override
	public int compare(Skill s1, Skill s2)
	{
		int ret = 0;
		
		// Null
		if (s1 == null && s2 == null) return 0;
		if (s1 == null) return -1;
		if (s2 == null) return +1;
		
		LvlStatus st1 = dplayer.getLvlStatus(s1);
		LvlStatus st2 = dplayer.getLvlStatus(s2);
		
		ret = st1.getLvl() - st2.getLvl();
		if (ret != 0) return ret;
		
		// Exp 
		ret = st1.getExp().orElse(0) - st2.getExp().orElse(0);
		if (ret != 0) return ret;
		
		//Exp to next
		ret = st2.getExpToNextLvl().orElse(0) - st1.getExpToNextLvl().orElse(0);
		if (ret != 0) return ret;
		
		return ret;
	}
	

}
