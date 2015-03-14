package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusKeys extends DeriusCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdDeriusKeysAdd innerCmdDeriusKeysAdd = new CmdDeriusKeysAdd() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusKeysAdd; } };
	public CmdDeriusKeysList innerCmdDeriusKeysList = new CmdDeriusKeysList() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusKeysList; } };
	public CmdDeriusKeysRemove innerCmdDeriusKeysRemove = new CmdDeriusKeysRemove() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusKeysRemove; } };
	public CmdDeriusKeysAbilityid innerCmdDeriusKeysAbilityid = new CmdDeriusKeysAbilityid() { @Override public List<String> getAliases() { return MConf.get().innerAliasesDeriusKeyAbilityId; } };
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeys()
	{
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(this.innerCmdDeriusKeysAdd);
		this.addSubCommand(this.innerCmdDeriusKeysList);
		this.addSubCommand(this.innerCmdDeriusKeysRemove);
		this.addSubCommand(this.innerCmdDeriusKeysAbilityid);
		
		this.addRequirements(ReqHasPerm.get(DeriusPerm.KEYS.getNode()));
	}
	
}
