package dk.muj.derius.entity;

import java.util.List;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// TIMING SETTINGS
	// -------------------------------------------- //
	
	public int debugLevel = -1;
	
	public double timingMax = 4.999999;
	
	// -------------------------------------------- //
	// SKILL SETTINGS
	// -------------------------------------------- //
	
	public int baseSpSlot = 3;
	
	/**
	 * The amount of millis a player will have to wait
	 * before being able to change specialisation again
	 * this counts for both learning & unlearning
	 */
	public long specialisationCooldown = TimeUnit.MILLIS_PER_DAY*3;
	
	/**
	 * The amount of millis a player will have to stand still
	 * before changing their specialisation
	 * this is so they don't do it by accident
	 */
	public long specialiseChangeStandStillSeconds = 60*1;
	
	/**
	 * Amount of millis a player must stand still
	 * toi be considered standing still (not walking)
	 */
	public long millisToStandStill = 500;
	
	// -------------------------------------------- //
	// STAMINA SETTINGS
	// -------------------------------------------- //
	
	/**
	 * Stamina can't go higher than this without a bonus
	 */
	public double staminaMax = 200.0;
	
	/**
	 * The stamina you get per minute
	 */
	public double staminaPerMinute = 20.0;
	
	/**
	 * How long the stamina scoreboard stays
	 */
	public int staminaBoardStay = 200;

	public int staminaRegenSeconds = 3*60;
	
	public double staminaStandStillMultiplier = 2.0;
	public double staminaSprintMultiplier = 0.0;
	public double staminaSneakMultiplier = 1.5;
	public double staminaWalkMultiplier = 0.8;
	
	public double staminaBlockSprintIfLower = 50;
	
	// -------------------------------------------- //
	// COMMAND ALIASES
	// -------------------------------------------- //
	
	public List<String> outerAliasesDerius = MUtil.list("d", "derius");
	
	public List<String> innerAliasesSkill = MUtil.list("s", "skill");
	public List<String> innerAliasesSkills = MUtil.list("skills");
	public List<String> innerAliasesDeriusList = MUtil.list("l", "list");
	public List<String> innerAliasesDeriusInspect = MUtil.list("i", "inspect");
	public List<String> innerAliasesDeriusSpecialise = MUtil.list("sp","specialise");
	public List<String> innerAliasesDeriusKeys = MUtil.list("k", "keys");
	public List<String> innerAliasesDeriusClean = MUtil.list("clean");
	public List<String> innerAliasesDeriusDebug = MUtil.list("debug");
	public List<String> innerAliasesDeriusSetStamina = MUtil.list("setstamina");
	
	public List<String> innerAliasesDeriusSpLearn = MUtil.list("learn");
	public List<String> innerAliasesDeriusSpUnlearn = MUtil.list("unlearn");
	public List<String> innerAliasesDeriusSpInfo = MUtil.list("i","info");
	public List<String> innerAliasesDeriusSpList = MUtil.list("l","list");
	
	public List<String> innerAliasesDeriusKeysAdd = MUtil.list("a", "add");	
	public List<String> innerAliasesDeriusKeysRemove = MUtil.list("r", "remove");
	public List<String> innerAliasesDeriusKeysList = MUtil.list("l", "list");	
	public List<String> innerAliasesDeriusKeysClear = MUtil.list("c", "clear");	
	public List<String> innerAliasesDeriusKeyAbilityId = MUtil.list("id", "Abilityid");
	
	public List<String> innerAliasesDeriusScoreboard = MUtil.list("sc", "scoreboard");
	public List<String> innerAliasesDeriusScShow = MUtil.list("show");
	public List<String> innerAliasesDeriusScKeep = MUtil.list("keep");

}
