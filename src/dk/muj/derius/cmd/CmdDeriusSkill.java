package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.LvlStatus;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.cmd.arg.ARLvlStatus;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.util.AbilityUtil;

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
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.getNode()));
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
		List<String> msgs = new ArrayList<String>();
		
		msgs.add(Txt.titleize(skill.getDisplayName(dsender)));	// Title
		msgs.add("<lime>" + skill.getDesc());			// Description
		
		// Swapping between default and user inserted value
		msgs.add(status.toString());

		// All Abilities
		msgs.add(MLang.get().skillInfoAbilities);
		for (Ability ability : skill.getAbilities())
		{
			if ( ! AbilityUtil.canPlayerSeeAbility(dsender, ability, VerboseLevel.ALWAYS)) continue;
			msgs.add(ability.getDisplayedDescription(dsender));
		}
		msgs.add(MLang.get().skillInfoLevelStats);
		for (Ability ability : skill.getAbilities())
		{
			if ( ! AbilityUtil.canPlayerSeeAbility(dsender, ability, VerboseLevel.ALWAYS)) continue;
			msgs.add(String.format("%s: <i>%s", ability.getDisplayName(dsender), ability.getLvlDescriptionMsg(status.getLvl())));
		}
		
		// Send Message
		msg(msgs);
		
		return;
	}
	
}
