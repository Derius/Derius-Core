package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

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
	public void perform()
	{
		List<String> messages = new ArrayList<String>();
		
		// Args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);
		if (mplayer == null) return;
		
		if (mplayer != msender && !Perm.SPECIALISATION_LIST_OTHER.has(sender, true)) return;

		messages.add(Txt.titleize(Txt.parse("%s's <i>specilisations", mplayer.getDisplayName(msender))));
		
		for (Skill skill : mplayer.getSpecialisedSkills())
		{
			messages.add(Txt.parse("%s: %s", skill.getDisplayName(msender), mplayer.getLvlStatus(skill).toString()));
		}
		
		// Send Message
		sendMessage(messages);
	}

}
