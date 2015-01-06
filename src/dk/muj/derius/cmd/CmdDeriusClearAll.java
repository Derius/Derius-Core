package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.skill.Skill;

public class CmdDeriusClearAll extends DeriusCommand
{
	public CmdDeriusClearAll()
	{
		this.addRequiredArg("all");
		this.addOptionalArg("force it", "no");
		
		this.setDesc("Dangerous! Resets ALL of the level data for all players for all skills!");
		
		super.addRequirements(ReqHasPerm.get(Perm.CLEAR_ALL.node));
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
		
		for (Skill skill: Skill.getAllSkills())
		{
			for(MPlayer target: MPlayerColl.get().getAll())
			{
				target.cleanNoCheck(skill.getId());
			}
		}
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusClearAll;
    }
}
