package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.cmd.arg.ARDPlayer;

public class CmdDeriusKeysList extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysList()
	{
		this.addOptionalArg("player", "yourself");
		this.addOptionalArg("page", "1");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_LIST.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		DPlayer mplayer = this.arg(0, ARDPlayer.getAny(), dsender);	
		Integer pageHumanBased = this.arg(1, ARInteger.get(), 1);
		
		// Create Pager
		String title = (mplayer == dsender) ? Txt.parse("<i>Your list of Keys") : Txt.parse("%s's <i>list of Keys", mplayer.getDisplayName(dsender));
		final List<String> keysToAbility = mplayer.chatKeysToString();
		final PagerSimple<String> pager = new PagerSimple<String>(keysToAbility, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, title, new Stringifier<String>() {
			@Override
			public String toString(String string, int index)
			{
				return string;
			}
		});
		
		// Send Message
		sendMessage(messages);
		
		return;
	}
	
}
