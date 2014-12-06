package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusAbility extends CmdDerius
{
	public CmdDeriusAbility()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("Player", "you");
		
		this.addRequirements(ReqHasPerm.get(Perm.ABILITY.node));
	}
	
	@Override
	public void perform()
	{
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusAbility;
    }
}
