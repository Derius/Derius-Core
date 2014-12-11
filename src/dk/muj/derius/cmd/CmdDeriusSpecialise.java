package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusSpecialise extends DeriusCommand
{
	CmdDeriusSpLearn innerCmdSpLearn = new CmdDeriusSpLearn();
	CmdDeriusSpUnlearn innerCmdSpUnlearn = new CmdDeriusSpUnlearn();
	CmdDeriusSpInfo innerCmdSpInfo = new CmdDeriusSpInfo();
	
	public CmdDeriusSpecialise()
	{
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(innerCmdSpLearn);
		this.addSubCommand(innerCmdSpUnlearn);
		this.addSubCommand(innerCmdSpInfo);
		
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION.node));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpecialise;
    }
}
