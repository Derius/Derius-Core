package dk.muj.derius.req.sp;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqHasOpenSlot implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqHasOpenSlot i = new ReqHasOpenSlot();
	public static ReqHasOpenSlot get() { return i; }
	private ReqHasOpenSlot() {}
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.NORMAL;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		DPlayer dplayer =  DeriusAPI.getDPlayer(sender);
		if (dplayer == null) return false;
		return dplayer.getOpenSpecialisationSlots() > 0;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return Txt.parse(MLang.get().specialisationTooMany);
	}

}
