package dk.muj.derius.cmd;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.Skill;
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
		this.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LEARN.node));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveCommandException
	{
		// Args
		Skill skill = this.arg(0, ARSkill.get());
		
		if (! SkillUtil.canPlayerSpecialiseSkill(msender, skill, true)) return;
		
		long moveMillis = PlayerUtil.getLastMoveMillis(msender.getPlayer()) - System.currentTimeMillis() + Txt.millisPerSecond * MConf.get().specialiseChangeStandStillSeconds;
		
		if (moveMillis > 0)
		{
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(moveMillis, TimeUnit.getAll()), 3);
			String moveDesc = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			msg(MLang.get().specialisationCantChange);
			msg(MLang.get().specialisationMoveCooldown, moveDesc);
			return;
		}
		
		msender.setSpecialisedIn(skill, true);
	
		return;
	}
	
}
