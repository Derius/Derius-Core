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
	// MESSAGES
	// -------------------------------------------- //
	
	public String msgPrefix = "<green>[DERIUS]";
	
	public String msgAbilityCooldown = "<green>[DERIUS] <i>You are still exhausted. You will be ready again in <lime>%s <i>seconds!";
	public String msgAbilityActivated = "<green>[DERIUS] <i>You activated <lime>%s";
	public String msgAbilityDeactivated = "<green>[DERIUS] <i>The ability <lime>%s <i>ran out";
	

	public String colorAbilityCanPlayerUse = "<pink>";
	public String colorAbilityCanPlayerNotUse = "<grey>";
	
	public String colorSkillCanPlayerUse = "<aqua>";
	public String colorSkillCanPlayerNotUse = "<grey>";
	public String colorSkillIsPlayerSpecialised = "<gold>";

	public String msgSkillSpecialisationAutoAssigned = "<b>The skill %s <b> is automatically specialised";
	public String msgSkillSpecialisationBlackList = "<b>It is not possible to specialise in %s";
	public String msgSkillSpecialisationAlreadyHas = "<b>You already have %s";
	public String msgSkillSpecialisationTooMany = "<b>You cannot add %s <b>because you don't have room for more specialised";
	public String msgSkillSpecialisationAddSucceed = "<i>You are now specialised in %s";
	public String msgSkillSpecialisationError = "<b>An error occured while trying to specialise";
	public String msgSkillSpecialisationAlreadyHasNot = "<b>You do not have the Skill %s <b>specialised";
	public String msgSpecialisationRemoveSuceed = "<i>You are no longer specialised in %s";
	
	public String msgKeyAdd = "<i>You have succesfully registered the key %s to the ability %s.";
	public String msgKeyAddIsAlready = "<i>The key %s is already in use. Please use another one.";
	public String msgKeyAddInvalid = "<i>This Ability Id is not valid/not in use.";
	public String msgKeyRemoveInvalid = "<i>This key doesn't exist, so it cannot be removed";
	public String msgKeyRemoveSuccess = "<i>You have succesfully removed the key %s from your list of keys.";
	public String msgKeyClearWarning = "<i>Do you really want to clear all your entries?  Type <aqua> /d k c true";
	public String msgKeyClearSuccess = "<i>You have successfully cleared your chat activation keys.";
	
	public String msgToolPrepared = "<i>You prepared your %s";
	public String msgToolNotPrepared = "<i>You lowered your %s";
	
	public String msgLvlStatusFormat = "<navy>LVL: <lime>%s  <navy>XP: <lime>%s<yellow>/<lime>%s";
	public String msgAbilityDisplayedDescription = "%s: <i>%s";
	
	public int timeLvlUpFadeIn = 5;
	public int timeLvlUpStay = 60;
	public int timeLvlUpFadeOut = 5;
	
	public int timeAbilityActivateFadeIn = 5;
	public int timeAbilityActivateStay = 50;
	public int timeAbilityActivateFadeOut = 5;
	
	public int timeAbilityDeactivateFadeIn = 5;
	public int timeAbilityDeactivateStay = 50;
	public int timeAbilityDeactivateFadeOut = 5;
	
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
	 * The amount of millis a player will have to stand still
	 * before changing their specialisation
	 * this is so they don't do it by accident
	 */
	public long specialiseChangeStandStillSeconds = 60*1;
	
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
	public List<String> innerAliasesDeriusKeys = MUtil.list("k", "keys");
	public List<String> innerAliasesDeriusDebug = MUtil.list("debug");
	
	public List<String> innerAliasesDeriusSpLearn = MUtil.list("learn");
	public List<String> innerAliasesDeriusSpUnlearn = MUtil.list("unlearn");
	public List<String> innerAliasesDeriusSpInfo = MUtil.list("i","info");
	public List<String> innerAliasesDeriusSpList = MUtil.list("l","list");
	
	public List<String> innerAliasesDeriusKeysAdd = MUtil.list("a", "add");	
	public List<String> innerAliasesDeriusKeysRemove = MUtil.list("r", "remove");
	public List<String> innerAliasesDeriusKeysList = MUtil.list("l", "list");	
	public List<String> innerAliasesDeriusKeysClear = MUtil.list("c", "clear");	
	public List<String> innerAliasesDeriusKeyAbilityId = MUtil.list("id", "Abilityid");
	
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
	
	public Map<Integer, WorldException> worldSkillsEarn = new HashMap<Integer, WorldException>();
	public Map<Integer, WorldException> worldAbilityUse = new HashMap<Integer, WorldException>();
}
