package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.VersionCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Derius;
import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

// This is the Basecommand for Derius
public class CmdDerius extends DeriusCommand
{
	//Initialize all the commands that belong to it.
	public VersionCommand cmdDeriusVersion = new VersionCommand(Derius.get(), Perm.VERSION.node, "v", "version");
	
	// Constructor
	public CmdDerius()
	{
		
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(cmdDeriusVersion);
		
		this.addRequirements(ReqHasPerm.get(Perm.BASECOMMAND.node));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().outerAliasesDerius;
    }
}
