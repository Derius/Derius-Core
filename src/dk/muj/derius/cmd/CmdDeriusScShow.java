package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.util.ScoreboardUtil;


public class CmdDeriusScShow extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusScShow()
	{
		// Args
		this.addOptionalArg("show", "yes/no");
		this.addOptionalArg("player", "you");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.SCOREBOARD_SHOW.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Boolean show = this.arg(0, ARBoolean.get(), true);
		DPlayer dplayer = this.arg(1, ARDPlayer.getAny(), dsender);
		
		boolean oldShow = dplayer.getBoardShowAtAll();
		
		// Detect "no change"
		if (show.equals(oldShow))
		{
			dsender.msg("<i>The scoreboard show settings are already <h>%s",  show.toString());
			return;
		}
		
		// Special cases
		if (show.equals(false))
		{
			ScoreboardUtil.resetScoreBoard(dplayer);
		}
		else if (show.equals(true) && dplayer.getStaminaBoardStay())
		{
			ScoreboardUtil.updateStaminaScore(dplayer, 5, dplayer.getStamina());
		}
		
		// Inform
		dsender.msg("<i>You changed %s's <i>show settings to <h>%s", dplayer.getDisplayName(dsender), show.toString());
		if (dsender != dplayer)
		{
			dplayer.msg("%s <i>changed your show settings from <h>%s <i>to <h>%s<i>.", dsender.getDisplayName(dplayer), oldShow, show);
		}
		
		// Apply
		dplayer.setBoardShowAtAll(show);
	}
}
