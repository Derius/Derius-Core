package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
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
		
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LIST.getNode()));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		DPlayer mplayer = this.arg(0, ARDPlayer.getAny(), dsender);
		
		List<String> msgs = new ArrayList<String>();
		
		if (mplayer != dsender && !Perm.SPECIALISATION_LIST_OTHER.has(sender, true)) return;

		msgs.add(Txt.titleize(String.format("%s's <i>Specialisations", mplayer.getDisplayName(dsender))));
		
		for (Skill skill : mplayer.getSpecialisedSkills())
		{
			msgs.add(String.format("%s: %s", skill.getDisplayName(dsender), mplayer.getLvlStatus(skill).toString()));
		}
		
		// Send Message
		msg(msgs);
		
		return;
	}

}
