package dk.muj.derius.req.sp;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
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
	public boolean apply(DPlayer dplayer)
	{
		return dplayer.getOpenSpecialisationSlots() > 0;
	}
	
	@Override
	public String createErrorMessage(DPlayer dplayer)
	{
		return Txt.parse(DLang.get().getSpecialisationTooMany());
	}

}
