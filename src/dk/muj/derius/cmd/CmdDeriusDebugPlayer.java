package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MPlayer;

public class CmdDeriusDebugPlayer extends DeriusCommand
{

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusDebugPlayer()
	{
		this.addRequiredArg("player");
		
		this.addAliases("player");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public void perform() 
	{
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny());
		if (mplayer == null) return;
		
		List<String> messages = new ArrayList<String>();
		
		Set<String> specialised = mplayer.getRawSpecialisedData();
		
		Map<String,Long> exp = mplayer.getRawExpData();

		messages.add(Txt.titleize(Txt.parse("Debug info about %s", mplayer.getDisplayName(msender))));
		messages.add(Txt.parse("<i>Current millis: <lime>%s", System.currentTimeMillis()));
		messages.add(Txt.parse("<i>Prepared tool: <lime>%s", mplayer.getPreparedTool()));
		messages.add(Txt.parse("<red>Specialised: <art>%S", Txt.implodeCommaAnd( specialised, ",", "&")));
		messages.add(Txt.parse("<red>Exp:"));
		
		for (String skill : exp.keySet())
		{
			messages.add(Txt.parse("<i>, <lime>%s", skill, exp.get(skill)));
		}
		
		sendMessage(messages);
		
		return;
	}
	
}
