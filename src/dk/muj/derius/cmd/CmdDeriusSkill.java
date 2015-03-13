package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.lvl.LvlStatus;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.cmd.arg.ARLvlStatus;
import dk.muj.derius.cmd.arg.ARSkill;

public class CmdDeriusSkill extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSkill()
	{
		// Args
		this.addRequiredArg("skillname");
		this.addOptionalArg("level", "your");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SKILL.getNode()));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Args
		Skill skill = this.arg (0, ARSkill.get());
		LvlStatus status = this.arg(1, ARLvlStatus.get(), dsender.getLvlStatus(skill));
		
		// Message construction
		List<String> msgs = new ArrayList<>();
		
		msgs.add(Txt.titleize(skill.getDisplayName(dsender)));	// Title
		msgs.add("<k>Desc: <i>" + skill.getDesc());			// Description
		
		// Exp descs
		msgs.add(DLang.get().getSkillInfoExpGain() + Txt.implodeCommaAndDot(skill.getEarnExpDescs(), "<lime>, <i>", "<lime>& <i>", "<lime>."));
		
		// Swapping between default and user inserted value
		msgs.add(status.toString());
		
		// All Abilities
		msgs.add(DLang.get().getSkillInfoAbilities());
		for (Ability ability : skill.getAbilities())
		{
			if ( ! AbilityUtil.canPlayerSeeAbility(dsender, ability, VerboseLevel.ALWAYS)) continue;
			msgs.add(ability.getDisplayedDescription(dsender));
		}
		msgs.add(DLang.get().getSkillInfoLevelStats());
		for (Ability ability : skill.getAbilities())
		{
			if ( ! AbilityUtil.canPlayerSeeAbility(dsender, ability, VerboseLevel.ALWAYS)) continue;
			if ( ! ability.getLvlDescriptionMsg(status.getLvl()).isPresent()) continue;
			msgs.add(String.format("%s: <i>%s", ability.getDisplayName(dsender), ability.getLvlDescriptionMsg(status.getLvl()).get()));
		}
		
		// Send Message
		msg(msgs);
		
		return;
	}
	
}
