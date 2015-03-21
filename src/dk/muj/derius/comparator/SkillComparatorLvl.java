package dk.muj.derius.comparator;

import java.util.Comparator;

import org.apache.commons.lang.Validate;

import dk.muj.derius.api.lvl.LvlStatus;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;

public class SkillComparatorLvl implements Comparator<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static SkillComparatorLvl get(DPlayer dplayer) { return new SkillComparatorLvl(dplayer); }
	public SkillComparatorLvl(DPlayer dplayer)
	{
		Validate.notNull(dplayer, "dplayer must not be null");
		this.dplayer = dplayer;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final DPlayer dplayer;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compare(Skill s1, Skill s2)
	{
		if (s1 == s2) return 0;
		
		// Null
		if (s1 == null && s2 == null) return 0;
		if (s1 == null) return -1;
		if (s2 == null) return +1;
		
		int ret = 0;
		
		if (dplayer.isSpecialisedIn(s1) && dplayer.isSpecialisedIn(s2)) ret = 0;
		else if (dplayer.isSpecialisedIn(s1)) ret = -1;
		else if (dplayer.isSpecialisedIn(s2)) ret = +1;
		if (ret != 0) return ret;
		
		LvlStatus st1 = dplayer.getLvlStatus(s1);
		LvlStatus st2 = dplayer.getLvlStatus(s2);
		
		ret = st2.getLvl() - st1.getLvl();
		if (ret != 0) return ret;
		
		// Exp 
		ret = st2.getExp().orElse(0) - st1.getExp().orElse(0);
		if (ret != 0) return ret;
		
		//Exp to next
		ret = st1.getExpToNextLvl().orElse(Integer.MAX_VALUE) - st2.getExpToNextLvl().orElse(Integer.MAX_VALUE);
		if (ret != 0) return ret;
		
		return ret;
	}
	

}
