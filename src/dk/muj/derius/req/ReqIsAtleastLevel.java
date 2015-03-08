package dk.muj.derius.req;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.req.util.ReqAbilityToSkill;
import dk.muj.derius.req.util.ReqNoDefault;

public class ReqIsAtleastLevel implements Req, ReqAbilityToSkill, ReqNoDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsAtleastLevel get(int level) { return new ReqIsAtleastLevel(level); }
	private ReqIsAtleastLevel(int level) { this.level = level; }
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.NORMAL;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final int level;
	public int getlevel() { return this.level; }
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //
	
	@Override
	public boolean apply(DPlayer dplayer, Skill skill)
	{
		return (dplayer.getLvl(skill) >= level);
	}

	@Override
	public String createErrorMessage(DPlayer dplayer, Skill skill)
	{
		return Txt.parse(DLang.get().getSkillLevelIsTooLow().replaceAll("{level}", String.valueOf(this.getlevel())).replaceAll("{skill}", skill.getDisplayName(dplayer)));
	}

}
