package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.util.ChatUtil;

public class CmdDeriusDebugTitle extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdDeriusDebugTitle()
	{
		super.addRequiredArg("activate/deactive/up");
		super.addRequiredArg("id");
		
		super.addAliases("title");
		super.addRequirements(ReqIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Integer id = super.arg(1, ARInteger.get());
		if(null == id)	return;
		String arg = this.arg(0).toLowerCase();
		switch(arg)
		{
			case "activate" : 
				Ability a1 = Ability.getAbilityById(id);
				if(a1 != null)
				{
					ChatUtil.msgAbilityActivate(msender, a1);
					break;
				}
			case "deactivate" : 
				Ability a2 = Ability.getAbilityById(id);
				if(a2 != null)
				{
					ChatUtil.msgAbilityDeactivate(msender, a2);
					break;
				}
			case "up" : 
				Skill skill = Skill.getSkillById(id);
				if(skill != null)
				{
					ChatUtil.msgLevelUp(msender, skill, msender.getLvl(skill));
					break;
				}
			default:
				msender.sendMessage("An error occured");
				break;
		}
	}
	
}
