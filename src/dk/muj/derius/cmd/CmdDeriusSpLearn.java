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
import dk.muj.derius.entity.Skill;

public class CmdDeriusSpLearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpLearn()
	{
		super.addRequiredArg("skill");
		
		super.addRequirements(ReqIsPlayer.get());
		super.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LEARN.node));
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
		
		if(moveMillis > 0)
		{
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(moveMillis, TimeUnit.getAll()), 3);
			String moveDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			msg("<b>You cannot change specialisation right now");
			msg("<b> please stand still for %s <b>more", moveDesc);
			return;
		}
		
		msender.setSpecialisedIn(skill, true);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpLearn;
    }
	
}
