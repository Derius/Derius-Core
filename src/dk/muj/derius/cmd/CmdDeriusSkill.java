package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARLvlStatus;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.lambda.LvlStatus;
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
		LvlStatus status = this.arg(1, ARLvlStatus.get(), msender.getLvlStatus(skill));
		if (status == null) return;
		
		// Message construction
		List<String> msgs = new ArrayList<String>();
		
		msgs.add(Txt.titleize(skill.getDisplayName(msender)));	// Title
		msgs.add("<lime>" + skill.getDescription());			// Description
		
		// Swapping between default and user inserted value
		msgs.add(status.toString());

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
			msgs.add(String.format("%s: <i>%s", ability.getDisplayName(msender), ability.getLvlDescriptionMsg(status.getLvl())));
		}
		
		// Send Message
		msg(msgs);
		
		return;
	}
	
}
