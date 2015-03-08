package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.cmd.arg.ARSkill;

public class CmdDeriusKeysAbilityid extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysAbilityid()
	{
		this.addOptionalArg("skill", "all");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_ABILITYID.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		List<String> messages = new ArrayList<String>();
		List<Ability> abilities = new ArrayList<>();
		
		//Args
		Skill skill = this.arg(0, ARSkill.get(), null);
		
		if (skill == null)
		{
			abilities.addAll(DeriusAPI.getAllAbilities());
		}
		else
		{
			abilities.addAll(skill.getAbilities());
		}
		
		// Apply
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			messages.add(Txt.parse("<red>%s %s", ability.getId(), ability.getName()));
		}
		
		sendMessage(messages);
		
		return;
	}
	
}
