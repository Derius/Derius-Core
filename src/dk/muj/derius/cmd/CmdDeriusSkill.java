package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

// Shows this skills with it's description and the ability with level specific information.
public class CmdDeriusSkill extends DeriusCommand
{
	public CmdDeriusSkill()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("player", "you");
		this.addOptionalArg("level", "your level");
		
		this.setDesc("Shows a skill, it's description, the passive and active abilities and level specific information");
		
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}
	
	@Override
	public void perform()
	{
		//Args
		// skillname args
		Skill skill = this.arg (0, ARSkill.get());
		if (skill == null) return;
		
		// player args
		MPlayer mplayer = this.arg(1, ARMPlayer.getAny(), msender);
		if (mplayer == null) return;
		
		// level args
		int level = this.arg(2, ARInteger.get(), msender.getLvl(skill.getId()));
		if (level == 0) return;
		
		List<String> messagelines = new ArrayList<String>();
		
		String level = "Level is"
		
		messagelines.add(skill.toString());
		messagelines.add("------------");
		messagelines.add(skill.getdesc());
		messagelines.add("<i>Passive abilities");
		
		messagelines.add("<i>Passive abilities");
		messagelines.addAll(skill.getPassiveAbilityDescriptions());
		messagelines.add("<i>Active abilities");
		messagelines.addAll(skill.getActiveAbilityDescriptions());
		messagelines.add("<i>Active abilities");

		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSkill;
    }
}
