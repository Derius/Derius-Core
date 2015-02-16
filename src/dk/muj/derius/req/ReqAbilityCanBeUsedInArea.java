package dk.muj.derius.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;

public class ReqAbilityCanBeUsedInArea implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqAbilityCanBeUsedInArea i = new ReqAbilityCanBeUsedInArea();
	public static ReqAbilityCanBeUsedInArea get() { return i; }
	private ReqAbilityCanBeUsedInArea() {}
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender arg0)
	{
		return false;
	}
	
	@Override
	public String createErrorMessage(CommandSender arg0)
	{
		return Txt.parse("<b>That ability can't be used in this area");
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
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills");
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills");
	}
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVECOMMAND
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		throw new UnsupportedOperationException("This req doesn't support commands");
	}

	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		throw new UnsupportedOperationException("This req doesn't support commands");
	}

}
