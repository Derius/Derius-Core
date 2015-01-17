package dk.muj.derius.req;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;

public class ReqIsGameMode implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsGameMode get(GameMode gameMode) { return new ReqIsGameMode(gameMode); }
	public ReqIsGameMode(GameMode gameMode) { this.gameMode = gameMode; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final GameMode gameMode;
	public GameMode getGameMode() { return this.gameMode; }
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender)
	{
		return IdUtil.isGameMode(sender, gameMode, true);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return Txt.parse("<b>You are not in the required gamemode.");
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
		return this.createErrorMessage(sender);
	}
	
	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		return this.apply(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return this.createErrorMessage(sender);
	}
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand arg1)
	{
		return this.apply(sender);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand arg1)
	{
		return this.createErrorMessage(sender);
	}
	
}