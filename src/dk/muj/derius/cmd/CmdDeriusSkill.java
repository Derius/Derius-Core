package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;

public class CmdDeriusSkill extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSkill()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("level", "your level");
		
		this.setDesc("Shows a skill, it's description, the passive and active abilities and level specific information");
		
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		//Args
		// skillname args
		Skill skill = this.arg (0, ARSkill.get());
		if (skill == null) return;
		
		
		// level args
		Integer level = this.arg(1, ARInteger.get(), -1);
		if (level == null) return;
		
		
		// Message construction
		msgLines.add(Txt.parse("<bold><under><i>" + skill.toString())); // Titel
		msgLines.add(skill.getDesc());
		
		// Swapping between default and user inserted value
		if(level.intValue() == -1)
		{
			LvlStatus status = msender.getLvlStatus(skill.getId());
			msgLines.add(Txt.parse("<grey>LVL:"+status.toString()));
		}
		else
		{
			msgLines.add(Txt.parse("<grey>LVL:<yellow>"+level));
		}

		msgLines.add("<i><under>Passive abilities");
		msgLines.addAll(skill.getPassiveAbilityDescriptions());
		msgLines.add("<i><under>Active abilities");
		msgLines.addAll(skill.getActiveAbilityDescriptions());
		msgLines.add("<i><under>Level stats");
		msgLines.addAll(skill.getAbilitiesDecriptionByLvl(level));
		

		this.msg(msgLines);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSkill;
    }
}
