package dk.muj.derius.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;

import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.exceptions.IdAlreadyInUseException;

public class Abilities
{
	//This is what we consider a static class
		//So it should not be instantiated.
		private Abilities(){};
		
		//A list of ability which we get from different sources.
		private static List<Ability> abilityList = new CopyOnWriteArrayList<Ability>();
		
		/**
		 * Adds a ability to our system.
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
			AbilityRegisteredEvent event = new AbilityRegisteredEvent(ability);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
		
		/**
		 * Gets a ability from its id. 
		 * This is the best way to get a ability, since the id rarely changes.
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
		 * Gets a ability from its name.
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
		
		public static List<Ability> GetAllAbilities()
		{
			return new ArrayList<Ability>(Abilities.abilityList);
		}
}
