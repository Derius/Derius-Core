package dk.muj.derius.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.massivecore.cmd.arg.ARAbstract;
import com.massivecraft.massivecore.cmd.arg.ARInteger;

import dk.muj.derius.api.lvl.LvlStatus;
import dk.muj.derius.api.lvl.LvlStatusDefault;

public class ARLvlStatus extends ARAbstract<LvlStatus>
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARLvlStatus i = new ARLvlStatus();
	public static ARLvlStatus get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public LvlStatus read(String arg, CommandSender sender) throws MassiveException
	{
		AR<Integer> innerReader = ARInteger.get();
		Integer inner = innerReader.read(arg, sender);
		
		return LvlStatusDefault.valueOf(inner);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
