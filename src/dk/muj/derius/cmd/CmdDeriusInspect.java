package dk.muj.derius.cmd;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.PagerSimple;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.skill.LvlStatus;

public class CmdDeriusInspect extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusInspect()
	{
		this.addOptionalArg("player", "you");
		this.addOptionalArg("page", "1");
		
		this.addRequirements(ReqHasPerm.get(Perm.INSPECT.node));
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
		
		if (mplayer != msender && Perm.INSPECT_OTHERS.has(msender.getSender(),true)) return;
		
		String title = (mplayer == msender) ? Txt.titleize(Txt.parse("<green>Your Skills")) : Txt.titleize(Txt.parse("%s's <green>Skills", mplayer.getDisplayName(msender)));

		// Create Pager
		final Collection<? extends Skill> skills = SkillColl.getAllSkills();
		final PagerSimple<Skill> pager = new PagerSimple<Skill>(skills, sender);
		
		// Use Pager
		List<String> messages = pager.getPageTxt(pageHumanBased, title, new Stringifier<Skill>() {
			@Override
			public String toString(Skill skill)
			{
				LvlStatus status = mplayer.getLvlStatus(skill);
				return Txt.parse("%s: %s", skill.getDisplayName(msender), status.toString());
			}
		});
		
		// Send Message
		sendMessage(messages);
	}
	
}
