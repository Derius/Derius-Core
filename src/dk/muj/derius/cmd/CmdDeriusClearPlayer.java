package dk.muj.derius.cmd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.cmd.arg.ARSkill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class CmdDeriusClearPlayer extends DeriusCommand
{
	public CmdDeriusClearPlayer()
	{
		this.addRequiredArg("player");
		this.addOptionalArg("skill", "all skills");
		this.addOptionalArg("force it", "no");
		
		this.setDesc("Resets all the skills/the skill of said player to 0.");
		
		super.addRequirements(ReqHasPerm.get(Perm.CLEAR_PLAYER.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public void perform()
	{
		String force = this.arg(2);
		String forceYes = "Yes, I want to force this";
		if(!force.equals(forceYes))
			return;
		
		MPlayer target = this.arg(0, ARMPlayer.getAny());
		if(target == null) return;
		
		Skill skill = this.arg(1,ARSkill.get(), null);
		List<Skill> skillList = new ArrayList<Skill>();
		
		if (skill == null)
			skillList.addAll(Skill.GetAllSkills());
		else
			skillList.add(skill);
		
		for(Skill name: skillList)
		{
			Field field = null;
			HashMap<Integer,Long> value = null;
			//Reflect my way into the data
			try
			{
				field = target.getClass().getDeclaredField("exp");
				field.setAccessible(true);
				value = (HashMap<Integer,Long>) field.get(target);
				
				value.put(name.getId(), 0L);
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
    	return MConf.get().innerAliasesDeriusClearPlayer;
    }
}
