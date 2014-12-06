package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
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
		
		//Args
		// player args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);
		if (mplayer == null) return;
		
		// Message construction
		msgLines.add("<bold><under>%s's Skills"); // Titel
		msgLines.addAll(Skills.getSkillsFromUser(mplayer)); // Not yet implemented, placeholder
		
		this.msg(msgLines);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusInspect;
    }
}
