package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusSpecialise extends DeriusCommand
{
	public CmdDeriusSpLearn innerCmdDeriusSpLearn = new CmdDeriusSpLearn();
	public CmdDeriusSpUnlearn innerCmdDeriusSpUnlearn = new CmdDeriusSpUnlearn();
	public CmdDeriusSpInfo innerCmdDeriusSpInfo = new CmdDeriusSpInfo();
	public CmdDeriusSpList innerCmdDeriusSpList = new CmdDeriusSpList();
	
	public CmdDeriusSpecialise()
	{
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(innerCmdDeriusSpLearn);
		this.addSubCommand(innerCmdDeriusSpUnlearn);
		this.addSubCommand(innerCmdDeriusSpInfo);
		this.addSubCommand(innerCmdDeriusSpList);
		
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION.node));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpecialise;
    }
}
