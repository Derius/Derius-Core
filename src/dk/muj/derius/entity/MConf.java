package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

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
	
	public String msgSkillLvlUp ="<i>You leveled up to level <lime>%s<i> in <aqua>%s<i>.";
	public String msgExhausted = "<i> You are still exhausted. You will be ready again in %s";
	public String msgAbilityActivated = "<i>You activated %s";
	public String msgAbilityDeactivated = "<i>The ability %s <i>ran out";
	public String msgAbilityCantBeUsedInArea = "<b>Sorry, %s<b> can't be used in this area";
	
	public String msgSkillInfoPassiveAbilities ="<art>____[<aqua>Passive abilities<art>]____";
	public String msgSkillInfoActiveAbilities =	"<art>____[<aqua> Active abilities <art>]____";
	public String msgSkillInfoLvlStats ="<art>____[<aqua>Level stats<art>]____";
	
	public String colorAbilityCanPlayerUse = "<pink>";
	public String colorAbilityCanPlayerNotUse = "<gray>";
	
	public String colorSkillCanPlayerUse = "<aqua>";
	public String colorSkillCanPlayerNotUse = "<grey>";
	public String colorSkillIsPlayerSpecialised = "<gold>";
	
	public String msgInspectOthersInvalid = "<i>You don't have the Permissions to inspect others!";	

	public String msgSkillSpecialisationAutoAssigned = "<b>The skill %s <b> is automatically specialised";
	public String msgSkillSpecialisationBlackList = "<b>It is not possible to specialise in %s";
	public String msgSkillSpecialisationAlreadyHas = "<b>You already have %s";
	public String msgSkillSpecialisationTooMany = "<b>You don't have room for more specialisations";
	public String msgSkillSpecialisationAddSucceed = "<i>You are now specialised in %s";
	public String msgSkillSpecialisationError = "<b>An error occured while trying to specialise";
	public String msgSkillSpecialisationIsnt = "<b>You do not have the Skill %s <b>specialised";
	public String msgSkillSpecialisationRemoveSuceed = "<i>You are no longer specialised in %s";
	
	public String msgKeyAddSuccess = "<i>You have succesfully registered the key %s to the ability %s.";
	public String msgKeyAddIsAlready = "<i>The key %s is already in use. Please use another one.";
	public String msgKeyAddInvalid = "<i>This Ability Id is not valid/not in use.";
	public String msgKeyRemoveInvalid = "<i>This key doesn't exist, so it cannot be removed";
	public String msgKeyRemoveSuccess = "<i>You have succesfully removed the key %s from your list of keys.";
	public String msgKeyClearWarning = "<i>Do you really want to clear all your entries?  Type <aqua> /d k c true";
	public String msgKeyClearSuccess = "<i>You have successfully cleared your chat activation keys.";
	
	public String msgSettingsMsgChangeInvalid = "<i>The parameter %s is invalid as a Msg type.";
	public String msgSettingsMsgChangeSuccess = "<i>You have succesfully changed your messsage appearance to %s.";
			
	public String msgToolPrepared = "<i>You prepared your <h>%s";
	public String msgToolNotPrepared = "<i>You lowered your <h>%s";
	
	public String msgLvlStatusFormat = "<navy>LVL: <lime>%s  <navy>XP: <lime>%s<yellow>/<lime>%s";
	public String msgLvlStatusFormatMini = "<navy>LVL: <lime>%s";
	public String msgAbilityDisplayedDescription = "%s: <i>%s";

	// -------------------------------------------- //
	// TIMING SETTINGS
	// -------------------------------------------- //
	
	public int timeLvlUpFadeIn = 5;
	public int timeLvlUpStay = 60;
	public int timeLvlUpFadeOut = 5;
	
	public int timeAbilityActivateFadeIn = 5;
	public int timeAbilityActivateStay = 50;
	public int timeAbilityActivateFadeOut = 5;
	
	public int timeAbilityDeactivateFadeIn = 5;
	public int timeAbilityDeactivateStay = 50;
	public int timeAbilityDeactivateFadeOut = 5;
	
	public double timingMax = 4.999999;
	
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
	public List<String> innerAliasesDeriusSettings = MUtil.list("se", "settings");
	public List<String> innerAliasesDeriusClean = MUtil.list("clean");
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
	
	public List<String> innerAliasesDeriusSeMsgSet = MUtil.list( "msgtype");
	
	// -------------------------------------------- //
	// WORLD SETTINGS
	// -------------------------------------------- //
	
	public Map<Integer, WorldExceptionSet> worldSkillsEarn = new HashMap<Integer, WorldExceptionSet>();
	public Map<Integer, WorldExceptionSet> worldAbilityUse = new HashMap<Integer, WorldExceptionSet>();
	
}
