package dk.muj.derius.cmd;

import java.util.List;

import dk.muj.derius.entity.MConf;

public class CmdDeriusSpecialise extends DeriusCommand
{

	public CmdDeriusSpecialise()
	{
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpecialise;
    }
}
