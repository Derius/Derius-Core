package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusKeys extends DeriusCommand
{
	public CmdDeriusKeysAdd innerCmdDeriusKeysAdd= new CmdDeriusKeysAdd();
	public CmdDeriusKeysRemove innerCmdDeriusKeysRemove= new CmdDeriusKeysRemove();
	public CmdDeriusKeysList innerCmdDeriusKeysList= new CmdDeriusKeysList();
	public CmdDeriusKeysClear innerCmdDeriusKeysClear= new CmdDeriusKeysClear();
	public CmdDeriusKeysAbilityid innerCmdDeriusKeysAbilityid= new CmdDeriusKeysAbilityid();
	
	
	public CmdDeriusKeys()
	{
		super.addSubCommand(HelpCommand.get());
		super.addSubCommand(this.innerCmdDeriusKeysAdd);
		super.addSubCommand(this.innerCmdDeriusKeysRemove);
		super.addSubCommand(this.innerCmdDeriusKeysList);
		super.addSubCommand(this.innerCmdDeriusKeysClear);
		super.addSubCommand(this.innerCmdDeriusKeysAbilityid);
		
		super.setVisibilityMode(VisibilityMode.VISIBLE);
		super.addRequirements(ReqHasPerm.get(Perm.KEYS.node));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeys;
    }
}
