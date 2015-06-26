package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.util.ScoreboardUtil;


public class CmdDeriusScShow extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusScShow()
	{
		// Args
		this.addArg(ARBoolean.get(), "show", "toggle").setDesc("Toggle scoreboard visibility on/off");
		this.addArg(getPlayerYou()).setDesc("The player whose visibility is toggled");
		
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
		DPlayer dplayer = this.readArg(dsender);
		boolean oldShow = dplayer.getBoardShowAtAll();
		boolean show = this.readArg( ! oldShow);
		
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
