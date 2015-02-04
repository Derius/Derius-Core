package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.Derius;
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
	
	private String skillName;
	public String getName() { return skillName; }
	public void setName(String newName) { this.skillName = newName; }
	
	private String desc = "";
	public String getDescription() { return desc; }
	public void setDescription(String newDescription) { this.desc = newDescription; }
	
	private List<String> earnExpDescs = new CopyOnWriteArrayList<String>();
	public List<String> getEarnExpDescs() { return new ArrayList<>(earnExpDescs); }
	public void setEarnExpDescs(List<String> descs) { this.earnExpDescs = descs; }
	public void addEarnExpDescs(String desc) { this.earnExpDescs.add(desc); }
	
	private Material icon = Material.AIR;
	
	private boolean spAutoAssigned = false;
	private boolean spBlacklisted = false;
	
	private double multiplier = 1.0;
	public double getMultiplier() { return this.multiplier; }
	public void setMutiplier(double multiplier) { this.multiplier = multiplier; }
	
	private WorldExceptionSet worldsEarn = new WorldExceptionSet();
	
	// Configuration
	private JsonObject configuration = new JsonObject();
	
	private transient List<Ability> passiveAbilities = new CopyOnWriteArrayList<Ability>();
	private transient List<Ability> activeAbilities = new CopyOnWriteArrayList<Ability>();
	
	private transient List<Req> seeRequirements = new CopyOnWriteArrayList<Req>();
	private transient List<Req> learnRequirements = new CopyOnWriteArrayList<Req>();
	private transient List<Req> specialiseRequirements = new CopyOnWriteArrayList<Req>();
	
	// Lambda, This is the default algorithm
	private transient LvlStatusCalculator expToLvlStatus = (long exp) -> 	
	{	
		int level = 0, nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; level++)
		{
			exp -= nextLvlExp;
			nextLvlExp *= 1.01;
		}
		
		return new LvlStatusDefault(level, Optional.of( (int) exp), Optional.of(nextLvlExp));
	};
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public Skill load(Skill that)
	{
		if (that == null || that == this) return that;
		
		this.enabled = that.enabled;
		this.spAutoAssigned = that.spAutoAssigned;
		this.spBlacklisted = that.spBlacklisted;
		
		if (that.skillName != null && ! that.skillName.equalsIgnoreCase("null")) this.skillName = that.skillName;
		if (that.desc != null && ! that.desc.equalsIgnoreCase("null")) this.desc = that.desc;
		if (that.earnExpDescs != null)this.earnExpDescs = that.earnExpDescs;
		if (that.icon != null) this.icon = that.icon;
		if (that.worldsEarn != null) this.worldsEarn = that.worldsEarn;
		
		return this;
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
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
	// CONFIGURATION
	// -------------------------------------------- //

	/**
	 * Reads a value from the skills custom configuration
	 * @param {String} name of the value
	 * @param {Class<T>} type of value
	 * @return {T} the value
	 */
	public <T> T readConfig(String field, Class<T> fieldType)
	{
		return Derius.get().gson.fromJson(this.configuration.get(field), fieldType);
	}
	
	/**
	 * Reads a value from the skills custom configuration
	 * @param {String} name of the value
	 * @param {TypeToken<T>} typetoken for generics
	 * @return {T} the value
	 */
	public <T> T readConfig(String field, TypeToken<T> typeToken)
	{
		return Derius.get().gson.fromJson(this.configuration.get(field), typeToken.getType());
	}
	
	/**
	 * Writes a value to the skills custom configuration
	 * @param {String} name of the value
	 * @param {Class<T>} type of value
	 * @return {T} the value
	 */
	public void writeConfig(String field, Object value)
	{
		this.configuration.add(field, Derius.get().gson.toJsonTree(value));
	}
	
	/**
	 * Writes a value to the skills custom configuration
	 * @param {String} name of the value
	 * @param {TypeToken<T>} typetoken for generics
	 * @return {T} the value
	 */
	public <T> void writeConfig(String field, Object value, TypeToken<T> typeToken)
	{
		this.configuration.add(field, Derius.get().gson.toJsonTree(value, typeToken.getType()));
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
	// FIELD: ICON
	// -------------------------------------------- //
	
	/**
	 * Gets the icon of the skill. This is showed in a chest GUI
	 * @return {Material} the skills
	 */
	public Material getIcon()
	{
		return this.icon;
	}
	
	/**
	 * Sets the icon for this skill shown in chest GUIs
	 * @param {Material} 
	 */
	public void setIcon(Material icon)
	{
		this.icon = icon;
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
	public String getId() { if (this.getClass().equals(Skill.class)) return null; throw new UnsupportedOperationException("Skill#getId must be implemented"); }
	
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
