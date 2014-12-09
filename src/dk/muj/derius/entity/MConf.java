package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

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
	// COOLDOWN SETTINGS
	// -------------------------------------------- //
	
	/**
	 * This Message get's shown to the Player when he attempts to use his ability,
	 * but the Cooldown time has not yet been reached.
	 */
	public String abilityCooldownMsg = "<green>[DERIUS]<i>You are still exhausted. You will be ready again in <lime>%s <i>seconds!";
	
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
