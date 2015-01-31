package dk.muj.derius.cmd;

import java.util.Set;

import com.massivecraft.massivecore.cmd.arg.ARSet;
import com.massivecraft.massivecore.cmd.arg.ARString;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MLang;

public class CmdDeriusKeysRemove extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysRemove()
	{
		this.addRequiredArg("keys/all");
		
		this.setErrorOnToManyArgs(false);
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_REMOVE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		Set<String> keys = this.argConcatFrom(0, ARSet.get(ARString.get(), true));
		
		// All case?
		if (keys.contains("all"))
		{
			msender.clearChatKeys();
			sendMessage(Txt.parse(MLang.get().keysClearSuccess));
			return;
		}
		
		for (String key : keys)
		{
			// Isn't chat key
			if ( ! msender.isAlreadyChatKey(key))
			{
				sendMessage(Txt.parse(MLang.get().keyHanst, key));
				return;
			}
			
			msender.removeChatKey(key);
			sendMessage(Txt.parse(MLang.get().keyRemoveSuccess, key));
		}
		
	}
	
}
