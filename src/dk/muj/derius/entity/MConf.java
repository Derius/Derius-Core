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
	 * The skills which you can't specialise int
	 */
	public List<Integer> specialisationBlacklist = new ArrayList<Integer>();
	
	/**
	 * The skills you are automatically specialised in.
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
	public boolean factionFlagUseEditableByUser = false;
	public boolean factionFlagUseVisibleByUser = false;
	
	public boolean factionFlagEarnSkillsDefaultValue = true;
	public boolean factionFlagEarnEditableByUser = false;
	public boolean factionFlagEarnVisibleByUser = false;
	
	public boolean factionFlagOverrideWorldDefaultValue = false;
	public boolean factionFlagOverrideWorldEditableByUser = false;
	public boolean factionFlagOverrideWorldVisibleByUser = false;
	
	
	// -------------------------------------------- //
	// WORLD SETTINGS
	// -------------------------------------------- //
	
	public Map<Integer, WorldException> worldSkillsUse = new HashMap<Integer, WorldException>();
	public Map<Integer, WorldException> worldSkillsEarn = new HashMap<Integer, WorldException>();
}
