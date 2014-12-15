package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

import dk.muj.derius.ChatUtil;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;

public class CmdDeriusDebugTitle extends DeriusCommand
{

	public CmdDeriusDebugTitle()
	{
		super.setDesc("test titles");
		super.addRequiredArg("activate/deactive/up");
		super.addRequiredArg("id");
		
		super.addAliases("title");
		super.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform()
	{
		Integer id = super.arg(1, ARInteger.get());
		if(null == id)	return;
		String arg = this.arg(0).toLowerCase();
		switch(arg)
		{
			case "activate" : 
				Ability a1 = Ability.GetAbilityById(id);
				if(a1 != null)
				{
				ChatUtil.msgAbilityActivate(msender, a1);
				break;
				}
			case "deactivate" : 
				Ability a2 = Ability.GetAbilityById(id);
				if(a2 != null)
				{
				ChatUtil.msgAbilityDeactivate(msender, a2);
				break;
				}
			case "up" : 
				Skill skill = Skill.GetSkillById(id);
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
