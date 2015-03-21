package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.VersionCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.DeriusPerm;
import dk.muj.derius.entity.MConf;

public class CmdDerius extends DeriusCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public VersionCommand innerCmdDeriusVersion = new VersionCommand(DeriusPlugin.get(), DeriusPerm.VERSION.getNode(), "v", "version");
	public CmdDeriusSkill innerCmdSkill = new CmdDeriusSkill() { public List<String> getAliases() { return MConf.get().innerAliasesSkill; } };
	public CmdDeriusSkills innerCmdSkills = new CmdDeriusSkills() { public List<String>  getAliases() { return MConf.get().innerAliasesSkills; } };
	public CmdDeriusList innerCmdDeriusList = new CmdDeriusList() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusList; } };
	public CmdDeriusSpecialise innerCmdDeriusSpecialise = new CmdDeriusSpecialise() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusSpecialise; } };
	public CmdDeriusInspect innerCmdDeriusInspect = new CmdDeriusInspect() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusInspect; } };
	public CmdDeriusDebug innerCmdDeriusDebug = new CmdDeriusDebug() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusDebug; } };
	public CmdDeriusClean innerCmdDeriusClean = new  CmdDeriusClean() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusClean; } };
	public CmdDeriusSetStamina innerCmdDeriusSetStamina = new  CmdDeriusSetStamina() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusSetStamina; } };
	public CmdDeriusScoarboard innerCmdDeriusScoarboard = new  CmdDeriusScoarboard() { public List<String> getAliases() { return MConf.get().innerAliasesDeriusScoreboard; } };

	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDerius()
	{
		// Subcommands
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(this.innerCmdSkill);
		// this.addSubCommand(this.innerCmdSkills);
		this.addSubCommand(this.innerCmdDeriusList);
		this.addSubCommand(this.innerCmdDeriusInspect);
		this.addSubCommand(this.innerCmdDeriusSpecialise);
		this.addSubCommand(this.innerCmdDeriusClean);
		this.addSubCommand(this.innerCmdDeriusScoarboard);
		this.addSubCommand(this.innerCmdDeriusDebug);
		this.addSubCommand(this.innerCmdDeriusSetStamina);
		this.addSubCommand(this.innerCmdDeriusVersion);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.BASECOMMAND.getNode()));
	}
	
}
