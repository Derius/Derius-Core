package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusClear extends DeriusCommand
{
	public CmdDeriusClearPlayer innerCmdDeriusClearPlayer = new CmdDeriusClearPlayer();
	public CmdDeriusClearSkill innerCmdDeriusClearSkill = new CmdDeriusClearSkill();
	public CmdDeriusClearAll innerCmdDeriusClearAll = new CmdDeriusClearAll();
	
	public CmdDeriusClear()
	{
		super.addSubCommand(HelpCommand.get());
		super.addSubCommand(this.innerCmdDeriusClearPlayer);
		super.addSubCommand(this.innerCmdDeriusClearSkill);
		super.addSubCommand(this.innerCmdDeriusClearAll);
		
		super.setVisibilityMode(VisibilityMode.INVISIBLE);
		super.addRequirements(ReqHasPerm.get(Perm.CLEAR.node));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusClear;
    }
}
