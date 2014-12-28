package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;

public class CmdDeriusKeysClear extends DeriusCommand
{
	public CmdDeriusKeysClear()
	{	
		this.addOptionalArg("force", "false");
		this.setDesc("Removes all of your entries in your list of activation keys.");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_CLEAR.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		MPlayer mplayer = msender;
		
		Boolean force = this.arg(0, ARBoolean.get(), false);
		
		if (force)
		{
			mplayer.clearChatKeys();
			mplayer.msg(Txt.parse(MConf.get().msgKeyClearSuccess));
		}
		else
		{
			mplayer.msg(Txt.parse(MConf.get().msgKeyClearWarning));
		}
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysClear;
    }
}
