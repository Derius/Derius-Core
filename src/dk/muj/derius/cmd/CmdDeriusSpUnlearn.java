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
import dk.muj.derius.skill.Skill;

public class CmdDeriusSpUnlearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpUnlearn()
	{
		super.addRequiredArg("skill");
		super.setDesc("unspecialises in said skill");
		
		super.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_UNLEARN.node));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Skill skill = super.arg(0, ARSkill.get());
		if(skill == null) return;
		long moveMillis = PlayerUtil.getLastMoveMillis(msender.getPlayer()) - System.currentTimeMillis() + Txt.millisPerSecond * MConf.get().specialiseChangeStandStillSeconds;
		
		if(moveMillis > 0)
		{
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(moveMillis, TimeUnit.getAllButMillis()), 3);
			String moveDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			msender.sendMessage(Txt.parse("<b>You cannot change specialisation right now"));
			msender.sendMessage(Txt.parse("<b> please stand still for %s <b>more",moveDesc));
			return;
		}
		
		if (skill.isSpAutoAssigned())
		{
			
			msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationAutoAssigned), skill.getDisplayName(msender));
			return;
		}
		else if ( ! msender.isSpecialisedIn(skill))
		{
			msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationIsnt), skill.getDisplayName(msender));
			return;
		}
		
		msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationRemoveSuceed, skill.getDisplayName(msender)));
		return;
				
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpUnlearn;
    }
	
}
