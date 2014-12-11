package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityUtil;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;

// Shows you a skill with it's description and abilities and some level specific information.
public class CmdDeriusSkill extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSkill()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("level", "your level");
		
		this.setDesc("describes skill");
		
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		// Args
		// Skillname args
		Skill skill = this.arg (0, ARSkill.get());
		if (skill == null) return;
		
		// Level args
		Integer level = this.arg(1, ARInteger.get(), -1);
		if (level == null) return;
		
		
		// Message construction
		msgLines.add(Txt.titleize(Txt.parse("<green>" + skill.toString()))); // Titel
		msgLines.add("<lime>"+skill.getDescription());
		
		// Swapping between default and user inserted value
		if(level.intValue() <= -1)
		{
			LvlStatus status = msender.getLvlStatus(skill);
			msgLines.add(Txt.parse(status.toString()));
			level = status.getLvl();
		}
		else
		{
			msgLines.add(Txt.parse("<navy>LVL: <lime>"+level));
		}

		msgLines.add("<red>[<green>Passive abilities<red>]");
		for(Ability a :skill.getPassiveAbilities())
			msgLines.add(a.getDisplayedDescription(msender));
		msgLines.add("<red>[<green>Active abilities<red>]");
		for(Ability a :skill.getActiveAbilities())
			msgLines.add(a.getDisplayedDescription(msender));
		msgLines.add("<red>[<green>Level stats<red>]");
		for(Ability a :skill.getActiveAbilities())
			msgLines.add(Txt.parse("%s%s: <i>%s",AbilityUtil.CanUseAbilitySkillColor(a, msender), a.getName(), a.getLvlDescription(level)));

		
		// Send Message
		this.msg(Txt.parse(msgLines));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSkill;
    }
}
