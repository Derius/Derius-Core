package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

// Shows you a list of all the available skills (color coded for state) and a short description of them.
// Color code of skill: grey = locked, you can't learn it | red = Haven't started to learn, on lvl 0 | green = You have started learning it and are on some level
public class CmdDeriusList extends CmdDerius
{
	public CmdDeriusList()
	{
		this.addOptionalArg("player", "you");
		
		this.addRequirements(ReqHasPerm.get(Perm.LIST.node));
	}
	
	@Override
	public void perform()
	{
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusList;
    }
}
