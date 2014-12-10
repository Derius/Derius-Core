package dk.muj.derius.ability;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.exceptions.IdAlreadyInUseException;

public final class Abilities
{
	//This is what we consider a static class
	//So it should not be instantiated.
	private Abilities(){};
	
	//A list of ability which we get from different sources.
	private static List<Ability> abilityList = new CopyOnWriteArrayList<Ability>();
	private static EnumMap<Material, Ability> interactKeys = new EnumMap<Material, Ability>(Material.class);
	private static EnumMap<Material, Ability> blockBreakKeys = new EnumMap<Material, Ability>(Material.class);
		
	/**
	 * Adds an ability to our system.
	 * We will instantiate the correct fields.
	 * You still have to enforce the powers & general implementation.
	 * This should be done on server startup.
	 * @param {Ability} The ability you want to add
	 */
	public static void AddAbility(Ability ability)
	{
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
		for(Material m: ability.getInteractKeys())
			interactKeys.put(m, ability);
		for(Material m: ability.getBlockBreakKeys())
			blockBreakKeys.put(m, ability);
		AbilityRegisteredEvent event = new AbilityRegisteredEvent(ability);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	/**
	 * Gets an ability from its id. 
	 * This is the best way to get an ability, since the id never changes.
	 * @param {int} The id of the ability you wanted to get.
	 * @return{Ability} The ability which has this id
	 */
	public static Ability GetAbilityById(int abilityId)
	{
		for(Ability ability: Abilities.abilityList)
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
		for(Ability ability: Abilities.abilityList)
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
		return new ArrayList<Ability>(Abilities.abilityList);
	}
	
	/**
	 * Gets the skill which will activate when right clicked with this material
	 * @param {Material} the material you want to check for.
	 * @return {Ability} The ability which has said material as interact key.
	 */
	public static Ability getAbilityByInteractKey(Material key)
	{
		return Abilities.interactKeys.get(key);
	}
	
	/**
	 * Gets the skill which will activate when a block with this material is broken
	 * @param {Material} the material you want to check for.
	 * @return {Ability} The ability which has said material as block break key.
	 */
	public static Ability getAbilityByBlockBreakKey(Material key)
	{
		return Abilities.blockBreakKeys.get(key);
	}
	
	/**
	 * Removes interact key from map
	 * THIS IS NOT THE PROPER WAY TO UNREGISTER AN INTERACT KEY
	 * @param {Material} material to remove
	 */
	public static void addInteractKey(Material m, Ability a)
	{
		Abilities.interactKeys.put(m,a);
	}
	
	/**
	 * Adds a block break key to the map
	 * THIS IS NOT THE PROPER WAY TO REGISTER A BLOCK BREAK KEY
	 * @param {Material} material to remove
	 */
	public static void addBlockBreakKey(Material m, Ability a)
	{
		Abilities.blockBreakKeys.put(m,a);
	}
	
	/**
	 * Adds a interact key to the map
	 * THIS IS NOT THE PROPER WAY TO REGISTER AN INTERACT KEY
	 * @param {Material} material to remove
	 */
	public static void removeInteractKey(Material m)
	{
		Abilities.interactKeys.remove(m);
	}
	
	/**
	 * Removes block break key from map
	 * THIS IS NOT THE PROPER WAY TO UNREGISTER A BLOCK BREAK KEY
	 * @param {Material} material to remove
	 */
	public static void removeBlockBreakKey(Material m)
	{
		Abilities.blockBreakKeys.remove(m);
	}

}