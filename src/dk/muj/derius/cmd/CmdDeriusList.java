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
		
		// Evaluates what color code the skill should have and adds it to the list
		for (Skill skill: Skills.GetAllSkills())
		{
			String colorCode;
			
			if (!skill.CanPlayerLearnSkill(mplayer))
			{
				colorCode = "<grey>";
			}
			else
			{
				colorCode = "<aqua>";
			}
			
			msgLines.add(Txt.parse("%s%s: <i>%s", colorCode, skill.getName(), skill.getDesc()));
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
