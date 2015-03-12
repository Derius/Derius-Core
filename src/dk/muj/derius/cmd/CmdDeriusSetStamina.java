package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.arg.ARDouble;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.entity.mplayer.MPlayer;

public class CmdDeriusSetStamina extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSetStamina()
	{
		// Args
		this.addRequiredArg("stamina");
		this.addOptionalArg("player", "you");
		
		// Visibility
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SETSTAMINA.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Double stamina = this.arg(0, ARDouble.get());
		MPlayer mplayer = (MPlayer) this.arg(1, ARDPlayer.getAny(), dsender);
		
		// Power
		double oldStamina = mplayer.getStamina();
		
		// Detect "no change"
		double difference = Math.abs(stamina - oldStamina);
		double maxDifference = 0.1d;
		if (difference < maxDifference)
		{
			dsender.msg("%s's <i>stamina is already <h>%.2f<i>.", mplayer.getDisplayName(dsender), stamina);
			return;
		}
		
		// Apply
		mplayer.setStamina(stamina);
		double newStamina = mplayer.getStamina();
		
		// Inform
		dsender.msg("<i>You changed %s's <i>stamina from <h>%.2f <i>to <h>%.2f<i>.", mplayer.getDisplayName(dsender),  oldStamina, newStamina);
		if (dsender != mplayer)
		{
			mplayer.msg("%s <i>changed your stamina from <h>%.2f <i>to <h>%.2f<i>.", dsender.getDisplayName(mplayer), oldStamina, newStamina);
		}
		
	}
}
