package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
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
		this.addOptionalArg("yes/no", "yes");
		this.addOptionalArg("player", "you");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SCOREBOARD_KEEP.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean keep = this.arg(ARBoolean.get(), true);
		DPlayer dplayer = this.arg(ARDPlayer.getAny(), dsender);
		
		boolean oldKeep = dplayer.getStaminaBoardStay();
		
		// Detect "no change"
		if (keep == oldKeep)
		{
			throw new MassiveException().addMsg(
					DLang.get().getScoreBoardSettingKeepAlready().replace("{player}", dplayer.getDisplayName(dsender)).replace("{state}", keep ? "on" : "off"));
		}

		// Inform
		msg(DLang.get().getScoreBoardSettingKeepSet().replace("{player}", dplayer.getDisplayName(dsender)).replace("{state}", keep ? "on" : "off"));
		if (dsender != dplayer)
		{
			dplayer.msg(DLang.get().getScoreBoardSettingKeepSetByOther().replace("{player}", dplayer.getDisplayName(dsender)).replace("{state}", keep ? "on" : "off").replace("{oldstate}", oldKeep ? "on" : "off"));
		}
		
		// Apply
		dplayer.setStaminaBoardStay(keep);
		
		// Special cases
		if (keep == false)
		{
			ScoreboardUtil.resetScoreBoard(dplayer);
		}
		else
		{
			ScoreboardUtil.updateStaminaScore(dplayer, 5, dplayer.getStamina());
		}
	}
}
