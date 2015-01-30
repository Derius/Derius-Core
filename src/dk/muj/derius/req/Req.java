package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.Skill;

public interface Req extends com.massivecraft.massivecore.cmd.req.Req
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public boolean apply(CommandSender sender, Skill skill);
	public String createErrorMessage(CommandSender sender, Skill skill);
	
	public boolean apply(CommandSender sender, Ability ability);
	public String createErrorMessage(CommandSender sender, Ability ability);

}
