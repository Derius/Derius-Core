package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.cmd.arg.ARDPlayer;

public class CmdDeriusSpList  extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpList()
	{
		this.addOptionalArg("player", "you");
		
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LIST.node));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveCommandException
	{
		List<String> messages = new ArrayList<String>();
		
		// Args
		DPlayer mplayer = this.arg(0, ARDPlayer.getAny(), dsender);
		
		if (mplayer != dsender && !Perm.SPECIALISATION_LIST_OTHER.has(sender, true)) return;

		messages.add(Txt.titleize(Txt.parse("%s's <i>Specialisations", mplayer.getDisplayName(dsender))));
		
		for (Skill skill : mplayer.getSpecialisedSkills())
		{
			messages.add(Txt.parse("%s: %s", skill.getDisplayName(dsender), mplayer.getLvlStatus(skill).toString()));
		}
		
		// Send Message
		sendMessage(messages);
		
		return;
	}

}
