package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.util.ScoreboardUtil;

public class CmdDeriusScKeep extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusScKeep()
	{
		// Args
		this.addOptionalArg("keep", "yes/no");
		this.addOptionalArg("player", "you");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.SCOREBOARD_KEEP.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Boolean keep = this.arg(0, ARBoolean.get(), true);
		DPlayer dplayer = this.arg(1, ARDPlayer.getAny(), dsender);
		
		boolean oldKeep = dplayer.getStaminaBoardStay();
		
		// Detect "no change"
		if (keep.equals(oldKeep))
		{
			dsender.msg("<i>The scoreboard keep settings are already <h>%s",  keep.toString());
			return;
		}

		// Inform
		dsender.msg("<i>You have changed %s's <i>keep settings to <h>%s", dplayer.getDisplayName(dsender), keep.toString());
		if (dsender != dplayer)
		{
			dplayer.msg("%s <i>changed your show settings from <h>%s <i>to <h>%s<i>.", dsender.getDisplayName(dplayer), oldKeep, keep);
		}
		
		// Apply
		dplayer.setStaminaBoardStay(keep);
		
		// Special cases
		if (keep.equals(false))
		{
			ScoreboardUtil.resetScoreBoard(dplayer);
		}
		else
		{
			ScoreboardUtil.updateStaminaScore(dplayer, 5, dplayer.getStamina());
		}
	}
}
