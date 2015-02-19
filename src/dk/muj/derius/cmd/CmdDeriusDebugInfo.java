 package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.ability.AbilityColl;
import dk.muj.derius.entity.skill.SkillColl;
import dk.muj.derius.util.Listener;


public class CmdDeriusDebugInfo extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusDebugInfo()
	{
		// Aliases
		this.addAliases("info");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgs = new ArrayList<String>();
		
		msgs.add(Txt.titleize("<green>REGISTERED SKILLS"));
		for (Skill skill : SkillColl.getAllSkills())
		{
			msgs.add(String.format("<red>%s %s", skill.getId(), skill.getName()));
		}
		
		msgs.add(Txt.titleize("<green>REGISTERED ABILITIES"));
		for (Ability ability : AbilityColl.getAllAbilities())
		{
			msgs.add(String.format("<red>%s %s		   %s", ability.getId(), ability.getName(), ability.getSkill().getId()));
		}

		msgs.add(Txt.titleize("<green>REGISTERED INTERACT MATERIALS"));
		msgs.add(String.format("<red>%s", Txt.implodeCommaAnd(Listener.getRegisteredInteractTools(), "<i>, <red>", " <i>& <red>")));
		
		this.msg(msgs);
		
		return;
	}
	
}
