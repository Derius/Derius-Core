package dk.muj.derius.req;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.req.util.ReqNoDefault;
import dk.muj.derius.req.util.ReqNoSkill;

public class ReqAbilityCanBeUsedInArea implements Req, ReqNoSkill, ReqNoDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqAbilityCanBeUsedInArea i = new ReqAbilityCanBeUsedInArea();
	public static ReqAbilityCanBeUsedInArea get() { return i; }
	private ReqAbilityCanBeUsedInArea() {}
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.NORMAL;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public boolean apply(DPlayer dplayer, Ability ability)
	{
		PS loc =  Mixin.getSenderPs(dplayer);
		if (loc == null) return true;
		
		return ability.getWorldsUse().contains(loc.getWorld());
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		return Txt.parse("<b>The ability <reset>%s <b>can't be used in this area", ability.getDisplayName(dplayer));
	}

}
