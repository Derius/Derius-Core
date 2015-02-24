package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqNoCmd;
import dk.muj.derius.req.util.ReqNoDefault;
import dk.muj.derius.req.util.ReqNoSkill;

public class ReqHasEnoughStamina implements Req, ReqNoSkill, ReqNoCmd, ReqNoDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqHasEnoughStamina i = new ReqHasEnoughStamina();
	public static ReqHasEnoughStamina get() { return i; }
	private ReqHasEnoughStamina() {}
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.HIGH;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(sender);
		double staminaUsage = ability.getStaminaUsage();
		
		return dplayer.hasEnoughStamina(staminaUsage);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return Txt.parse(MLang.get().mustHaveEnoughStamina, ability.getDisplayName(sender));
	}

}
