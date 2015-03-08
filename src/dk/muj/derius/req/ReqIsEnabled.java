package dk.muj.derius.req;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.req.util.ReqNoDefault;

public class ReqIsEnabled implements Req, ReqNoDefault
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsEnabled i = new ReqIsEnabled();
	public static ReqIsEnabled get() { return i; }
	private ReqIsEnabled() {}
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.NEVER;
	}

	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(DPlayer dplayer, Skill skill)
	{
		return skill.isEnabled();
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Skill skill)
	{
		return Txt.parse(DLang.get().getSkillDisabled().replaceAll("{skill}", skill.getDisplayName(dplayer)));
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //
	
	@Override
	public boolean apply(DPlayer dplayer, Ability ability)
	{
		return ability.isEnabled();
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Ability ability)
	{
		return Txt.parse(DLang.get().getAbilityDisabled().replaceAll("{ability}", ability.getDisplayName(dplayer)));
	}

}
