package dk.muj.derius.ability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Const;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.exceptions.IdAlreadyInUseException;
import dk.muj.derius.integration.FactionIntegration;
import dk.muj.derius.skill.Skill;

public abstract class Ability
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private AbilityType type;
	
	private String desc = "";
	private String name;
	
	//A list of ability which we get from different sources.
	private static List<Ability> abilityList = new CopyOnWriteArrayList<Ability>();
	private static EnumMap<Material, Ability> interactKeys = new EnumMap<Material, Ability>(Material.class);
	private static EnumMap<Material, Ability> blockBreakKeys = new EnumMap<Material, Ability>(Material.class);
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	/**
	 * Gets an ability from its id. 
	 * This is the best way to get an ability, since the id never changes.
	 * @param {int} The id of the ability you wanted to get.
	 * @return{Ability} The ability which has this id
	 */
	public static Ability GetAbilityById(int abilityId)
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
	public static Ability GetAbilityByName(String abilityName)
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
	public static List<Ability> GetAllAbilities()
	{
		return new ArrayList<Ability>(Ability.abilityList);
	}
	
	/**
	 * Gets the skill which will activate when right clicked with this material
	 * @param {Material} the material you want to check for.
	 * @return {Ability} The ability which has said material as interact key.
	 */
	public static Ability getAbilityByInteractKey(Material key)
	{
		return Ability.interactKeys.get(key);
	}
	
	/**
	 * Gets the skill which will activate when a block with this material is broken
	 * @param {Material} the material you want to check for.
	 * @return {Ability} The ability which has said material as block break key.
	 */
	public static Ability getAbilityByBlockBreakKey(Material key)
	{
		return Ability.blockBreakKeys.get(key);
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
		Ability before = GetAbilityById(ability.getId());
		if(before != null)
		{
			int id = ability.getId();
			try
			{
				throw new IdAlreadyInUseException("The id: "+ id + " is already registered by " + before.getName()
						+ " but "+ability.getName() + " is trying to use it");
			}
			catch (IdAlreadyInUseException e)
			{
				e.printStackTrace();
			}
		}
		abilityList.add(ability);
		AbilityRegisteredEvent event = new AbilityRegisteredEvent(ability);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	// -------------------------------------------- //
	// SKILL
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
	public void setDescription(String str)
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
	public String getDisplayedDescription(MPlayer whatcherObject)
	{
		return Txt.parse("%s%s: <i>%s",AbilityUtil.CanUseAbilitySkillColor(this, whatcherObject),this.getName(), this.getDescription());
	}
	
	// -------------------------------------------- //
	// ACTIVATION KEYS
	// -------------------------------------------- //
	
	//INTERACT KEYS
	
	/**
	 * This will add said materials to its list of interact activation keys
	 * @param {Material...} an array of keys to add
	 */
	public void addInteractKeys(Material... keys)
	{
		for(Material m: keys)
			interactKeys.put(m,this);
	}
	
	/**
	 * This will add the materials in the collection to its list of interact activation keys
	 * @param {Collection<Material>} a collection of keys to add
	 */
	public void addInteractKeys(Collection<Material> keys)
	{
		for (Material m :keys)
		{
			interactKeys.put(m, this);
		}
	}
	
	/**
	 * This will remove said materials from the list of interact activation keys
	 * @param {Material...} an array of keys to remove
	 */
	public void removeInteractKeys(Material... keys)
	{
		for(Material m: keys)
			interactKeys.remove(m);
	}

	//BLOCK BREAK KEYS
	
	/**
	 * This will add said materials to its list of block break activation keys
	 * @param {Material...} an array of keys to add
	 */
	public void addBlockBreakKeys(Material... keys)
	{
		for(Material m: keys)
			blockBreakKeys.put(m,this);
	}
	
	/**
	 * This will add said materials to its list of block break activation keys
	 * @param {Material...} an array of keys to remove
	 */
	public void removeBlockBreakKeys(Material... keys)
	{
		for(Material m: keys)
			blockBreakKeys.remove(m);
	}
	
	// -------------------------------------------- //
	// TIME
	// -------------------------------------------- //
	
	/**
	 * Gets how many ticks this ability will last
	 * on passed mplayer, in ticks
	 * @param {MPlayer} player to check
	 * @return {int} amount of ticks, this ability would last.
	 */
	public int getTicksLast(int lvl)
	{
		return this.getTicksLastDefault(lvl);
	}
	
	
	private int getTicksLastDefault(int level)
	{
		return (2 + level/50)*20;
	}
	
	/**
	 * Gets how many ticks the cooldown will last
	 * after using this ability on passed mplayer
	 * @param {MPlayer} player to check
	 * @return {int} amount of ticks, the cooldown will be.
	 */
	public int getCooldownTime(MPlayer p)
	{
		return this.getCooldownTimedefault();
	}
	
	private int getCooldownTimedefault()
	{
		return 20*60*2;
	}

	// -------------------------------------------- //
	// AREA RESTRICTION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not this skill can be used in said area
	 * @param {Location} the are you want to check for
	 * @return {boolean} true if the skill can be used
	 */
	public boolean CanAbilityBeUsedInArea(Location loc)
	{
		if(FactionIntegration.EstablishIntegration())
		{
			Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));
			if(f != null)
				if(f.getFlag(Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD))
					return f.getFlag(Const.FACTION_FLAG_ABILITIES_USE);
			
		}
		return MConf.get().worldAbilityUse.get(this.getId()).EnabledInWorld(loc.getWorld());
	}
	
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
	
	/**
	 * Tells whether or not the player can use the ability .
	 * The ability can have different reasons the player might not.
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can use said ability
	 */
	public abstract boolean CanPlayerActivateAbility(MPlayer p);
	
	// Ability Execution methods
	/**
	 * This is the method called by Derius to run your ability. 
	 * It is similar to bukkits onEnable method.
	 * @param {MPlayer} the player to use the ability
	 * @param {Object} other parameter used in some abilities
	 */
	public abstract void onActivate(MPlayer p, Object other);
	
	/**
	 * This is the method called by Derius when your ability
	 * is deactivated. It is similar to bukkits onDisable method.
	 * @param {MPlayer} the player to stop using the ability
	 */
	public abstract void onDeactivate(MPlayer p);
	
	/**
	 * Gets the skill associated with this ability
	 * @return {Skill} the skill associated with this ability
	 */
	public abstract Skill getSkill();
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}
}
