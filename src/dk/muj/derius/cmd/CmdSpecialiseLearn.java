package dk.muj.derius.cmd;

import java.util.List;

import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.skill.Skill;

public class CmdSpecialiseLearn extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdSpecialiseLearn()
	{
		super.addRequiredArg("skill");
		super.setDesc("specialises in said skill");
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Skill skill = super.arg(0, ARSkill.get());
		if(skill == null) return;
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesSpecialiseLearn;
    }
}
