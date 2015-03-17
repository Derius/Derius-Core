
package dk.muj.derius.cmd;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.skill.Skill;

public class CmdDeriusList extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusList()
	{
		// Args
		this.addOptionalArg("page", "1");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.LIST.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		int pageHumanBased = this.arg(ARInteger.get(), 1);
		
		// Create Pager
		final Collection<? extends Skill> skills = DeriusAPI.getAllSkills();
		final PagerSimple<Skill> pager = new PagerSimple<Skill>(skills, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, "List of skills", (skill, index) ->
		{
				return Txt.parse("%s: <i>%s", skill.getDisplayName(dsender), skill.getDesc());
		});
		
		// Send Message
		sendMessage(messages);
		
		return;
	}
	
}
