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
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.comparator.SkillComparatorLvl;

public class CmdDeriusSpList  extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpList()
	{
		this.addArg(getPlayerYou());
		this.addArg(ArgSetting.getPage());
		
		this.addRequirements(ReqHasPerm.get(DeriusPerm.SPECIALISATION_LIST.getNode()));
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

		if (dplayer != dsender && ! DeriusPerm.SPECIALISATION_LIST_OTHER.has(sender, true)) return;
		
		final List<Skill> skills = new ArrayList<>(dplayer.getSpecialisedSkills());
		Collections.sort(skills, SkillComparatorLvl.get(dplayer));
		final Pager<Skill> pager = new PagerSimple<Skill>(skills, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, String.format("%s's <i>Specialisations", dplayer.getDisplayName(dsender)), new Stringifier<Skill>()
		{
			@Override
			public String toString(Skill skill, int index)
			{
				return Txt.parse("%s: %s", skill.getDisplayName(dplayer), dplayer.getLvlStatus(skill).toString());
			}
				
		});
		
		// Send message
		sendMessage(messages);
		
		return;
	}

}
