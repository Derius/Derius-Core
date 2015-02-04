package dk.muj.derius.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.arg.ArgReader;
import com.massivecraft.massivecore.cmd.arg.ArgReaderAbstract;

import dk.muj.derius.lambda.LvlStatus;
import dk.muj.derius.lambda.LvlStatusDefault;

public class ARLvlStatus extends ArgReaderAbstract<LvlStatus>
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
	public LvlStatus read(String arg, CommandSender sender) throws MassiveCommandException
	{
		ArgReader<Integer> innerReader = ARInteger.get();
		Integer inner = innerReader.read(arg, sender);
		
		return new LvlStatusDefault(inner);
	}

}
