package dk.muj.derius.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.req.util.ReqNoCmd;
import dk.muj.derius.req.util.ReqNoDefault;
import dk.muj.derius.req.util.ReqNoSkill;

public class ReqAbilityCanBeUsedInArea implements Req, ReqNoCmd, ReqNoSkill, ReqNoDefault
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
	public boolean apply(CommandSender sender, Ability ability)
	{
		if ( ! IdUtil.isPlayer(sender)) return true;
		Player player = (Player) sender;
		PS loc =  PS.valueOf(player.getLocation());
		
		return ability.getWorldsUse().contains(loc.getWorld());
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return Txt.parse("<b>The ability <reset>%s <b>can't be used in this area", ability.getDisplayName(sender));
	}

}
