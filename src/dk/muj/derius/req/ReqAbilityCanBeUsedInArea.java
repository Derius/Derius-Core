package dk.muj.derius.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Const;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.integration.FactionIntegration;
import dk.muj.derius.skill.Skill;

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
		
		if(FactionIntegration.establishIntegration())
		{
			Faction f = BoardColl.get().getFactionAt(loc);
			if(f != null)
			{
				if(f.getFlag(Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD))
				{
					return f.getFlag(Const.FACTION_FLAG_ABILITIES_USE);
				}
			}
			
		}
		return MConf.get().worldAbilityUse.get(ability.getId()).contains(loc.getWorld());
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
		return false;
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse("<b>This should not happen, a bug occured. A SKILL WAS PASSED INSTEAD OF ABILITY");
	}
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVECOMMAND
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender arg0, MassiveCommand arg1)
	{
		return false;
	}

	@Override
	public String createErrorMessage(CommandSender arg0, MassiveCommand arg1)
	{
		return Txt.parse("<b>This should not happen, a bug occured. A COMMAND WAS PASSED INSTEAD OF ABILITY");
	}

}
