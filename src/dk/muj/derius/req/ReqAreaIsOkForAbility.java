package dk.muj.derius.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Const;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.integration.FactionIntegration;

public class ReqAreaIsOkForAbility extends ReqAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	final Ability ability;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqAreaIsOkForAbility get(Ability ability) { return new ReqAreaIsOkForAbility(ability); }
	public ReqAreaIsOkForAbility(Ability ability) { this.ability = ability; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean apply(CommandSender sender, MassiveCommand arg1)
	{
		if (IdUtil.isPlayer(sender)) return true;
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
	public String createErrorMessage(CommandSender sender, MassiveCommand arg1)
	{
		return Txt.parse(MConf.get().msgAbilityCantBeUsedInArea, ability.getDisplayedDescription(sender));
	}

}
