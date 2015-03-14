package dk.muj.derius.cmd;

import java.util.LinkedHashMap;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.SkillUtil;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;

public class CmdDeriusSpLearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpLearn()
	{
		this.addRequiredArg("skill");
		
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SPECIALISATION_LEARN.getNode()));
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
			msg(DLang.get().getSpecialisationCantChange());
			msg(DLang.get().getSpecialisationMoveCooldown().replace("{time}", moveDesc));
			return;
		}
		
		dsender.setSpecialisedIn(skill, true);
		msg(DLang.get().getSpecialisationSuccess().replace("{skill}", skill.getDisplayName(dsender)));
	
		return;
	}
	
}
