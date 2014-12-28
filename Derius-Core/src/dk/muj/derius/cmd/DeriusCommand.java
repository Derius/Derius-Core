package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;

import dk.muj.derius.entity.MPlayer;


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
		this.msender = MPlayer.get(sender);
	}
	
	@Override
	public void unsetSenderVars()
	{
		this.msender = null;
	}
	
	// In the constructor we call the Visibility Mode of the Commands. Visible means, it is visible to all players.
	public DeriusCommand()
	{
		this.setVisibilityMode(VisibilityMode.VISIBLE);
	}
}
