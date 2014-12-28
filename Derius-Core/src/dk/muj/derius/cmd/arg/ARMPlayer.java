package dk.muj.derius.cmd.arg;

import com.massivecraft.massivecore.cmd.arg.ArgReader;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;


public class ARMPlayer
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ArgReader<MPlayer> any = MPlayerColl.get().getAREntity();
	public static ArgReader<MPlayer> getAny() { return any; }
	
	private static ArgReader<MPlayer> online = MPlayerColl.get().getAREntity(true);
	public static ArgReader<MPlayer> getOnline() { return online; }
	
}
