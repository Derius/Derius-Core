package dk.muj.derius.cmd;

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
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_INFO.node));
	}
	
	@Override
	public void perform()
	{
		msg("<i>"+specialisationInfo);
	}

}
