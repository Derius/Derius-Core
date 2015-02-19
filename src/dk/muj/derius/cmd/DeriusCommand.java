package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommand;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.entity.mplayer.MPlayerColl;


public class DeriusCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected DPlayer dsender;
	public DPlayer getDSender() { return dsender; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void fixSenderVars() 
	{
		this.dsender = MPlayerColl.get().get(sender, true);
	}
	
	@Override
	public void unsetSenderVars()
	{
		this.dsender = null;
	}
	
}
