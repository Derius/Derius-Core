package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.pager.Stringifier;
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
		super.addOptionalArg("player", "yourself");
		super.addOptionalArg("page", "1");
		
		super.addRequirements(ReqHasPerm.get(Perm.KEYS_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);	
		if(mplayer == null) return;
		Integer pageHumanBased = this.arg(1, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Pager
		String title = mplayer == msender ? Txt.parse("<i>Your list of Keys") : Txt.parse("%s's <i>list of Keys", mplayer.getDisplayName(msender));
		final List<String> keysToAbility = mplayer.chatKeysToString();
		final PagerSimple<String> pager = new PagerSimple<String>(keysToAbility, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, title, new Stringifier<String>() {
			@Override
			public String toString(String string)
			{
				return string;
			}
		});
		
		// Send Message
		sendMessage(messages);
	}
	
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeysList;
    }
	
}
