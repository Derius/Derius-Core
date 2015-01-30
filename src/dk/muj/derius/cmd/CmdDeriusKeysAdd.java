package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.AbilityColl;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;

public class CmdDeriusKeysAdd extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysAdd()
	{
		super.addRequiredArg("keys");
		super.addRequiredArg("ability Id");
		
		super.addRequirements(ReqHasPerm.get(Perm.KEYS_ADD.node));
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
		if (id == null)	return;
		
		// Already a chat key?
		if (msender.isAlreadyChatKey(key))
		{
			sendMessage(Txt.parse(MLang.get().keyAlreadyHas, key));
			return;
		}
		
		Ability ability = AbilityColl.get().get(id);
		if (ability == null)
		{
			sendMessage(Txt.parse(MLang.get().abilityInvalidId, id));
			return;
		}
		
		msender.addChatKey(key, ability);
		sendMessage(Txt.parse(MLang.get().keyAddSuccess, key, ability.toString()));
	}

	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysAdd;
    }
	
}
