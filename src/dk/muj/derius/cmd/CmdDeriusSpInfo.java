package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.config.DLang;

public class CmdDeriusSpInfo extends DeriusCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpInfo()
	{
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SPECIALISATION_INFO.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		msg(DLang.get().getSpecialisationInfo().replace("{max}", String.valueOf(dsender.getMaxSpecialisationSlots())));
		return;
	}

}
