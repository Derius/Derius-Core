package dk.muj.derius.cmd;

import java.util.LinkedHashMap;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.SpecialisationStatus;

public class CmdDeriusSpLearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpLearn()
	{
		super.addRequiredArg("skill");
		super.setDesc("specialises in said skill");
		
		super.addRequirements(ReqIsPlayer.get());
		super.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LEARN.node));
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
		
		SpecialisationStatus status = msender.setSpecialisedIn(skill);
		switch(status)
		{
			case AUTO_ASSIGNED : 
				msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationAutoAssigned),skill.getDisplayName(msender));
				return;
			case BLACK_LISTED : 
				msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationBlackList),skill.getDisplayName(msender));
				return;
			case HAD : 
				msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationAlreadyHas),skill.getDisplayName(msender));
				return;
			case TOO_MANY : 
				msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationTooMany),skill.getDisplayName(msender));
				return;
			case HAS_NOW : 
				msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationAddSucceed, skill.getDisplayName(msender)));
				return;
			default:
				msender.msg(Txt.parse(MConf.get().msgSkillSpecialisationError, skill.getDisplayName(msender)));
				break;
				
		}
		
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpLearn;
    }
	
}
