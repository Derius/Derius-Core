package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;

public class CmdDeriusSpInfo extends DeriusCommand
{
	
	// -------------------------------------------- //
	// CLASS FIELDS
	// -------------------------------------------- //
	
	public static String specialisationInfo = Txt.parse("When you specialise in a skill you are able to exceed level %s, and reach level %s."
			+ " You can only specialise in %s skills. If you unlearn/unspecialise in a skill you get reset back to level 0",
			
			MConf.get().softCap, MConf.get().hardCap, MConf.get().specialisationMax);
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpInfo()
	{
		super.setDesc("info about specialisation");
		
		super.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_INFO.node));
	}
		

	
	@Override
	public void perform()
	{
		this.msg(Txt.parse("<i>"+specialisationInfo));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpInfo;
    }
}
