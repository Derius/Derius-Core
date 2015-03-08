package dk.muj.derius.cmd.arg;

import com.massivecraft.massivecore.cmd.arg.ARSenderIdAbstract;
import com.massivecraft.massivecore.cmd.arg.ArgReader;

import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.entity.mplayer.MPlayerColl;


public class ARDPlayer
{
	// -------------------------------------------- //
	// INNER INSTANCE
	// -------------------------------------------- //
	
	public static ArgReader<DPlayer> getAny() { return any; }
	
	public static ArgReader<DPlayer> getOnline() { return online; }
	
	
	// -------------------------------------------- //
	// DPLAYER
	// -------------------------------------------- //
	
	private static ArgReader<DPlayer> any = new ARSenderIdAbstract<DPlayer>(MPlayerColl.get(), false)
	{
		public DPlayer getResultForSenderId(String senderId) { return MPlayerColl.get().get(senderId); }
	};
	
	private static ArgReader<DPlayer> online = new ARSenderIdAbstract<DPlayer>(MPlayerColl.get(), true)
	{
		public DPlayer getResultForSenderId(String senderId) { return MPlayerColl.get().get(senderId); }
	};
	
}
