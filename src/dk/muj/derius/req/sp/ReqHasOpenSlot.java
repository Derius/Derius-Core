package dk.muj.derius.req.sp;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.req.Req;
import dk.muj.derius.entity.Skill;

public class ReqHasOpenSlot implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ReqHasOpenSlot i = new ReqHasOpenSlot();
	public static ReqHasOpenSlot get() { return i; }
	private ReqHasOpenSlot() {}
	
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		MPlayer mplayer =  MPlayer.get(sender);
		if (mplayer == null) return false;
		return mplayer.getOpenSpecialisationSlots() > 0;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return Txt.parse(MLang.get().specialisationTooMany);
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
	public boolean apply(CommandSender sender, MassiveCommand cmd)
	{
		return this.apply(sender);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand cmd)
	{
		return this.createErrorMessage(sender);
	}

}
