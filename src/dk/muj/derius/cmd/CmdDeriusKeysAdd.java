package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.entity.MLang;

public class CmdDeriusKeysAdd extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysAdd()
	{
		this.addRequiredArg("key");
		this.addRequiredArg("ability Id");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_ADD.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		String key = this.arg(0).toLowerCase();
		if (key == null) return;
		String id = this.arg(1);
		if (id == null)	return;
		
		// Already a chat key?
		if (dsender.isAlreadyChatKey(key))
		{
			sendMessage(Txt.parse(MLang.get().keyAlreadyHas, key));
			return;
		}
		
		Ability ability = DeriusAPI.getAbility(id);
		if (ability == null)
		{
			sendMessage(Txt.parse(MLang.get().abilityInvalidId, id));
			return;
		}
		
		dsender.addChatKey(key, ability);
		sendMessage(Txt.parse(MLang.get().keyAddSuccess, key, ability.toString()));
		
		return;
	}
	
}
