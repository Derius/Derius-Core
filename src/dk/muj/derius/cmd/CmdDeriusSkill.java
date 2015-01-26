package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;

public class CmdDeriusSkill extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSkill()
	{
		super.addRequiredArg("skillname");
		super.addOptionalArg("level", "your level");
		
		super.addRequirements(ReqHasPerm.get(Perm.SKILL.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> messages = new ArrayList<String>();
		
		// Args
		Skill skill = this.arg (0, ARSkill.get());
		if (skill == null) return;
		Integer level = this.arg(1, ARInteger.get(), -1);
		if (level == null) return;
		
		// Message construction
		messages.add(Txt.titleize(Txt.parse(skill.getDisplayName(msender)))); // Titel
		messages.add("<lime>"+skill.getDescription());
		
		// Swapping between default and user inserted value
		if (level <= -1)
		{
			LvlStatus status = msender.getLvlStatus(skill);
			messages.add(Txt.parse(status.toString()));
			level = status.getLvl();
		}
		else
		{
			messages.add(Txt.parse(MLang.get().levelStatusFormatMini, level));
		}

		// Passive Abilities
		messages.add(MLang.get().skillInfoPassiveAbilities);
		for (Ability a : skill.getPassiveAbilities())
		{
			if ( ! msender.canSeeAbility(a)) continue;
			messages.add(Txt.parse(a.getDisplayedDescription(msender)));
		}
		
		// Active Abilities
		messages.add(MLang.get().skillInfoActiveAbilities);
		for (Ability a : skill.getActiveAbilities())
		{
			if ( ! msender.canSeeAbility(a)) continue;
			messages.add(Txt.parse(a.getDisplayedDescription(msender)));
		}
		messages.add(MLang.get().skillInfoLevelStats);
		for (Ability a : skill.getAllAbilities())
		{
			if ( ! msender.canSeeAbility(a)) continue;
			messages.add(Txt.parse("%s: <i>%s", a.getDisplayName(msender), a.getLvlDescription(level)));
		}
		
		// Send Message
		sendMessage(messages);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSkill;
    }
	
}
