package dk.muj.derius.entity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.lambda.TicksLastCalculator;
import dk.muj.derius.req.Req;
import dk.muj.derius.util.AbilityUtil;

public class Ability extends Entity<Ability>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private boolean enabled = true;
	public boolean isEnabled() { return enabled && this.getSkill().isEnabled(); }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	
	private transient AbilityType type;
	
	private String name;
	public String getName() { return name; }
	public void setName(String newName) { this.name = newName; }
	
	private String desc = "";
	public String getDescription() { return desc; }
	public void setDescription(String newDescription) { this.desc = newDescription; }
	
	private int ticksCooldown = 20*60*2;
	
	private WorldExceptionSet worldsUse = new WorldExceptionSet();
	
	protected transient List<Req> seeRequirements			= new CopyOnWriteArrayList<Req>();
	protected transient List<Req> activateRequirements	= new CopyOnWriteArrayList<Req>();
	
	// Lambda
	private transient TicksLastCalculator levelToTicks = (int level) ->
	{
		return (2 + level/50) * 20;
	};
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	// GSON
	public Ability() { constructed = true; };
	private boolean constructed = false;
	
	/**
	 * Registers an ability to our system.
	 * This should be done on server startup.
	 */
	public void register()
	{
		AbilityRegisteredEvent event = new AbilityRegisteredEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		
		if ( ! this.attached())
		{
			Ability old = AbilityColl.get().get(this.getId(), false);
			if (old != null)
			{
				this.load(old);
				AbilityColl.get().removeAtLocal(this.getId());
			}
			
			AbilityColl.get().attach(this, this.getId());
		}
		
		return;
	}
	
	// -------------------------------------------- //
	// ABILTY TYPE & CHECK
	// -------------------------------------------- //
	
	public enum AbilityType
	{
		/**
		 * Active skills last over a duration of time
		 */
		ACTIVE(),
		/**
		 * Passive abilities are activated once & don't last over time
		 */
		PASSIVE();
	}
	
	/**
	 * Gets the ability type (passive/active) of this ability
	 * @return {AbilityType} the type of this ability
	 */
	public AbilityType getType() { return this.type; }
	
	/**
	 * Sets the ability type (passive/active) of this ability
	 * @param {AbilityType} the new type of this ability
	 */
	protected void setType(AbilityType newType){ this.type = newType; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	/**
	 * Gets the name & description, as it would be displayed
	 * to the passed player
	 * @param {Object} player (id/uuid/mplayer/bukkit-player) to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayedDescription(Object watcherObject)
	{
		String name = this.getDisplayName(watcherObject);
		if (name == null) return null;
		return Txt.parse(MLang.get().abilityDisplayedDescription, name, this.getDescription());
	}
	
	/**
	 * Gets the name  as it would be displayed to the passed player
	 * @param {Object} player (id/uuid/mplayer/bukkit-player) to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayName(Object watcherObject)
	{
		MPlayer player = MPlayer.get(watcherObject);
		if (player == null) return null;
		String color = AbilityUtil.canPlayerActivateAbility(player, this, false) ? MLang.get().abilityColorPlayerCanUse : MLang.get().abilityColorPlayerCantUse;
		return color + this.getName();
	}
	
	// -------------------------------------------- //
	// TIME
	// -------------------------------------------- //

	/**
	 * Gets how many ticks this ability will last
	 * @param {int} the level to check for
	 * @return {int} amount of ticks, this ability would last.
	 */
	public int getDuration(int level)
	{
		return this.levelToTicks.calcDuration(level);
	}

	// Lambda
	/**
	 * Each ability can have a different way to calculate the cooldown time.
	 * We don't know it, but we store the level, which this is depending on.
	 * This will change the algorithm for this ability.
	 * @param algorithm
	 */
	public final void setDurationAlgorithm(TicksLastCalculator algorithm)
	{
		this.levelToTicks = algorithm;
	}
	
	/**
	 * Each ability can have a different way to calculate the cooldown time.
	 * We don't know it, but we store the level, which this is depending on.
	 * This will get the cooldown calculation algorithm for this ability.
	 * @return {TicksLastCalculator} The algorithm which is being used for this ability.
	 */
	public final TicksLastCalculator getTicksLastAlgorithm()
	{
		return this.levelToTicks;
	}
	
	// Cooldown
	/**
	 * Sets how many ticks the cooldown will last.
	 * @param {int} The ticks it will last
	 */
	public void setTicksCooldown(int ticks)
	{
		this.ticksCooldown = ticks;
	}
	
	/**
	 * Gets how many ticks the cooldown will last
	 * @return {int} amount of ticks, the cooldown will be.
	 */
	public int getCooldownTicks()
	{
		return this.ticksCooldown;
	}
	
	// -------------------------------------------- //
	// FIELD: WORLDEXCEPTION
	// -------------------------------------------- //
	
	/**
	 * Gets an exception set for the worlds in which
	 * it is possible to earn exp in this skill.
	 * @return {WorldExceptionSet} worlds where you can earn exp in this skill
	 */
	public WorldExceptionSet getWorldsUse()
	{
		return this.worldsUse;
	}
	
	/**
	 * Sets the exception set for which worlds
	 * it is possible to earn exp for this skill
	 * @param {WorldExceptionSet} worldexceptionset
	 */
	public void setWorldsEarn(WorldExceptionSet worldsUse)
	{
		this.worldsUse = worldsUse;
	}
	
	// -------------------------------------------- //
	// RESTRICTION
	// -------------------------------------------- //
	
	/**
	 * This will return the list of requirements
	 * that must be met in order for a player to see the ability
	 * if they can't see the skill, they should not see it anywhere.
	 * @return {List<Req>} list of requirements to see the ability
	 */
	public List<Req> getSeeRequirements() { return this.seeRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be met in order for a player to see the ability
	 * if they can't see the skill, they should not see it anywhere.
	 * @param {List<Req>} list of requirements to see the ability 
	 */
	public void setSeeRequirements(List<Req> requirements) { this.seeRequirements = requirements; }
	
	/**
	 * This will add to the list of requirements
	 * that must be met in order for a player to see the ability
	 * if they can't see the skill, they should not see it anywhere.
	 * @param {Req...} added requirements to see the ability
	 */
	public void addSeeRequirements(Req... requirements) { this.seeRequirements.addAll(Arrays.asList(requirements)); }
	
	/**
	 * This will give the list of requirements
	 * that must be met in order for a player to activate the ability
	 * @return {List<Req>} list of requirements to activate the ability
	 */
	public List<Req> getActivateRequirements() { return this.activateRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be met in order for a player to activate the ability
	 * @param {List<Req>} list of requirements to activate the ability
	 */
	public void setActivateRequirements(List<Req> requirements) { this.activateRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be met in order for a player to activate the ability
	 * @param {List<Req>} added requirements to activate the ability
	 */
	public void addActivateRequirements(Req... requirements) { this.activateRequirements.addAll(Arrays.asList(requirements)); }
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the skill associated with this ability
	 * @return {Skill} the skill associated with this ability
	 */
	public Skill getSkill() { throw new UnsupportedOperationException("Ability#getSkill must be implemented"); };
	
	/**
	 * Gets the id of the ability. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * @return {String} the abilities unique id.
	 */
	public String getId() { if (constructed) return null; throw new UnsupportedOperationException("Ability#getId must be implemented"); }
	
	/**
	 * Gets a description based on passed level
	 * example "Double drop. Chance for double drop is 10%"
	 * if someone with that level had 10% chance to double drop.
	 * @param {int} the level you want to test for
	 * @return {String} the actual string message
	 */
	public String getLvlDescriptionMsg(int lvl) { throw new UnsupportedOperationException("Skill#getId must be implemented"); };
	
	// Ability Execution methods
	/**
	 * This is the method called by Derius to run your ability. 
	 * It is similar to bukkits onEnable method.
	 * @param {MPlayer} the player to use the ability
	 * @param {Object} other parameter used in some abilities
	 * @return {Object} this object will be passed to onDeactivate for data transferring.
	 */
	public Object onActivate(MPlayer p, Object other) { return null; };
	
	/**
	 * This is the method called by Derius when your ability
	 * is deactivated. It is similar to bukkits onDisable method.
	 * @param {MPlayer} the player to stop using the ability
	 * @param {Object} object received from onActivate
	 */
	public void onDeactivate(MPlayer p, Object other) {};
	
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
