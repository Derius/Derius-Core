package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.arg.ARDouble;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.entity.mplayer.MPlayer;

public class CmdDeriusSetStamina extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSetStamina()
	{
		// Args
		this.addArg(ARDouble.get(), "stamina").setDesc("The amount we wat to set the stamina to");
		this.addArg(getPlayerYou()).setDesc("the player whose stamina to set");
		
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
		Double stamina = this.readArg();
		MPlayer mplayer = (MPlayer) this.readArg(dsender);
		
		// Power
		double oldStamina = mplayer.getStamina();
		
		// Detect "no change"
		double difference = Math.abs(stamina - oldStamina);
		double maxDifference = 0.01D;
		if (difference < maxDifference)
		{
			msg(DLang.get().getStaminaIsAlready().replace("{player}", mplayer.getDisplayName(dsender)).replace("{stamina}", String.valueOf(MUtil.round(stamina, 2))));
			return;
		}
		
		// Apply
		mplayer.setStamina(stamina);
		double newStamina = mplayer.getStamina();
		
		// Inform
		mplayer.msg(DLang.get().getStaminaSet().replace("{player}", dsender.getDisplayName(mplayer)).replace("{stamina}", String.valueOf(newStamina)).replace("{oldstamina}", String.valueOf(oldStamina)));
		
		return;
	}
}
