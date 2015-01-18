package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;

public class CmdDeriusKeysRemove extends DeriusCommand
{
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysRemove()
	{
		this.addRequiredArg("key");
		
		this.setDesc("Removes a key from your list of activation keys.");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_REMOVE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		MPlayer mplayer = this.msender;
		
		String key = this.arg(0).toLowerCase();
		
		if ( ! mplayer.isAlreadyChatKey(key))
		{
			mplayer.msg(Txt.parse(MLang.get().keysAlreadyHas));
			return;
		}
		
		mplayer.removeChatKeys(key);
		
		mplayer.msg(Txt.parse(MLang.get().keysRemoveSuccess, key));
	}
	
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysRemove;
    }
	
}
