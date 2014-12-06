package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

// Shows you all the learned skills and the level for said player/yourself.
public class CmdDeriusInspect extends CmdDerius
{
	public CmdDeriusInspect()
	{
		this.addOptionalArg("player", "you");
		
		this.addRequirements(ReqHasPerm.get(Perm.INSPECT.node));
	}
	
	@Override
	public void perform()
	{
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusInspect;
    }
}
