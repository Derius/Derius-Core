package dk.muj.derius.cmd;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.util.SkillUtil;

public class CmdDeriusSpLearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpLearn()
	{
		this.addRequiredArg("skill");
		
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LEARN.getNode()));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Skill skill = this.arg(0, ARSkill.get());
		
		if (! SkillUtil.canPlayerSpecialiseSkill(dsender, skill, VerboseLevel.LOW)) return;
		
		long moveMillis = PlayerUtil.getLastMoveMillis(dsender.getPlayer()) - System.currentTimeMillis() + Txt.millisPerSecond * MConf.get().specialiseChangeStandStillSeconds;
		
		if (moveMillis > 0)
		{
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(moveMillis, TimeUnit.getAll()), 3);
			String moveDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			msg(MLang.get().specialisationCantChange);
			msg(MLang.get().specialisationMoveCooldown, moveDesc);
			return;
		}
		
		dsender.setSpecialisedIn(skill, true);
		msg(MLang.get().specialisationSuccess, skill.getDisplayName(dsender));
	
		return;
	}
	
}
