package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.config.DLang;

public class CmdDeriusKeysAdd extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysAdd()
	{
		this.addRequiredArg("key");
		this.addRequiredArg("ability Id");
		
		this.addRequirements(ReqHasPerm.get(DeriusPerm.KEYS_ADD.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		String key = this.arg(0).toLowerCase();
		String id = this.arg(1);
		
		// Already a chat key?
		if (dsender.isAlreadyChatKey(key))
		{
			sendMessage(Txt.parse(DLang.get().getKeyAlreadyHas().replace("{key}", key)));
			return;
		}
		
		Ability ability = DeriusAPI.getAbility(id);
		if (ability == null)
		{
			sendMessage(Txt.parse(DLang.get().getAbilityNoSuchId().replace("{id}", id)));
			return;
		}
		
		dsender.addChatKey(key, ability);
		sendMessage(Txt.parse(DLang.get().getKeyAddSuccess().replace("{key}", key).replace("{ability}", ability.getDisplayName(dsender))));
		
		return;
	}
	
}
