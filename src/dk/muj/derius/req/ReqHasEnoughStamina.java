package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;

public class ReqHasEnoughStamina implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqHasEnoughStamina i = new ReqHasEnoughStamina();
	public static ReqHasEnoughStamina get() { return i; }
	private ReqHasEnoughStamina() {}
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills.");
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		throw new UnsupportedOperationException("This req doesn't support skills.");
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
	
	// -------------------------------------------- //
	// OVERRIDE: OTHER
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender arg0, MassiveCommand arg1)
	{
		throw new UnsupportedOperationException("This req doesn't support commands.");
	}

	@Override
	public String createErrorMessage(CommandSender arg0, MassiveCommand arg1)
	{
		throw new UnsupportedOperationException("This req doesn't support commands.");
	}
	
	@Override
	public boolean apply(CommandSender sender)
	{
		throw new UnsupportedOperationException("This req doesn't support default.");
	}

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		throw new UnsupportedOperationException("This req doesn't support default.");
	}

}
