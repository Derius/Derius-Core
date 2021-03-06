package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.ArgSetting;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPerm;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.comparator.SkillComparatorLvl;

public class CmdDeriusInspect extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusInspect()
	{
		// Args
		this.addArg(getPlayerYou()).setDesc("The player to inspect");
		this.addArg(ArgSetting.getPage());
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(DeriusPerm.INSPECT.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Args
		DPlayer dplayer = this.readArg(dsender);
		int pageHumanBased = this.readArg();
		
		if (dplayer != dsender && DeriusPerm.INSPECT_OTHERS.has(dsender.getSender(), true)) return;
		
		String title =  (dplayer == dsender) ? Txt.parse("<green>%s", DLang.get().getYourSkills()) 
				: Txt.parse("%s's <green>%s", dplayer.getDisplayName(dsender), DLang.get().getSkills());

		// Create Pager
		final List<Skill> skills = new ArrayList<Skill>(DeriusAPI.getAllSkills());
		Collections.sort(skills, SkillComparatorLvl.get(dplayer));
		final Pager<Skill> pager = new PagerSimple<Skill>(skills, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, title, new Stringifier<Skill>()
		{
			@Override
			public String toString(Skill skill, int index)
			{
				return Txt.parse("%s: %s", skill.getDisplayName(dplayer), dplayer.getLvlStatus(skill).toString());
			}
				
		});
		
		// Send Message
		sendMessage(messages);
		
		return;
	}
	
}
