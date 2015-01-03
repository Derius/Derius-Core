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

// Shows you all the learned skills and the level for said player/yourself.
// Default Color code of skill: grey = locked, you can't learn it | aqua = You have started learning it and are on some level
public class CmdDeriusInspect extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusInspect()
	{
		this.addOptionalArg("player", "you");
		
		this.setDesc("inspects player");
		
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
			msgLines.add(Txt.titleize("<green>Your Skills"));
		}
		else
		{
			
			msgLines.add(Txt.titleize(Txt.parse("%s's <green>Skills", mplayer.getDisplayName(msender))));
		}
		
		// Evaluates if the user has leveled the skill and adds it to the List
		for (Skill skill: Skill.getAllSkills())
		{
			LvlStatus status = mplayer.getLvlStatus(skill);
			
			// Example Output (before applying the colors): "<aqua>Mining: <navy>LVL: <lime>1 <navy>XP: <lime>120<yellow>/<lime>5000"
			msgLines.add(Txt.parse("%s: %s", skill.getDisplayName(msender), status.toString()));
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
