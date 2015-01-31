package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;

public class CmdDeriusSpInfo extends DeriusCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpInfo()
	{
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_INFO.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		msg(MLang.get().specialisationInfo, MConf.get().softCap, MConf.get().hardCap, msender.getSpecialisationSlots());
		return;
	}

}
