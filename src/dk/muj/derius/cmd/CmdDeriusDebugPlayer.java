package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.entity.mplayer.MPlayer;

public class CmdDeriusDebugPlayer extends DeriusCommand
{

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusDebugPlayer()
	{
		// Args
		this.addRequiredArg("player");
		
		// Aliases
		this.addAliases("player");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException 
	{
		// Args
		MPlayer mplayer = (MPlayer) this.arg(0, ARDPlayer.getAny());
		
		List<String> messages = new ArrayList<String>();
		
		@SuppressWarnings("deprecation")
		Set<String> specialised = mplayer.getRawSpecialisedData();
		
		@SuppressWarnings("deprecation")
		Map<String,Long> exp = mplayer.getRawExpData();

		messages.add(Txt.titleize(Txt.parse("Debug info about %s", mplayer.getDisplayName(dsender))));
		messages.add(Txt.parse("<i>Current millis: <lime>%s", System.currentTimeMillis()));
		messages.add(Txt.parse("<i>Prepared tool: <lime>%s", mplayer.getPreparedTool()));
		messages.add(Txt.parse("<red>Specialised: <art>%S", Txt.implodeCommaAnd( specialised, ",", "&")));
		messages.add(Txt.parse("<red>Exp:"));
		
		for (String skill : exp.keySet())
		{
			messages.add(Txt.parse("<i>, <lime>%s", skill, exp.get(skill)));
		}
		
		this.sendMessage(messages);
		
		return;
	}
	
}
