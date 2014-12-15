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
	//Initialize all the sub-commands that belong to it.
	public VersionCommand innerCmdDeriusVersion = new VersionCommand(Derius.get(), Perm.VERSION.node, "v", "version");
	public CmdDeriusSkill innerCmdDeriusSkill = new CmdDeriusSkill();
	public CmdDeriusList innerCmdDeriusList = new CmdDeriusList();
	public CmdDeriusSpecialise innerCmdDeriusSpecialise = new CmdDeriusSpecialise();
	public CmdDeriusInspect innerCmdDeriusInspect = new CmdDeriusInspect();	
	public CmdDeriusDebug innerCmdDeriusDebug = new CmdDeriusDebug();
	
	// Constructor
	public CmdDerius()
	{
		super.addSubCommand(HelpCommand.get());
		super.addSubCommand(this.innerCmdDeriusSkill);
		super.addSubCommand(this.innerCmdDeriusList);
		super.addSubCommand(this.innerCmdDeriusInspect);
		super.addSubCommand(this.innerCmdDeriusSpecialise);
		super.addSubCommand(this.innerCmdDeriusDebug);
		super.addSubCommand(this.innerCmdDeriusVersion);
		
		super.addRequirements(ReqHasPerm.get(Perm.BASECOMMAND.node));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().outerAliasesDerius;
    }
}
