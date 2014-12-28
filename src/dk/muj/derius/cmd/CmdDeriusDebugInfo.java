 package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;


public class CmdDeriusDebugInfo extends DeriusCommand
{
	public CmdDeriusDebugInfo()
	{
		super.setDesc("info for debugging");
		
		super.addAliases("info");
	}
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		msgLines.add(Txt.parse("<art>REGISTERED SKILLS"));
		for(Skill skill: Skill.GetAllSkills())
			msgLines.add(Txt.parse("<red>")+skill.getId() +" "+ skill.getName());
		
		msgLines.add(Txt.parse("<art>REGISTERED ABILITIES"));
		for(Ability ability: Ability.GetAllAbilities())
			msgLines.add(Txt.parse("<red>")+ability.getId() +" "+ ability.getName()+"          "+ability.getSkill().getId());

		for (String string : msgLines)
		{
			sender.sendMessage(string);
		}
	}
}
