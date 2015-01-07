package dk.muj.derius.util;

import com.massivecraft.massivecore.util.PermUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;

public class DeriusPermUtil
{

	// -------------------------------------------- //
	// MIXIN
	// -------------------------------------------- //
	
	public static String getDeniedMessage(String perm)
	{
		do
		{
			if (perm.startsWith(Perm.ABILITY_USE.node))
			{	
				int id = getIdFromPerm(perm);
				Ability ability = Ability.getAbilityById(id);
				if (ability == null) break;
				return Txt.parse("<b>You do not have permission to use the ability %s", ability.getName());
			}
			else if (perm.startsWith(Perm.ABILITY_SEE.node))
			{	
				int id = getIdFromPerm(perm);
				Ability ability = Ability.getAbilityById(id);
				if (ability == null) break;
				return Txt.parse("<b>You do not have permission to see that ability");
			}
			else if (perm.startsWith(Perm.SKILL_LEARN.node))
			{	
				int id = getIdFromPerm(perm);
				Skill skill = Skill.getSkillById(id);
				if (skill == null) break;
				return Txt.parse("<b>You do not have permission to learn the skill %s", skill.getName());
			}
			else if (perm.startsWith(Perm.SKILL_SEE.node))
			{	
				int id = getIdFromPerm(perm);
				Skill skill = Skill.getSkillById(id);
				if (skill == null) break;
				return Txt.parse("<b>You do not have permission to see that skill");
			}
		}while (false);
		return PermUtil.getDeniedMessage(perm);
	}
	
	// -------------------------------------------- //
	// UTIL METHODS
	// -------------------------------------------- //
	
	public static boolean isInteger(String s) 
	{
		if (s == null || s.isEmpty()) return false;
		int max = s.charAt(0) == '-' ? 10 : 9;
		if ( s.length() > max) return false;
	    for (int i = 0; i < s.length(); i++) 
	    {
	    	if(i == 0 && s.charAt(0) == '-') 
	    	{
	    		if(s.length() == 1) return false;
	    		else continue;
	    	}
	    	if(Character.digit(s.charAt(i), 10) < 0) return false;
	    }
	    return true;
	}
	
	public static int getIdFromPerm(String perm)
	{
		int index = perm.lastIndexOf(".");
		if (index < 0) return 0;
		String strId = perm.substring(index);
		if ( ! isInteger(strId)) return 0;
		return Integer.parseInt(strId);
	}
}
