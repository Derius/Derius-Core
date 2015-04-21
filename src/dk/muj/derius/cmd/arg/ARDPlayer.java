package dk.muj.derius.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.massivecore.cmd.arg.ARSenderIdAbstract;

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
	
	private static AR<DPlayer> any = new ARSenderIdAbstract<DPlayer>(MPlayerColl.get(), false)
	{
		public DPlayer getResultForSenderId(String senderId) { return MPlayerColl.get().get(senderId); }

		@Override
		public Collection<String> getTabList(CommandSender sender, String arg)
		{
			return null;
		}
	};
	
	private static AR<DPlayer> online = new ARSenderIdAbstract<DPlayer>(MPlayerColl.get(), true)
	{
		public DPlayer getResultForSenderId(String senderId) { return MPlayerColl.get().get(senderId); }

		@Override
		public Collection<String> getTabList(CommandSender sender, String arg)
		{
			return null;
		}
	};
	
}
