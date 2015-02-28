package dk.muj.derius.req.sp;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqAbilityToSkill;
import dk.muj.derius.req.util.ReqNoDefault;

public class ReqIsntBlacklisted implements Req, ReqAbilityToSkill, ReqNoDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsntBlacklisted i = new ReqIsntBlacklisted();
	public static ReqIsntBlacklisted get() { return i; }
	private ReqIsntBlacklisted() {}
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.LOW;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //

	@Override
	public boolean apply(DPlayer dplayer, Skill skill)
	{
		return ! skill.isSpBlackListed();
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Skill skill)
	{
		return Txt.parse(MLang.get().specialisationBlacklisted, skill.getDisplayName(dplayer));
	}

}
