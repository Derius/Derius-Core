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
		super.addRequiredArg("player");
		
		super.addAliases("player");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() 
	{
		MPlayer target = super.arg(0, ARMPlayer.getAny());
		if(null == target)	return;
		
		List<String> messages = new ArrayList<String>();
		
		Set<String> specialised = target.getRawSpecialisedData();
		
		Map<String,Long> exp = target.getRawExpData();

		messages.add(Txt.titleize("Debug info about "+target.getDisplayName(msender)));
		messages.add(Txt.parse("<i>Current millis: <lime>" + System.currentTimeMillis()));
		messages.add(Txt.parse("<i>Prepared tool: <lime>" + target.getPreparedTool()));
		messages.add(Txt.parse("<red>Specialised:<art> "+Txt.implodeCommaAnd( specialised, ",", "&")));
		messages.add(Txt.parse("<red>Exp:"));
		
		for (String i : exp.keySet())
		{
			messages.add(Txt.parse("<i>" + i + "  <lime>" + exp.get(i)));
		}
		
		sendMessage(messages);
	}
	
}
