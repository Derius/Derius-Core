package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.AbilityColl;
import dk.muj.derius.entity.Skill;

public class CmdDeriusKeysAbilityid extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysAbilityid()
	{
		this.addOptionalArg("skill", "all");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_ABILITYID.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveCommandException
	{
		List<String> messages = new ArrayList<String>();
		List<Ability> abilities = new ArrayList<Ability>();
		
		//Args
		Skill skill = this.arg(0, ARSkill.get(), null);
		
		if (skill == null)
		{
			abilities.addAll(AbilityColl.getAllAbilities());
		}
		else
		{
			abilities.addAll(skill.getAllAbilities());
		}
		
		// Apply
		for (Ability ability : AbilityColl.getAllAbilities())
		{
			messages.add(Txt.parse("<red>%s %s", ability.getId(), ability.getName()));
		}
		
		sendMessage(messages);
		
		return;
	}
	
}
