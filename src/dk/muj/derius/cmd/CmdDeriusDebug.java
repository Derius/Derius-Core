package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;

public class CmdDeriusDebug extends DeriusCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdDeriusDebugInfo innerCmdDeriusDebugInfo = new CmdDeriusDebugInfo();
	public CmdDeriusDebugTitle innerCmdDeriusDebugTitle = new CmdDeriusDebugTitle();
	public CmdDeriusDebugPlayer innerCmdDeriusDebugPlayer = new CmdDeriusDebugPlayer();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusDebug()
	{
		// Subcommands
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(innerCmdDeriusDebugInfo);
		this.addSubCommand(innerCmdDeriusDebugTitle);
		this.addSubCommand(innerCmdDeriusDebugPlayer);
		
		// Visibility
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.DEBUG.getNode()));
	}
	
}
