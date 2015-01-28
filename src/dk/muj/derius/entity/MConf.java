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
	// TIMING SETTINGS
	// -------------------------------------------- //
	
	public double timingMax = 4.999999;
	
	// -------------------------------------------- //
	// SKILL SETTINGS
	// -------------------------------------------- //
	
	/**
	 * The maximum amount of skills you can specialise in
	 */
	public int specialisationMax = 3;
	
	/**
	 * The maximum level you can reach without specialisation
	 */
	public int softCap = 1000;
	/**
	 * The maximum level you can reach with specialisation
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
	
	public Map<String, WorldExceptionSet> worldSkillsEarn = new HashMap<String, WorldExceptionSet>();
	public Map<String, WorldExceptionSet> worldAbilityUse = new HashMap<String, WorldExceptionSet>();
	
}
