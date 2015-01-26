package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;

public class CmdDeriusKeysRemove extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysRemove()
	{
		super.addRequiredArg("key/all");
		
		super.addRequirements(ReqHasPerm.get(Perm.KEYS_REMOVE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		String key = this.arg(0).toLowerCase();
		
		// All case?
		if (key.equals("all"))
		{
			msender.clearChatKeys();
			sendMessage(Txt.parse(MLang.get().keysClearSuccess));
			return;
		}
		
		// Isn't chat key
		if ( ! msender.isAlreadyChatKey(key))
		{
			sendMessage(Txt.parse(MLang.get().keyHanst, key));
			return;
		}
		
		msender.removeChatKey(key);
		sendMessage(Txt.parse(MLang.get().keyRemoveSuccess, key));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysRemove;
    }
	
}
