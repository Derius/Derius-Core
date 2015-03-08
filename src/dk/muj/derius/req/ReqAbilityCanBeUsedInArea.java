package dk.muj.derius.req;

import org.bukkit.Location;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.player.DPlayer;
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
		if ( ! dplayer.isPlayer()) return false;
		Location loc = dplayer.getPlayer().getLocation();
		
		return ability.getWorldsUse().contains(loc.getWorld());
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		return Txt.parse("<b>The ability <reset>%s <b>can't be used in this area", ability.getDisplayName(dplayer));
	}

}
