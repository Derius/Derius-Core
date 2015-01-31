package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.util.AbilityUtil;

public class CmdDeriusSkill extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSkill()
	{
		this.addRequiredArg("skillname");
		this.addOptionalArg("level", "your level");
		
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{	
		// Args
		Skill skill = this.arg (0, ARSkill.get());
		if (skill == null) return;
		Integer level = this.arg(1, ARInteger.get(), -1);
		if (level == null) return;
		
		// Message construction
		List<String> msgs = new ArrayList<String>();
		
		msgs.add(Txt.titleize(skill.getDisplayName(msender)));	// Title
		msgs.add("<lime>" + skill.getDescription());			// Description
		
		// Swapping between default and user inserted value
		if (level <= -1)
		{
			LvlStatus status = msender.getLvlStatus(skill);
			msgs.add(status.toString());
			level = status.getLvl();
		}
		else
		{
			msgs.add(Txt.parse(MLang.get().levelStatusFormatMini, level));
		}

		// All Abilities
		msgs.add(MLang.get().skillInfoAbilities);
		for (Ability ability : skill.getAllAbilities())
		{
			if ( ! AbilityUtil.canPlayerSeeAbility(msender, ability, false)) continue;
			msgs.add(ability.getDisplayedDescription(msender));
		}
		msgs.add(MLang.get().skillInfoLevelStats);
		for (Ability ability : skill.getAllAbilities())
		{
			if ( ! AbilityUtil.canPlayerSeeAbility(msender, ability, false)) continue;
			msgs.add(String.format("%s: <i>%s", ability.getDisplayName(msender), ability.getLvlDescriptionMsg(level)));
		}
		
		// Send Message
		msg(msgs);
	}
	
}
