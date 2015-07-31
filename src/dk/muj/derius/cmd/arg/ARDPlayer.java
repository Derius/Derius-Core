package dk.muj.derius.cmd.arg;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.cmd.arg.AR;

import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.entity.mplayer.MPlayerColl;


public class ARDPlayer
{
	// -------------------------------------------- //
	// INNER INSTANCE
	// -------------------------------------------- //
	
	public static AR<DPlayer> getAny() { return any; }
	
	public static AR<DPlayer> getOnline() { return online; }
	
	
	// -------------------------------------------- //
	// DPLAYER
	// -------------------------------------------- //
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static AR<DPlayer> any = (AR) MPlayerColl.get().getAREntity(SenderPresence.ANY);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static AR<DPlayer> online = (AR) MPlayerColl.get().getAREntity(SenderPresence.ONLINE);
	
}
