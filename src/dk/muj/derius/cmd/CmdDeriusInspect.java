package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
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
			msgLines.add("<bold><under>Your Skills");
		}
		else
		{
			msgLines.add(Txt.parse("<bold><under>%s's Skills", mplayer.getName()));
		}
		
		msgLines.add("");
		
		// Evaluates if the user has leveled the skill and adds it to the List
		for (Skill skill: Skills.GetAllSkills())
		{
			int currentLvl = mplayer.getLvlStatus(skill.getId()).getLvl();
			if( currentLvl <= 1)
			{
				msgLines.add(Txt.parse("<i>%s	: <g>%s", skill.getName(), currentLvl));
			}
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
