package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;

public class CmdDeriusKeysList extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysList()
	{
		this.addOptionalArg("player", "yourself");
		this.addOptionalArg("page", "1");
		this.setDesc("Shows your list of activation keys.");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		// Args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);	
		if(mplayer == null)
			return;
		
		int page = this.arg(1, ARInteger.get(), 1);
		
		msgLines.addAll(mplayer.chatKeysToString());
		msender.msg(Txt.getPage(msgLines, page, "List of Keys"));
	}
	
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysList;
    }
	
}
