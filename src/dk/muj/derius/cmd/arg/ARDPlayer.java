package dk.muj.derius.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ArgReader;
import com.massivecraft.massivecore.cmd.arg.ArgReaderAbstract;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;


public class ARDPlayer
{
	// -------------------------------------------- //
	// INNER INSTANCE
	// -------------------------------------------- //
	
	private static ArgReader<MPlayer> innerAny = MPlayerColl.get().getAREntity();

	
	private static ArgReader<MPlayer> innerOnline = MPlayerColl.get().getAREntity(true);
	public static ArgReader<DPlayer> getOnline() { return online; }
	
	
	// -------------------------------------------- //
	// DPLAYER
	// -------------------------------------------- //
	
	public static ArgReader<DPlayer> getAny() { return any; }
	private static ArgReader<DPlayer> any = new ArgReaderAbstract<DPlayer>()
	{

		@Override
		public DPlayer read(String arg, CommandSender sender) throws MassiveException
		{
			return innerAny.read(arg, sender);
		}
		
	};
	
	private static ArgReader<DPlayer> online = new ArgReaderAbstract<DPlayer>()
	{

		@Override
		public DPlayer read(String arg, CommandSender sender) throws MassiveException
		{
			return innerOnline.read(arg, sender);
		}
		
	};
	
}
