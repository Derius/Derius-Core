package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;


public class DeriusCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public MPlayer msender;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void fixSenderVars() 
	{
		this.msender = MPlayerColl.get().get(sender, true);
	}
	
	@Override
	public void unsetSenderVars()
	{
		this.msender = null;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	// In the constructor we call the Visibility Mode of the Commands. Visible means, it is visible to all players.
	public DeriusCommand()
	{
		this.setVisibilityMode(VisibilityMode.VISIBLE);
	}
	
}
