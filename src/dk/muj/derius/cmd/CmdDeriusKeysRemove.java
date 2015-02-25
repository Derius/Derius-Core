package dk.muj.derius.cmd;

import java.util.Set;

import com.massivecraft.massivecore.MassiveException;
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
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_REMOVE.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Set<String> keys = this.argConcatFrom(0, ARSet.get(ARString.get(), true));
		
		// All case?
		if (keys.contains("all"))
		{
			dsender.clearChatKeys();
			msg(MLang.get().keysClearSuccess);
			return;
		}
		
		for (String key : keys)
		{
			// Isn't chat key
			if ( ! dsender.isAlreadyChatKey(key))
			{
				msg(MLang.get().keyHanst, key);
				return;
			}
			
			dsender.removeChatKey(key);
			sendMessage(Txt.parse(MLang.get().keyRemoveSuccess, key));
		}
		
		return;
	}
	
}
