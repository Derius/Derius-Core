package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusSpecialise extends DeriusCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdDeriusSpLearn innerCmdDeriusSpLearn = new CmdDeriusSpLearn() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusSpLearn; } };
	public CmdDeriusSpUnlearn innerCmdDeriusSpUnlearn = new CmdDeriusSpUnlearn() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusSpUnlearn; } };
	public CmdDeriusSpInfo innerCmdDeriusSpInfo = new CmdDeriusSpInfo() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusSpInfo; } };
	public CmdDeriusSpList innerCmdDeriusSpList = new CmdDeriusSpList() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusSpList; } };
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSpecialise()
	{
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(innerCmdDeriusSpLearn);
		this.addSubCommand(innerCmdDeriusSpUnlearn);
		this.addSubCommand(innerCmdDeriusSpInfo);
		this.addSubCommand(innerCmdDeriusSpList);
		
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION.getNode()));
	}
	
}
