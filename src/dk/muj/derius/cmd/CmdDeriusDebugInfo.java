 package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.util.Listener;


public class CmdDeriusDebugInfo extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusDebugInfo()
	{
		super.setDesc("info for debugging");
		
		super.addAliases("info");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		msgLines.add(Txt.parse("<art>REGISTERED SKILLS"));
		for(Skill skill: Skill.getAllSkills())
			msgLines.add(Txt.parse("<red>")+skill.getId() +" "+ skill.getName());
		
		msgLines.add(Txt.parse("<art>REGISTERED ABILITIES"));
		for(Ability ability: Ability.getAllAbilities())
			msgLines.add(Txt.parse("<red>")+ability.getId() +" "+ ability.getName()+"          "+ability.getSkill().getId());

		msgLines.add(Txt.parse("<art>REGISTERED INTERACT MATERIALS"));
		msgLines.add("<red>"+Txt.implodeCommaAnd(Listener.getRegisteredInteractTools(), "<i>, <red>", " <i>& <red>"));
		
		msg(msgLines);
	}
	
}
