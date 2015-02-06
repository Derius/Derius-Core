package dk.muj.derius.cmd.arg;

import com.massivecraft.massivecore.cmd.arg.ArgReader;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.entity.MPlayerColl;


public class ARDPlayer
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ArgReader<? extends DPlayer> any = MPlayerColl.get().getAREntity();
	public static ArgReader<? extends DPlayer> getAny() { return any; }
	
	private static ArgReader<? extends DPlayer> online = MPlayerColl.get().getAREntity(true);
	public static ArgReader<? extends DPlayer> getOnline() { return online; }
	
}
