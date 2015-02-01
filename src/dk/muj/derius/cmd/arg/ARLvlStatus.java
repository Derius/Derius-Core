package dk.muj.derius.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.arg.ArgReader;
import com.massivecraft.massivecore.cmd.arg.ArgReaderAbstract;
import com.massivecraft.massivecore.cmd.arg.ArgResult;

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
	public ArgResult<LvlStatus> read(String arg, CommandSender sender)
	{
		ArgResult<LvlStatus> ret = new ArgResult<LvlStatus>();
		
		ArgReader<Integer> innerReader = ARInteger.get();
		ArgResult<Integer> inner = innerReader.read();
		
		if (inner.hasErrors())
		{
			ret.setErrors(inner.getErrors());
			return ret;
		}
		
		ret.setResult(new LvlStatusDefault(inner.getResult()));
		
		return ret;
	}

}
