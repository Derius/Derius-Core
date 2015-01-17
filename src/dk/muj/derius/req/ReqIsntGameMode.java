package dk.muj.derius.req;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;

public class ReqIsntGameMode implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsntGameMode get(List<GameMode> gameModes) { return new ReqIsntGameMode(gameModes); }
	public ReqIsntGameMode(List<GameMode> gameModes) { this.gameModes = gameModes; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<GameMode> gameModes;
	public List<GameMode> getGameMode() { return this.gameModes; }
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender)
	{
		for (GameMode gm : gameModes)
		{
			if ( IdUtil.isGameMode(sender, gm, false)) return false;
		}
		return true;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return Txt.parse("<i>You can't do this in this gamemode.");
	}

	// -------------------------------------------- //
	// OVERRIDE: OTHER
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		return this.apply(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse("<i>You can't level %s <i>in this gamemode.", skill.getDisplayName(sender));
	}
	
	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		return this.apply(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return Txt.parse("<i>You can't use %s <i>in this gamemode.", ability.getDisplayName(sender));
	}
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand arg1)
	{
		return this.apply(sender);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand arg1)
	{
		return Txt.parse("<i>You can't perform this command in this GameMode.");
	}
}
