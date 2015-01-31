 package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.AbilityColl;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.util.Listener;


public class CmdDeriusDebugInfo extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusDebugInfo()
	{
		this.addAliases("info");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> messages = new ArrayList<String>();
		
		messages.add(Txt.parse("<art>REGISTERED SKILLS"));
		for (Skill skill : SkillColl.getAllSkills())
		{
			messages.add(Txt.parse("<red>%s %s", skill.getId(), skill.getName()));
		}
		
		messages.add(Txt.parse("<art>REGISTERED ABILITIES"));
		for (Ability ability : AbilityColl.getAllAbilities())
		{
			messages.add(Txt.parse("<red>%s %s           %s", ability.getId(), ability.getName(), ability.getSkill().getId()));
		}

		messages.add(Txt.parse("<art>REGISTERED INTERACT MATERIALS"));
		messages.add(Txt.parse("<red>%s", Txt.implodeCommaAnd(Listener.getRegisteredInteractTools(), "<i>, <red>", " <i>& <red>")));
		
		sendMessage(messages);
		
		return;
	}
	
}
