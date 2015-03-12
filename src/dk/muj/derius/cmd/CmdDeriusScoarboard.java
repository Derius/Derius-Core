package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusScoarboard extends DeriusCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdDeriusScShow innerCmdDeriusSeScoreboard = new CmdDeriusScShow() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusScShow; } };
	public CmdDeriusScKeep innerCmdDeriusScKeep = new CmdDeriusScKeep() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusScKeep; } };
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusScoarboard()
	{
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(this.innerCmdDeriusSeScoreboard);
		this.addSubCommand(this.innerCmdDeriusScKeep);
		
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SCOREBOARD.getNode()));
	}
}
