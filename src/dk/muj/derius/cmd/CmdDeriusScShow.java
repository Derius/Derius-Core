package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.config.DLang;
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
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SCOREBOARD_SHOW.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean show = this.arg(0, ARBoolean.get(), true);
		DPlayer dplayer = this.arg(1, ARDPlayer.getAny(), dsender);
		
		boolean oldShow = dplayer.getBoardShowAtAll();
		
		// Detect "no change"
		if (show == oldShow)
		{
			throw new MassiveException().addMsg(
					DLang.get().getScoreBoardSettingShowAlready().replace("{player}", dplayer.getDisplayName(dsender)).replace("{state}", show ? "on" : "off"));
		}
		
		// Special cases
		if (show == false)
		{
			ScoreboardUtil.resetScoreBoard(dplayer);
		}
		else if (show == true && dplayer.getStaminaBoardStay())
		{
			ScoreboardUtil.updateStaminaScore(dplayer, 5, dplayer.getStamina());
		}
		
		// Inform
		msg(DLang.get().getScoreBoardSettingShowSet().replace("{player}", dplayer.getDisplayName(dsender)).replace("{state}", show ? "on" : "off"));
		if (dsender != dplayer)
		{
			msg(DLang.get().getScoreBoardSettingShowSetByOther().replace("{player}", dplayer.getDisplayName(dsender)).replace("{state}", show ? "on" : "off").replace("{oldstate}", oldShow ? "on" : "off"));
		}
		
		// Apply
		dplayer.setBoardShowAtAll(show);
	}
}
