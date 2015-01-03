package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		super.setDesc("info about player");
		
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
		
		List<String> msgLines = new ArrayList<String>();
		
		
		List<Integer> specialised = target.getRawSpecialisedData();
		
		Map<Integer,Long> exp = target.getRawExpData();

		msgLines.add(Txt.titleize("Debug info about "+target.getDisplayName(msender)));
		msgLines.add(Txt.parse("<i>Current millis: <lime>" + System.currentTimeMillis()));
		//msgLines.add(Txt.parse("<i>Specialised millis: <lime>" + target.getSpecialisationChangeMillis()));
		//Srew this, it didn't work it makes no sense and I'm confused
		msgLines.add(Txt.parse("<i>Prepared tool: <lime>" + target.getPreparedTool()));
		
		msgLines.add(Txt.parse("<red>Specialised:<art> "+Txt.implodeCommaAnd( specialised, ",", "&")));
		msgLines.add(Txt.parse("<red>Exp:"));
		for(Integer i: exp.keySet())
			msgLines.add(Txt.parse("<i>" + i + "  <lime>" + exp.get(i)));
		
		msender.msg(msgLines);
	}
	
}
