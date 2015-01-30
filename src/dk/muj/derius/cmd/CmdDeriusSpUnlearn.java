package dk.muj.derius.cmd;

import java.util.LinkedHashMap;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.Skill;

public class CmdDeriusSpUnlearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpUnlearn()
	{
		super.addRequiredArg("skill");
		
		super.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_UNLEARN.node));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		Skill skill = super.arg(0, ARSkill.get());
		if (skill == null) return;
		
		long moveMillis = PlayerUtil.getLastMoveMillis(msender.getPlayer()) - System.currentTimeMillis() + Txt.millisPerSecond * MConf.get().specialiseChangeStandStillSeconds;
		
		if (moveMillis > 0)
		{
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(moveMillis, TimeUnit.getAllButMillis()), 3);
			String moveDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			msg("<b>You cannot change specialisation right now");
			msg("<b> please stand still for %s <b>more",moveDesc);
			return;
		}
		
		if (skill.isSpAutoAssigned())
		{
			msg(MLang.get().specialisationAutoAssigned, skill.getDisplayName(msender));
			return;
		}
		else if ( ! msender.isSpecialisedIn(skill))
		{
			msg(MLang.get().specialisationIsnt, skill.getDisplayName(msender));
			return;
		}
		
		msg(MLang.get().specialisationRemoved, skill.getDisplayName(msender));
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpUnlearn;
    }
	
}
