package dk.muj.derius.req.sp;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqAbilityToSkill;
import dk.muj.derius.req.util.ReqNoCmd;
import dk.muj.derius.req.util.ReqNoDefault;

public class ReqIsntSpecialised implements Req, ReqAbilityToSkill, ReqNoCmd, ReqNoDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsntSpecialised i = new ReqIsntSpecialised();
	public static ReqIsntSpecialised get() { return i; }
	private ReqIsntSpecialised() {}
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.HIGH;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(sender);
		if (mplayer == null) return false;
		return ! mplayer.isSpecialisedIn(skill);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse(MLang.get().specialisationHasAlready, skill.getDisplayName(sender));
	}

}
