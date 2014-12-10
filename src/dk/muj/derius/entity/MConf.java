package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.WorldException;

public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// -------------------------------------------- //
	// MESSAGES
	// -------------------------------------------- //
	
	/**
	 * This Message get's shown to the Player when he attempts to use his ability,
	 * but the Cooldown time has not yet been reached.
	 */
	public String abilityCooldownMsg = "<green>[DERIUS] <i>You are still exhausted. You will be ready again in <lime>%s <i>seconds!";
	
	/**
	 * This Message get's shown to the Player when he activated an ability.
	 */
	public String abilityActivatedMsg = "<green>[DERIUS] <i>You activated <lime>%s";
	
	/**
	 * This Message get's shown to the Player when an ability gets deactivated.
	 */
	public String abilityDeactivatedMsg = "<green>[DERIUS] <i>The ability <lime>%s <i>ran out";
	
	/**
	 * This is the Color that get's added  to the skill, when the player is able to learn it.
	 */
	public String canPlayerLearnSkillColorYes = "<aqua>";
	
	/**
	 * This is the Color that get's added  to the skill, when the player is NOY able to learn it.
	 */
	public String canPlayerLearnSkillColorNo = "<grey>";
	
	/**
	 * This is the Color that get's added  to the ability, when the player is able to learn it.
	 */
	public String canPlayerUseAbilityColorYes = "<pink>";
	
	/**
	 * This is the Color that get's added  to the skill, when the player is NOT able to learn it.
	 */
	public String canPlayerUseAbilityColorNo = "<grey>";
	
	/**
	 * This is the message that gets sent when asked for the LvlStatus.getString.
	 */
	public String LvlStatus = "<navy>LVL: <lime>%s  <navy>XP: <lime>%s<yellow>/<lime>%s";
	
	// -------------------------------------------- //
	// SKILL SETTINGS
	// -------------------------------------------- //
	
	/**
	 * The maximum amount of skills you can specialize in
	 */
	public int specialisationMax = 3;
	
	/**
	 * The maximum level you can reach without specialization
	 */
	public int softCap = 1000;
	/**
	 * The maximum level you can reach with specialization
	 */
	public int hardCap = 2000;
	
	/**
	 * The amount of millis a player will have to wait
	 * before being able to change specialisation again
	 * this counts for both learning & unlearning
	 */
	public long specialisationCooldown = Txt.millisPerDay*3;
	
	/**
	 * The skills which you can't specialize in.
	 */
	public List<Integer> specialisationBlacklist = new ArrayList<Integer>();
	
	/**
	 * The skills you are automatically specialized in.
	 */
	public List<Integer> specialisationAutomatic = new ArrayList<Integer>();
	
	// -------------------------------------------- //
	// COMMAND ALIASES
	// -------------------------------------------- //
	public List<String> outerAliasesDerius = MUtil.list("d", "derius");
	
	public List<String> innerAliasesDeriusSkill = MUtil.list("s", "skill");
	public List<String> innerAliasesDeriusList = MUtil.list("l", "list");
	public List<String> innerAliasesDeriusInspect = MUtil.list("i", "inspect");
	
	public List<String> innerAliasesDeriusSpecialise = MUtil.list("sp","specialise");
	public List<String> innerAliasesDeriusSpLearn = MUtil.list("learn");
	public List<String> innerAliasesDeriusSpUnlearn = MUtil.list("unlearn");
	public List<String> innerAliasesDeriusSpInfo = MUtil.list("info");
	
	// -------------------------------------------- //
	// FACTION SETTINGS
	// -------------------------------------------- //
	
	public boolean factionFlagUseSkillsDefaultValue = true;
	public boolean factionFlagUseSkillsEditableByUser = false;
	public boolean factionFlagUseSkillsVisibleByUser = false;
	
	public boolean factionFlagEarnDefaultValue = true;
	public boolean factionFlagEarnEditableByUser = false;
	public boolean factionFlagEarnVisibleByUser = false;
	
	public boolean factionFlagUseAbilitiesDefaultValue = true;
	public boolean factionFlagUseAbilitiesEditableByUser = false;
	public boolean factionFlagUseAbilitiesVisibleByUser = false;
	
	public boolean factionFlagOverrideWorldDefaultValue = false;
	public boolean factionFlagOverrideWorldEditableByUser = false;
	public boolean factionFlagOverrideWorldVisibleByUser = false;
	
	
	// -------------------------------------------- //
	// WORLD SETTINGS
	// -------------------------------------------- //
	
	public Map<Integer, WorldException> worldSkillsUse = new HashMap<Integer, WorldException>();
	public Map<Integer, WorldException> worldSkillsEarn = new HashMap<Integer, WorldException>();
	public Map<Integer, WorldException> worldAbilityUse = new HashMap<Integer, WorldException>();
}
