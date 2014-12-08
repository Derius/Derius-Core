package dk.muj.derius.skill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

import dk.muj.derius.Const;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.integration.FactionIntegration;

public abstract class Skill
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<String> earnExpDesc = new ArrayList<String>();
	private List<Description> passiveAbilityDesc = new ArrayList<Description>();
	private List<Description> activeAbilityDesc = new ArrayList<Description>();
	//private List<Material> activationItems = new ArrayList<Material>();
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	/**
	 * Adds a description of how to earn exp in said skill
	 * @param {String} The description to add
	 */
	public void addEarnExpDesc (String desc) {	this.earnExpDesc.add(desc);	}
	
	/**
	 * Adds a description (with name) of a passive ability in this skill
	 * @param {Description} The description to add
	 */
	public void addPassiveAbilityDesc (String name, String desc) {	this.passiveAbilityDesc.add(new Description(name,desc));	}
	
	/**
	 * Adds a description (with name) of a active ability in this skill
	 * @param {Description} The description to add
	 */
	public void addActiveAbilityDesc (String name, String desc) {	this.activeAbilityDesc.add(new Description(name,desc));	}
	
	/**
	 * Gets a list of the descriptions to earn exp
	 * @return {String} the description
	 */
	public List<String> getEarnExpDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(String desc : earnExpDesc)
			descs.add(desc);
		return descs;
	}
	
	/**
	 * Gets a list of the descriptions (with name) of the passive abilities
	 * @return {Description} the description
	 */
	public List<String> getPassiveAbilityDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(Description desc : passiveAbilityDesc)
			descs.add(desc.toString());
		return descs;
	}
	
	/**
	 * Gets a list of the descriptions (with name) of the active abilities
	 * @return {Description} the description
	 */
	public List<String> getActiveAbilityDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(Description desc : activeAbilityDesc)
			descs.add(desc.toString());
		return descs;
	}
	
	// -------------------------------------------- //
	// LEVELS
	// -------------------------------------------- //
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * The level calculation algorithm can also be change later on.
	 * @param {long} the amount of exp to convert to level
	 * @return {int} The level equivalent to the amount of exp passed.
	 */
	public LvlStatus LvlStatusFromExp(long exp)
	{
		return Skill.DefaultExpToLvlStatus(exp);
	}

	private static LvlStatus DefaultExpToLvlStatus(long exp)
	{
		int level = 0;
		int nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; nextLvlExp *= 1.1)
		{
			exp = exp-nextLvlExp;
			level++;
		}
		return new LvlStatus(level,(int)exp,nextLvlExp);
	}
	
	// -------------------------------------------- //
	// ABILITIES
	// -------------------------------------------- //
	
	//Commented out, might use this later
	/**
	 * Gets a list of items to activate an ability
	 * 
	 * @return {List}
	 */
	/*
	public List<Material> getActivationItems()
	{
		return activationItems;
	}
	*/
	
	/**
	 * Passes in a material into the list of activationitems.
	 * @param {Material} the item that should be put into the list.
	 */
	/*
	public void addActivationItem(Material material) { this.activationItems.add(material); }
	*/
	
	/**
	 * Passes in an itemId into the list of activationitems.
	 * @param {int} the itemId of which material should be put into the list.
	 */
	/*
	@SuppressWarnings("deprecation")
	public void addActivationITem(int itemId) {  this.activationItems.add(Material.getMaterial(itemId)); }
	*/
	
	// -------------------------------------------- //
	// AREA RESTRICTION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not this skill can be used in said area
	 * @param {Location} the are you want to check for
	 * @return {boolean} true if the skill can be used
	 */
	public boolean CanSkillBeUsedInArea(Location loc)
	{
		if(FactionIntegration.EstablishIntegration())
		{
			Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));
			if(f != null)
				if(f.getFlag(Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD))
					return f.getFlag(Const.FACTION_FLAG_SKILLS_USE);
			
		}
		return MConf.get().worldSkillsUse.get(this.getId()).EnabledInWorld(loc.getWorld());
	}
	
	/**
	 * Tells whether or not experience can be earned for this skill in said area
	 * @param {Location} the are you want to check for
	 * @return {boolean} true if experience in this skill can be earned in the area
	 */
	public boolean CanSkillBeEarnedInArea(Location loc)
	{
		if(FactionIntegration.EstablishIntegration())
		{
			Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));
			if(f != null)
				if(f.getFlag(Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD))
					return f.getFlag(Const.FACTION_FLAG_SKILLS_EARN);
			
		}
		return MConf.get().worldSkillsEarn.get(this.getId()).EnabledInWorld(loc.getWorld());
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the skill. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {int} the skills unique id.
	 */
	public abstract int getId();
	
	/**
	 * Gets the name of the skill. This is seen by players.
	 * THis MUST be unique but can always be changed.
	 * @return {String} The skills name
	 */
	public abstract String getName();
	
	/**
	 * Gives a short description of the skill.
	 * Should not be more than one or two minecraft chat lines long
	 * @return {String} a short description of the skill
	 */
	public abstract String getDesc();
	
	/**
	 * Tells whether or not the player can learn said skill.
	 * The skill can have different reasons the player might not.
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can learn said skill
	 */
	public abstract boolean CanPlayerLearnSkill(MPlayer p);
	
	/**
	 * Gets a list of descriptions for the different abilities.
	 * But these should include level specific data.
	 * So this would include data like your double drop chance
	 * or the length of an active ability being activated (for that lvl).
	 * These string should be passed directly to a player under normal circumstances.
	 * @param {int} The level you want to get information about.
	 * @return {List<String>} description of abilities to the corresponding lvl.
	 */
	public abstract List<String> getAbilitiesDecriptionByLvl(int lvl);
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}
	
}
