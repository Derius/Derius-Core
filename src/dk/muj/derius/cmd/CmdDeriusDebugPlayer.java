package dk.muj.derius.cmd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MPlayer;

public class CmdDeriusDebugPlayer extends DeriusCommand
{

	public CmdDeriusDebugPlayer()
	{
		super.setDesc("info about player");
		
		super.addRequiredArg("player");
		
		super.addAliases("player");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void perform() 
	{
		MPlayer target = super.arg(0, ARMPlayer.getAny());
		if(null == target)	return;
		
		List<String> msgLines = new ArrayList<String>();
		
		
		Field field = null;
		Collection<? extends Object> value = null;
		//Reflect my way into the data
		try
		{
			field = target.getClass().getDeclaredField("specialised");
			field.setAccessible(true);
			value = (Collection<? extends Object>) field.get(target);
		}
		catch (NoSuchFieldException e)		{	e.printStackTrace();	}
		catch (SecurityException e)			{	e.printStackTrace();	}
		catch (IllegalArgumentException e)	{	e.printStackTrace();	}
		catch (IllegalAccessException e)	{	e.printStackTrace();	}
		
		Field field2 = null;
		HashMap<Integer,Long> value2 = null;
		//Reflect my way into the data
		try
		{
			field2 = target.getClass().getDeclaredField("exp");
			field2.setAccessible(true);
			value2 = (HashMap<Integer,Long>) field2.get(target);
		}
		catch (NoSuchFieldException e)		{	e.printStackTrace();	}
		catch (SecurityException e)			{	e.printStackTrace();	}
		catch (IllegalArgumentException e)	{	e.printStackTrace();	}
		catch (IllegalAccessException e)	{	e.printStackTrace();	}

		
		msgLines.add(Txt.titleize("Debug info about "+target.getDisplayName(msender)));
		msgLines.add(Txt.parse("<i>Current millis: <lime>" + System.currentTimeMillis()));
		msgLines.add(Txt.parse("<i>Specialised millis: <lime>" + System.currentTimeMillis()));
		
		msgLines.add(Txt.parse("<red>Specialised:<art> "+Txt.implodeCommaAnd( value, ",", "&")));
		msgLines.add(Txt.parse("<red>Exp:"));
		for(Integer i: value2.keySet())
			msgLines.add(Txt.parse("<i>" + i + "  <lime>" + value2.get(i)));
		
		msender.msg(msgLines);
	}
}
