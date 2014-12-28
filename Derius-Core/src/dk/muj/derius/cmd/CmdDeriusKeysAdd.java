package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;

public class CmdDeriusKeysAdd extends DeriusCommand
{
	public CmdDeriusKeysAdd()
	{
		this.addRequiredArg("keys");
		this.addRequiredArg("ability Id");
		
		this.setDesc("Adds a new key to your list of activation keys.");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_ADD.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		MPlayer mplayer = this.msender;
		
		String key = this.arg(0).toLowerCase();
		
		if(mplayer.isAlreadyChatKey(key))
		{
			mplayer.msg(Txt.parse(MConf.get().msgKeyAddIsAlready, key));
			return;
		}
		
		Integer id = this.arg(1, ARInteger.get());
		Ability ability = Ability.GetAbilityById(id);
		if( (ability == null))
		{
			mplayer.msg(Txt.parse(MConf.get().msgKeyAddInvalid));
			return;
		}
		
		mplayer.addChatKeys(key, ability);
		
		mplayer.msg(Txt.parse(MConf.get().msgKeyAddSuccess, key, ability.toString()));
	}
	
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysAdd;
    }
}
