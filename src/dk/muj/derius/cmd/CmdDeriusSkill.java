package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;

// Shows this skills with it's description and the ability with level specific information.
public class CmdDeriusSkill extends DeriusCommand
{
	public CmdDeriusSkill()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("level", "your level");
		
		this.setDesc("Shows a skill, it's description, the passive and active abilities and level specific information");
		
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}
	
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
		
		if(level.intValue() == -1)
		{
			LvlStatus status = msender.getLvlStatus(skill.getId());
			msgLines.add(Txt.parse("<grey>LVL:"+status.toString()));
		}
		else
		{
			msgLines.add(Txt.parse("<grey>LVL:<yellow>"+level));
		}
		
		
		msgLines.add(skill.toString());
		msgLines.add("------------");
		msgLines.add(skill.getDesc());
		msgLines.add("<i>Passive abilities");
		
		msgLines.add("<i>Passive abilities");
		msgLines.addAll(skill.getPassiveAbilityDescriptions());
		msgLines.add("<i>Active abilities");
		msgLines.addAll(skill.getActiveAbilityDescriptions());
		msgLines.add("<i>Active abilities");

		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSkill;
    }
}
