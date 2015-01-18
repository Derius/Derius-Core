package dk.muj.derius.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class ReqCooldownIsExpired implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ReqCooldownIsExpired i = new ReqCooldownIsExpired();
	public static ReqCooldownIsExpired get() { return i; }
	private ReqCooldownIsExpired() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	

	@Override
	public boolean apply(CommandSender sender)
	{
		MPlayer mplayer = MPlayer.get(IdUtil.getId(sender.getName()));
		if (mplayer.hasCooldownExpired()) return true;
		return false;
	}
	

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		MPlayer mplayer = MPlayer.get(IdUtil.getId(sender.getName()));
		int seconds = (int) mplayer.getCooldownExpireIn() / 1000;
		
		return Txt.parse(Txt.parse(MLang.get().exhausted, seconds));
	}

	// -------------------------------------------- //
	// OVERRIDE: SKILL
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
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
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
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVECOMMAND
	// -------------------------------------------- //
	
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
