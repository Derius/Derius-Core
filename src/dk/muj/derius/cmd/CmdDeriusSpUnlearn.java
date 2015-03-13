package dk.muj.derius.cmd;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;

public class CmdDeriusSpUnlearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpUnlearn()
	{
		this.addRequiredArg("skill");
		
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SPECIALISATION_UNLEARN.getNode()));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Skill skill = this.arg(0, ARSkill.get());
		
		long moveMillis = PlayerUtil.getLastMoveMillis(dsender.getPlayer()) - System.currentTimeMillis() + Txt.millisPerSecond * MConf.get().specialiseChangeStandStillSeconds;
		
		if (moveMillis > 0)
		{
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(moveMillis, TimeUnit.getAllButMillis()), 3);
			String moveDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			msg(DLang.get().getSpecialisationCantChange());
			msg(DLang.get().getSpecialisationMoveCooldown().replace("{time}", moveDesc));
			return;
		}
		
		if (skill.isSpAutoAssigned())
		{
			msg(DLang.get().getSpecialisationAutoAssigned().replace("{skill}", skill.getDisplayName(dsender)));
			return;
		}
		else if ( ! dsender.isSpecialisedIn(skill))
		{
			msg(DLang.get().getSpecialisationIsnt().replace("{skill}", skill.getDisplayName(dsender)));
			return;
		}
		
		dsender.setNotSpecialisedIn(skill);
		
		msg(DLang.get().getSpecialisationRemoved().replace("{skill}", skill.getDisplayName(dsender)));
		
		return;
	}
	
}
