package dk.muj.derius.cmd;

import java.util.Set;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARSet;
import com.massivecraft.massivecore.cmd.arg.ARString;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.api.config.DLang;

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
			msg(DLang.get().getKeysClearSuccess());
			return;
		}
		
		for (String key : keys)
		{
			// Isn't chat key
			if ( ! dsender.isAlreadyChatKey(key))
			{
				msg(DLang.get().getKeyHasnt().replaceAll("{key}", key));
				return;
			}
			
			dsender.removeChatKey(key);
			msg(DLang.get().getKeyRemoveSuccess().replaceAll("{key}", key));
		}
		
		return;
	}
	
}
