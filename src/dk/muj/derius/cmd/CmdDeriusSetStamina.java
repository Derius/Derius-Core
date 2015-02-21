package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.arg.ARDouble;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.events.PlayerUpdateStaminaEvent;
import dk.muj.derius.events.PlayerUpdateStaminaEvent.StaminaUpdateReason;

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
		this.addRequirements(ReqHasPerm.get(Perm.SETSTAMINA.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Double stamina = this.arg(0, ARDouble.get());
		DPlayer dplayer = this.arg(1, ARDPlayer.getAny(), dsender);
		
		// Power
		double oldStamina = dplayer.getStamina();
		
		// Detect "no change"
		double difference = Math.abs(stamina - oldStamina);
		double maxDifference = 0.1d;
		if (difference < maxDifference)
		{
			dsender.msg("%s's <i>stamina is already <h>%.2f<i>.", dplayer.getDisplayName(dsender), stamina);
			return;
		}

		// Event
		PlayerUpdateStaminaEvent event = new PlayerUpdateStaminaEvent(dplayer, stamina, StaminaUpdateReason.COMMAND);
		event.run();
		if (event.isCancelled()) return;
		double newStamina = event.getStaminaAmount();
		
		// Apply
		dplayer.setStamina(newStamina);
		newStamina = dplayer.getStamina();
		
		// Inform
		dsender.msg("<i>You changed %s's <i>stamina from <h>%.2f <i>to <h>%.2f<i>.", dplayer.getDisplayName(dsender),  oldStamina, newStamina);
		if (dsender != dplayer)
		{
			dplayer.msg("%s <i>changed your stamina from <h>%.2f <i>to <h>%.2f<i>.", dsender.getDisplayName(dplayer), oldStamina, newStamina);
		}
		
	}
}
