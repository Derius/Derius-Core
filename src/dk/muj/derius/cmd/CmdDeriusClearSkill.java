package dk.muj.derius.cmd;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.skill.Skill;

public class CmdDeriusClearSkill extends DeriusCommand
{
	public CmdDeriusClearSkill()
	{
		this.addRequiredArg("skill");
		this.addOptionalArg("force it", "no");
		
		this.setDesc("Resets all the players level data of this skill.");
		
		super.addRequirements(ReqHasPerm.get(Perm.CLEAR_SKILL.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public void perform()
	{
		String force = this.arg(1);
		String forceYes = "Yes, I want to force this";
		if(!force.equals(forceYes))
			return;
		
		Skill skill = this.arg(1,ARSkill.get());
		
		for(MPlayer target: MPlayerColl.get().getAll())
		{
			Field field = null;
			HashMap<Integer,Long> value = null;
			//Reflect my way into the data
			try
			{
				field = target.getClass().getDeclaredField("exp");
				field.setAccessible(true);
				value = (HashMap<Integer,Long>) field.get(target);
				
				value.put(skill.getId(), 0L);
				field.set(target, value);
			}
			catch (NoSuchFieldException e)		{	e.printStackTrace();	}
			catch (SecurityException e)			{	e.printStackTrace();	}
			catch (IllegalArgumentException e)	{	e.printStackTrace();	}
			catch (IllegalAccessException e)	{	e.printStackTrace();	}
		}
		
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusClearSkill;
    }
}
