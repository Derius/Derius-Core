package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.entity.MPlayerColl;


public class DeriusCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public DPlayer dsender;
	
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
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	// In the constructor we call the Visibility Mode of the Commands. Visible means, it is visible to all players.
	public DeriusCommand()
	{
		this.setVisibilityMode(VisibilityMode.VISIBLE);
	}
	
}
