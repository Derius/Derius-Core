package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

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
		super.addSubCommand(HelpCommand.get());
		super.addSubCommand(innerCmdDeriusDebugInfo);
		super.addSubCommand(innerCmdDeriusDebugTitle);
		super.addSubCommand(innerCmdDeriusDebugPlayer);
		
		super.setVisibilityMode(VisibilityMode.SECRET);
		super.addRequirements(ReqHasPerm.get(Perm.DEBUG.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusDebug;
    }
	
}
