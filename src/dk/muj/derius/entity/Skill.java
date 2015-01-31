package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.lambda.LvlStatus;
import dk.muj.derius.lambda.LvlStatusCalculator;
import dk.muj.derius.lambda.LvlStatusDefault;
import dk.muj.derius.req.Req;
import dk.muj.derius.util.SkillUtil;

public class Skill extends Entity<Skill>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean enabled = true;
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	
	private String name;
	public String getName() { return name; }
	public void setName(String newName) { this.name = newName; }
	
	private String desc = "";
	public String getDescription() { return desc; }
	public void setDescription(String newDescription) { this.desc = newDescription; }
	
	private List<String> earnExpDescs = new CopyOnWriteArrayList<String>();
	public List<String> getEarnExpDescs() { return new ArrayList<>(earnExpDescs); }
	public void setEarnExpDescs(List<String> descs) { this.earnExpDescs = descs; }
	public void addEarnExpDescs(String desc) { this.earnExpDescs.add(desc); }
	
	private boolean spAutoAssigned = false;
	private boolean spBlacklisted = false;
	
	private WorldExceptionSet worldsEarn = new WorldExceptionSet();
	
	private transient List<Ability> passiveAbilities = new CopyOnWriteArrayList<Ability>();
	private transient List<Ability> activeAbilities = new CopyOnWriteArrayList<Ability>();
	
	private transient List<Req> seeRequirements = new CopyOnWriteArrayList<Req>();
	private transient List<Req> learnRequirements = new CopyOnWriteArrayList<Req>();
	private transient List<Req> specialiseRequirements = new CopyOnWriteArrayList<Req>();
	
	// Lambda
	private transient LvlStatusCalculator expToLvlStatus = (long exp) -> 	
	{	
		//This is the default algorithm
		int level = 0, nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; level++)
		{
			exp -= nextLvlExp;
			nextLvlExp *= 1.01;
		}
		
		return new LvlStatusDefault(level, Optional.of( (int) exp), Optional.of(nextLvlExp));
	};
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	// GSON
	public Skill() { constructed = true; };
	private boolean constructed = false;
	
	/**
	 * Registers an ability to our system.
	 * This should be done on server startup.
	 */
	public void register()
	{
		SkillRegisteredEvent event = new SkillRegisteredEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;

		if ( ! this.attached())
		{
			Skill old = SkillColl.get().get(this.getId(), false);
			if (old != null)
			{
				this.load(old);
				SkillColl.get().removeAtLocal(this.getId());
			}
			SkillColl.get().attach(this, this.getId());	
		}
		
		return;
	}

	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	/**
	 * Gets the name & description, as it would be displayed
	 * to the passed player
	 * @param {MPlayer} player to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayedDescription(MPlayer watcherObject)
	{
		return Txt.parse("%s: <i>%s", this.getDisplayName(watcherObject), this.getDescription());
	}
	
	/**
	 * Returns a colorcode name
	 * based on the players ability to learn stated skill or not.
	 * @param {MPlayer} The MPlayer we want to check for.
	 * @return {String} The colorcode for the txt.parse method.
	 */
	public String getDisplayName ( Object watcherObject)
	{
		MPlayer mplayer = MPlayer.get(watcherObject);
		if (mplayer.isSpecialisedIn(this))
		{
			return Txt.parse(MLang.get().skillColorPlayerIsSpecialised + this.getName());
		}
		else if (SkillUtil.canPlayerLearnSkill(mplayer, this, false))
		{
			return Txt.parse(MLang.get().skillColorPlayerCanUse + this.getName());
		}
		else
		{
			return  Txt.parse(MLang.get().skillColorPlayerCantUse + this.getName());
		}
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
	public final LvlStatus getLvlStatusFromExp(long exp) { return this.expToLvlStatus.apply(exp); }
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * This will change the algorithm
	 * @param {LvlStatusCalculator} The new algorithm to calculate levels for this skill
	 */
	public final void setLvlStatusAlgorithm(LvlStatusCalculator algorithm) { this.expToLvlStatus = algorithm; }
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * This will get the level calculation algorithm for this skill
	 * @param {LvlStatusCalculator} The new algorithm to calculate levels for this skill
	 */
	public final LvlStatusCalculator getLvlStatusAlgorithm() { return this.expToLvlStatus; }
	
	// -------------------------------------------- //
	// SPECIALISATION
	// -------------------------------------------- //
	
	/**
	 * @return {boolean} true if a player is automatically specialised in this skill
	 */
	public boolean isSpAutoAssigned()
	{
		return this.spAutoAssigned;
	}
	
	/**
	 * @return {boolean} true if this skill can not be specialised.
	 */
	public boolean isSpBlackListed()
	{
		return this.spBlacklisted;
	}
	
	// -------------------------------------------- //
	// FIELDS: REQUIREMENTS
	// -------------------------------------------- //
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to see the skill
	 * if they can't see the skill, they should not see it anywhere.
	 * @return {List<Req>} list of requirements to see the skill
	 */
	public List<Req> getSeeRequirements() { return this.seeRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to see the skill
	 * if they can't see the skill, they should not see it anywhere.
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to see the skill 
	 */
	public void setSeeRequirements(List<Req> requirements) { this.seeRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to see the skill
	 * if they can't see the skill, they should not see it anywhere.
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to see the skill
	 */
	public void addSeeRequirements(Req... requirements) { this.seeRequirements.addAll(Arrays.asList(requirements)); }
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to learn the skill (earn exp)
	 * @return {List<Req>} list of requirements to learn the skill
	 */
	public List<Req> getLearnRequirements() { return this.learnRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to learn the skill (earn exp)
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to learn the skill 
	 */
	public void setLearnRequirements(List<Req> requirements) { this.learnRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to learn the skill (earn exp)
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to learn the skill
	 */
	public void addLearnRequirements(Req... requirements) { this.learnRequirements.addAll(Arrays.asList(requirements)); }
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to specialise in the skill
	 * @return {List<Req>} list of requirements to specialise the skill
	 */
	public List<Req> getSpecialiseRequirements() { return this.specialiseRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to specialise in the skill
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to specialise the skill 
	 */
	public void setSpecialiseRequirements(List<Req> requirements) { this.specialiseRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to specialise in the skill
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to specialise the skill
	 */
	public void addSpecialiseRequirements(Req... requirements) { this.specialiseRequirements.addAll(Arrays.asList(requirements)); }
	
	// -------------------------------------------- //
	// FIELD: WORLDEXCEPTION
	// -------------------------------------------- //
	
	/**
	 * Gets an exception set for the worlds in which
	 * it is possible to use this ability.
	 * @return {WorldExceptionSet} worlds where you can use this ability
	 */
	public WorldExceptionSet getWorldsEarn()
	{
		return worldsEarn;
	}
	
	/**
	 * Sets the exception set for which worlds
	 * it is possible to use this ability.
	 * @param {WorldExceptionSet} worlds where you can use this ability
	 */
	public void setWorldsEarn(WorldExceptionSet worldsEarn)
	{
		this.worldsEarn = worldsEarn;
	}
	
	// -------------------------------------------- //
	// FIELD: ABILITIES
	// -------------------------------------------- //
	
	/**
	 * Gets the list of active abilities
	 * @return {List<Ability>} all active abilities related to this skill
	 */
	public List<Ability> getActiveAbilities() { return this.activeAbilities; }
	
	/**
	 * Gets the list of passive abilities
	 * @return {List<Ability>} all passive abilities related to this skill
	 */
	public List<Ability> getPassiveAbilities() { return this.passiveAbilities; }
	
	/**
	 * Gets the list of all abilities related to skill
	 * @return {List<Ability>} all abilities related to this skill
	 */
	public List<Ability> getAllAbilities()
	{
		List<Ability> ret = new ArrayList<Ability>();
		ret.addAll(this.getPassiveAbilities());
		ret.addAll(this.getActiveAbilities());
		return ret;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the skill. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {String} the skills unique id.
	 */
	public String getId() { if (constructed) return null; throw new UnsupportedOperationException("Skill#getId must be implemented"); }
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		return obj == this;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getId().hashCode();
		
		return result;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}
	
}
