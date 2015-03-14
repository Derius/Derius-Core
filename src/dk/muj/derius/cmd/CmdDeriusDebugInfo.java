 package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.skill.Skill;

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
		for (Skill skill : DeriusAPI.getAllSkills())
		{
			msgs.add(String.format("<red>%s %s", skill.getId(), skill.getName()));
		}
		
		msgs.add(Txt.titleize("<green>REGISTERED ABILITIES"));
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			msgs.add(String.format("<red>%s %s		   %s", ability.getId(), ability.getName(), ability.getSkill().getId()));
		}

		msgs.add(Txt.titleize("<green>REGISTERED PREPARABLE MATERIALS"));
		msgs.add(String.format("<red>%s", Txt.implodeCommaAnd(DeriusAPI.getPreparableTools(), "<i>, <red>", " <i>& <red>")));
		
		this.msg(msgs);
		
		return;
	}
	
}
