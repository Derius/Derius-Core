package dk.muj.derius.req;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqNoDefault;
import dk.muj.derius.req.util.ReqNoSkill;

public class ReqHasEnoughStamina implements Req, ReqNoSkill, ReqNoDefault
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
	public boolean apply(DPlayer dplayer, Ability ability)
	{
		return dplayer.hasEnoughStamina(ability.getStaminaUsage());
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		return Txt.parse(MLang.get().mustHaveEnoughStamina, ability.getDisplayName(dplayer));
	}

}
