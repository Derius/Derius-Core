package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.skill.Skill;

public class CmdDeriusClearSkill extends DeriusCommand
{
	public CmdDeriusClearSkill()
	{
		this.addRequiredArg("skill");
		this.addOptionalArg("force it", "no");
		
		this.setDesc("Resets all the players level data of this skill.");
		
		super.addRequirements(ReqHasPerm.get(Perm.CLEAR_SKILL.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String force = this.arg(1);
		String forceYes = "Yes, I want to force this";
		if(!force.equals(forceYes))
			return;
		
		Skill skill = this.arg(1,ARSkill.get());
		
		for(MPlayer target: MPlayerColl.get().getAll())
		{
			target.cleanNoCheck(skill.getId());
		}
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusClearSkill;
    }
}
