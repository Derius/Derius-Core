package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.ArgSetting;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.AR;

import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.entity.mplayer.MPlayerColl;


public class DeriusCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected DPlayer dsender;
	public DPlayer getDSender() { return dsender; }
	
	private int idx = 0;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void fixSenderVars() 
	{
		this.dsender = MPlayerColl.get().get(sender, true);
		this.idx = 0;
	}
	
	@Override
	public void unsetSenderVars()
	{
		this.dsender = null;
		this.idx = 0;
	}
	
	// -------------------------------------------- //
	// LE NICE ARG
	// -------------------------------------------- //
	
	// argIsSet
	
	public boolean argIsSet()
	{
		return this.argIsSet(idx);
	}
	
	// arg
	
	public String arg()
	{
		return this.arg(idx++);
	}
	
	public <T> T arg(AR<T> argReader) throws MassiveException
	{
		return this.arg(idx++, argReader);
	}
	
	public <T> T arg(AR<T> argReader, T defaultNotSet) throws MassiveException
	{
		return this.arg(idx++, argReader, defaultNotSet);
	}
	
	// argConcatFrom
	
	public String argConcatFrom()
	{
		return this.argConcatFrom(idx++);
	}
	
	public <T> T argConcatFrom(AR<T> argReader) throws MassiveException
	{
		return this.argConcatFrom(idx++, argReader);
	}
	
	public <T> T argConcatFrom(AR<T> argReader, T defaultNotSet) throws MassiveException
	{
		return this.argConcatFrom(idx++, argReader, defaultNotSet);
	}
	
	public static ArgSetting<DPlayer> getPlayerYou()
	{
		return new ArgSetting<DPlayer>(ARDPlayer.getAny(), "player", "you");
	}
	
}
