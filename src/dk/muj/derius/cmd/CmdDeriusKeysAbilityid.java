package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.skill.Skill;

public class CmdDeriusKeysAbilityid extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusKeysAbilityid()
	{
		super.addOptionalArg("skill", "all skills");
		
		super.addRequirements(ReqHasPerm.get(Perm.KEYS_ABILITYID.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> messages = new ArrayList<String>();
		
		//Args
		Skill skill = this.arg(0, ARSkill.get(), null);
		
		// Which abilities should be shown
		if (skill == null)
		{
			for (Ability ability : Ability.getAllAbilities())
			{
				messages.add(Txt.parse("<red>")+ability.getId() +" "+ ability.getName());
			}
		}
		else
		{
			for (Ability ability : skill.getAllAbilities())
				messages.add(Txt.parse("<red>")+ability.getId() +" "+ ability.getName());
		}
		
		sendMessage(messages);
	}
	
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeyAbilityId;
    }
	
}
