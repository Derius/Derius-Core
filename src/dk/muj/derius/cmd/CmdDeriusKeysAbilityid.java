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
	public CmdDeriusKeysAbilityid()
	{
		this.addOptionalArg("skill", "all skills");
		
		this.setDesc("Shows you the ability Id of a list of abilities.");
		
		this.addRequirements(ReqHasPerm.get(Perm.KEYS_ABILITYID.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> list = new ArrayList<String>();
		
		//Args
		Skill skill = this.arg(0, ARSkill.get(), null);
		
		// Which abilities should be shown
		if(skill == null)
			for(Ability ability: Ability.GetAllAbilities())
				list.add(Txt.parse("<red>")+ability.getId() +" "+ ability.getName());
		else
			for(Ability ability: skill.getAllAbilities())
				list.add(Txt.parse("<red>")+ability.getId() +" "+ ability.getName());
		
		msender.msg(list);
	}
	
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusKeyAbilityId;
    }
}
