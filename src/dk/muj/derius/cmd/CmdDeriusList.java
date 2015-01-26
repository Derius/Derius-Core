package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class CmdDeriusList extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusList()
	{
		super.addOptionalArg("player", "you");
		super.addOptionalArg("page", "1");
		
		super.addRequirements(ReqHasPerm.get(Perm.LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		MPlayer mplayer = this.arg(0, ARMPlayer.getAny(), msender);
		if (mplayer == null) return;
		Integer pageHumanBased = this.arg(1, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Pager
		final List<Skill> skills = Skill.getAllSkills();
		final PagerSimple<Skill> pager = new PagerSimple<Skill>(skills, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, "List of skills", new Stringifier<Skill>() {
			@Override
			public String toString(Skill skill)
			{
				return Txt.parse("%s: %s", skill.getDisplayName(msender), skill.getDescription());
			}
		});
		
		// Send Message
		sendMessage(messages);
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusList;
    }
	
}
