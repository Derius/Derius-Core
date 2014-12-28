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

// Shows you a list of all the available skills (color coded for state) and a short description of them.
// Color code of skill: grey = locked, you can't learn it | aqua = You have started learning it and are on some level
public class CmdDeriusList extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusList()
	{
		this.addOptionalArg("player", "you");
		
		this.setDesc("lists skills");
		
		this.addRequirements(ReqHasPerm.get(Perm.LIST.node));
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
		msgLines.add(Txt.titleize("Skills")); // Titel
		
		// Put the skill into the list, colored accordingly to players ability to learn them.
		for (Skill skill: Skill.GetAllSkills())
		{
			// Example Output (before before applying the colors): "<aqua>Mining: <i>Makes you better at mining."
			msgLines.add(Txt.parse("%s: <i>%s", skill.getDisplayName(msender), skill.getDescription()));
		}
		
		// Send Message
		this.msg(msgLines);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusList;
    }
}
