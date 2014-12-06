package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skills;

// Shows you a list of all the available skills (color coded for state) and a short description of them.
// Color code of skill: grey = locked, you can't learn it | red = Haven't started to learn, on lvl 0 | green = You have started learning it and are on some level
public class CmdDeriusList extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusList()
	{
		this.addOptionalArg("player", "you");
		
		this.setDesc("Shows you a list of all the available skills and a short description of them");
		
		this.addRequirements(ReqHasPerm.get(Perm.LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<String> msgLines = new ArrayList<String>();
		
		//Args
		// player args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);
		if (mplayer == null) return;
		
		// Message construction
		msgLines.add("<bold><under>Skills"); // Titel
		msgLines.addAll(Skills.getSkillsByAvailability(mplayer)); // Not yet implemented, placeholder
		
		this.msg(msgLines);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusList;
    }
}
