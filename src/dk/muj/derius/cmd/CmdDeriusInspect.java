package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.Skills;

// Shows you all the learned skills and the level for said player/yourself.
public class CmdDeriusInspect extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusInspect()
	{
		this.addOptionalArg("player", "you");
		
		this.setDesc("Shows you all the learned skills and the level for said player/yourself.");
		
		this.addRequirements(ReqHasPerm.get(Perm.INSPECT.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		// Args
		// Player args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);
		if (mplayer == null) return;
		
		// Message construction
		
		// Titel switch
		if (mplayer == msender)
		{
			msgLines.add(Txt.titleize("Your Skills"));
		}
		else
		{
			
			msgLines.add(Txt.titleize(Txt.parse("%s's Skills", mplayer.getDisplayName(msender))));
		}
		
		// Evaluates if the user has leveled the skill and adds it to the List
		for (Skill skill: Skills.GetAllSkills())
		{
			LvlStatus status = mplayer.getLvlStatus(skill);
			String skillColor;
			if(!mplayer.CanLearnSkill(skill))
			{
				skillColor = "<gray>";
			}
			else
			{
				skillColor = "<aqua>";
			}
			
			msgLines.add(Txt.parse("%s%s: %s", skillColor, skill.getName(), status));
		}
		
		// Send Message
		this.msg(msgLines);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusInspect;
    }
}
