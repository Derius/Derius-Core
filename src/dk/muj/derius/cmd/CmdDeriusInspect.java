package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.cmd.arg.ARDPlayer;
import dk.muj.derius.comparator.SkillComparatorLvl;
import dk.muj.derius.entity.skill.SkillColl;

public class CmdDeriusInspect extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusInspect()
	{
		// Args
		this.addOptionalArg("player", "you");
		this.addOptionalArg("page", "1");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.INSPECT.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Args
		DPlayer dplayer = this.arg(0, ARDPlayer.getAny(), dsender);
		int pageHumanBased = this.arg(1, ARInteger.get(), 1);
		
		if (dplayer != dsender && Perm.INSPECT_OTHERS.has(dsender.getSender(),true)) return;
		
		String title =  (dplayer == dsender) ? Txt.parse("<green>Your Skills") 
				: Txt.parse("%s's <green>Skills", dplayer.getDisplayName(dsender));

		// Create Pager
		final List<Skill> skills = new ArrayList<>(SkillColl.getAllSkills());
		Collections.sort(skills, SkillComparatorLvl.get(dplayer));
		final Pager<Skill> pager = new PagerSimple<Skill>(skills, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, title, 
				skill -> Txt.parse("%s: %s", skill.getDisplayName(dsender), dsender.getLvlStatus(skill).toString()) );
		
		// Send Message
		sendMessage(messages);
		
		return;
	}
	
}
