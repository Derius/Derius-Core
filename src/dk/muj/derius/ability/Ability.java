package dk.muj.derius.ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.exceptions.IdAlreadyInUseException;
import dk.muj.derius.req.Req;
import dk.muj.derius.skill.Skill;

public abstract class Ability
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private AbilityType type;
	
	private String desc = "";
	private String name;
	
	private int ticksCooldown = 20*60*2;
	
	protected List<Req> seeRequirements= new CopyOnWriteArrayList<Req>();
	protected List<Req> activateRequirements= new CopyOnWriteArrayList<Req>();
	
	//A list of ability which we get from different sources.
	private static List<Ability> abilityList = new CopyOnWriteArrayList<Ability>();
	
	// Lambda
	TicksLastCalculator levelToTicks = (int level) ->
	{
		return (2 + level/50) * 20;
	};
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	/**
	 * Gets an ability from its id. 
	 * This is the best way to get an ability, since the id never changes.
	 * @param {int} The id of the ability you wanted to get.
	 * @return{Ability} The ability which has this id
	 */
	public static Ability getAbilityById(int abilityId)
	{
		for(Ability ability: Ability.abilityList)
		{
			if(ability.getId() == abilityId)
				return ability;
		}
		return null;
	}
	
	/**
	 * Gets an ability from its name.
	 * This should only be done by players. Since they don't know the id
	 * @param {String} The name of the ability you wanted to get.
	 * @return{Ability} The ability which starts with this name
	 */
	public static Ability getAbilityByName(String abilityName)
	{
		for(Ability ability: Ability.abilityList)
		{
			if(ability.getName().startsWith(abilityName))
				return ability;
		}
		return null;
	}
	
	/**
	 * Gets all registered abilities.
	 * @return {List<Ability>} all registered skills
	 */
	public static List<Ability> getAllAbilities()
	{
		return new ArrayList<Ability>(Ability.abilityList);
	}

	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	/**
	 * Registers an ability to our system.
	 * We will instantiate the correct fields.
	 * You still have to enforce the powers & general implementation.
	 * This should be done on server startup.
	 */
	public void register()
	{
		Ability ability = this;
		Object before = getAbilityById(ability.getId());
		if(before != null)
		{
			int id = ability.getId();
			try
			{
				throw new IdAlreadyInUseException("The id: "+ id + " is already registered by " + before.toString()
						+ " but "+ability.getName() + " is trying to use it");
			}
			catch (IdAlreadyInUseException e)
			{
				e.printStackTrace();
				return;
			}
		}
		abilityList.add(ability);
		AbilityRegisteredEvent event = new AbilityRegisteredEvent(ability);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	// -------------------------------------------- //
	// ABILTY TYPE & CHECK
	// -------------------------------------------- //
	
	/**
	 * Gets the ability type (passive/active) of this ability
	 * @return {AbilityType} the type of this ability
	 */
	public AbilityType getType()
	{
		return this.type;
	}
	
	/**
	 * Sets the ability type (passive/active) of this ability
	 * @param {AbilityType} the new type of this ability
	 */
	protected void setType(AbilityType newType)
	{
		this.type = newType;
	}
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	/**
	 * Sets the name of the ability
	 * @param {String} new name for this ability
	 */
	protected void setName(String str)
	{
		this.name = str;
	}
	
	/**
	 * Gets the name of the ability
	 * @param {String} name for this ability
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Sets the description of the ability
	 * @param {String} new description for this ability
	 */
	protected void setDescription(String str)
	{
		this.desc = str;
	}
	
	/**
	 * Gets the description of the ability
	 * @param {String} description for this ability
	 */
	public String getDescription()
	{
		return this.desc;
	}

	/**
	 * Gets the name & description, as it would be displayed
	 * to the passed player
	 * @param {MPlayer} player to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayedDescription(Object watcherObject)
	{
		MPlayer player = MPlayer.get(watcherObject);
		if (player == null) return null;
		String name = this.getDisplayName(watcherObject);
		return Txt.parse(MLang.get().abilityDisplayedDescription, name, this.getDescription());
	}
	
	/**
	 * Gets the name  as it would be displayed to the passed player
	 * @param {MPlayer} player to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayName(Object watcherObject)
	{
		MPlayer player = MPlayer.get(watcherObject);
		if (player == null) return null;
		String color = canPlayerActivateAbility(player) ? MLang.get().abilityColorPlayerCanUse : MLang.get().abilityColorPlayerCantUse;
		return color + this.getName();
	}
	
	// -------------------------------------------- //
	// TIME
	// -------------------------------------------- //

	/**
	 * Gets how many ticks this ability will last
	 * @return {int} amount of ticks, this ability would last.
	 */
	public int getTicksLast(int level)
	{
		return this.levelToTicks.apply(level);
	}

	// Lambda
	/**
	 * Each ability can have a different way to calculate the cooldowntime.
	 * We don't know it, but we store the level, which this is depending on.
	 * This will change the algorithm for this ability.
	 * @param algorithm
	 */
	public final void setTicksLastAlgorithm(TicksLastCalculator algorithm)
	{
		this.levelToTicks = algorithm;
	}
	
	/**
	 * Each ability can have a different way to calculate the cooldowntime.
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
	protected void setTicksCooldown(int ticks)
	{
		this.ticksCooldown = ticks;
	}
	
	/**
	 * Gets how many ticks the cooldown will last
	 * @return {int} amount of ticks, the cooldown will be.
	 */
	public int getCooldownTime()
	{
		return this.ticksCooldown;
	}
	
	// -------------------------------------------- //
	// RESTRICTION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player can use said ability.
	 * This is based on the ability requirements
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can use said ability
	 */
	public final boolean canPlayerActivateAbility(MPlayer p)
	{
		return this.canPlayerActivateAbility(p, false);
	}
	
	/**
	 * Tells whether or not the player can use said ability.
	 * This is based on the ability requirements
	 * @param {MPlayer} the player you want to check
	 * @param {boolean} true if error message should be sent
	 * @return {boolean} true if the player can use said ability
	 */
	public boolean canPlayerActivateAbility(MPlayer p, boolean verboseNot)
	{
		for (Req req : this.getActivateRequirements())
		{
			if ( ! req.apply(p.getSender(), this)) 
			{
				if (verboseNot) p.sendMessage(req.createErrorMessage(p.getSender(), this));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Tells whether or not the player can see said ability.
	 * This is based on the skill requirements
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can see said ability
	 */
	public final boolean canPlayerSeeAbility(MPlayer p)
	{
		return this.canPlayerSeeAbility(p, false);
	}
	
	/**
	 * Tells whether or not the player can see said ability.
	 * This is based on the skill requirements
	 * @param {MPlayer} the player you want to check
	 * @param {boolean} true if error message should be sent
	 * @return {boolean} true if the player can see said ability
	 */
	public boolean canPlayerSeeAbility(MPlayer p, boolean verboseNot)
	{
		for (Req req : this.getSeeRequirements())
		{
			if ( ! req.apply(p.getSender(), this)) 
			{
				if (verboseNot) p.sendMessage(req.createErrorMessage(p.getSender(), this));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to see the ability
	 * if they can't see the skill, they should not see it anywhere.
	 * @return {List<Req>} list of requirements to see the ability
	 */
	public List<Req> getSeeRequirements() { return this.seeRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to see the ability
	 * if they can't see the skill, they should not see it anywhere.
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to see the ability 
	 */
	public void setSeeRequirements(List<Req> requirements) { this.seeRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to see the ability
	 * if they can't see the skill, they should not see it anywhere.
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to see the ability
	 */
	public void addSeeRequirements(Req... requirements) { this.seeRequirements.addAll(Arrays.asList(requirements)); }
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to activate the ability
	 * @return {List<Req>} list of requirements to activate the ability
	 */
	public List<Req> getActivateRequirements() { return this.activateRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to activate the ability
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to activate the ability
	 */
	public void setActivateRequirements(List<Req> requirements) { this.activateRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to activate the ability
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to activate the ability
	 */
	public void addActivateRequirements(Req... requirements) { this.activateRequirements.addAll(Arrays.asList(requirements)); }
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the ability. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * @return {int} the abilities unique id.
	 */
	public abstract int getId();
	
	/**
	 * Gets a description based on passed level
	 * example "Double drop. Chance for double drop is 10%"
	 * if someone with that level had 10% chance to double drop.
	 * @param {int} the level you want to test for
	 * @return {String} the actual string message
	 */
	public abstract String getLvlDescription(int lvl);
	
	// Ability Execution methods
	/**
	 * This is the method called by Derius to run your ability. 
	 * It is similar to bukkits onEnable method.
	 * @param {MPlayer} the player to use the ability
	 * @param {Object} other parameter used in some abilities
	 * @return {Object} this object will be passed to onDeactivate for data transfering.
	 */
	public abstract Object onActivate(MPlayer p, Object other);
	
	/**
	 * This is the method called by Derius when your ability
	 * is deactivated. It is similar to bukkits onDisable method.
	 * @param {MPlayer} the player to stop using the ability
	 * @param {Object} object received from onActivate
	 */
	public abstract void onDeactivate(MPlayer p, Object other);
	
	/**
	 * Gets the skill associated with this ability
	 * @return {Skill} the skill associated with this ability
	 */
	public abstract Skill getSkill();
	
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
		
		result += this.getId();
		
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
