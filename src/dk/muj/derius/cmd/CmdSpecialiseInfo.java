package dk.muj.derius.cmd;

import java.util.List;

import dk.muj.derius.entity.MConf;

public class CmdSpecialiseInfo extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdSpecialiseInfo()
	{
		super.setDesc("info about specialisation");
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesSpecialiseInfo;
    }
}
