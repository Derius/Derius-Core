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
import dk.muj.derius.skill.SpecialisationStatus;

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
		
		SpecialisationStatus status = msender.setNotSpecialisedIn(skill);
		switch(status)
		{
			case AUTO_ASSIGNED : 
				msender.msg(Txt.parse(MConf.get().msgSkillIsAutoSpecialised),skill.getDisplayName(msender));
				return;
			case BLACK_LISTED : 
				msender.msg(Txt.parse(MConf.get().msgSkillIsBlackListed),skill.getDisplayName(msender));
				return;
			case DIDNT_HAVE : 
				msender.msg(Txt.parse(MConf.get().msgAlreadyNotSpecialised),skill.getDisplayName(msender));
				return;
			case DONT_HAVE_NOW : 
				msender.msg(Txt.parse(MConf.get().msgSpecialisationRemoveSuceed, skill.getDisplayName(msender)));
				return;
			default:
				msender.msg(Txt.parse(MConf.get().specialisationError, skill.getDisplayName(msender)));
				break;
				
		}
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpUnlearn;
    }
}
