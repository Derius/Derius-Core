package dk.muj.derius.cmd;

import java.util.List;

import dk.muj.derius.cmd.arg.ARMsgType;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MsgType;

public class CmdDeriusSeMsgChange extends DeriusCommand
{
	public CmdDeriusSeMsgChange()
	{
		this.addRequiredArg("chat/title/scoreboard");
		
		this.setDesc("Changes the way some messages get displayed to you.");
	}
	
	//Scoreboard is actually not implemented yet, but yey scoreboards!
	@Override
	public void perform()
	{
		MsgType type = this.arg(0, ARMsgType.get());
		if(type == null)
		{
			msender.msg(MConf.get().msgSettingsMsgChangeInvalid, type);
			return;
		}
		
		msender.setMsgType(type);
		msender.msg(MConf.get().msgSettingsMsgChangeSuccess, type);
		
	}
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSeMsgSet;
    }
}
