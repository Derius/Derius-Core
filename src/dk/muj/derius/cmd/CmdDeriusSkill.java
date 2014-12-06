package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

// Shows this skills with it's description and the ability with level specific information.
public class CmdDeriusSkill extends CmdDerius
{
	public CmdDeriusSkill()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("player", "you");
		this.addOptionalArg("level", "your level");
		
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}
	
	@Override
	public void perform()
	{
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSkill;
    }
}
