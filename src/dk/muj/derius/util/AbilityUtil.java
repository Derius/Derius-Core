package dk.muj.derius.util;

import java.util.Optional;

import org.bukkit.Bukkit;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Ability.AbilityType;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;

public final class AbilityUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	
	private AbilityUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// REQUIREMENTS
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player can use said ability.
	 * This is based on the ability requirements
	 * @param {DPlayer} the player you want to check
	 * @param {Ability} ability to check for
	 * @param {boolean} true if error message should be sent
	 * @return {boolean} true if the player can use said ability
	 */
	public static boolean canPlayerActivateAbility(DPlayer dplayer, Ability ability, boolean verboseNot)
	{
		for (Req req : ability.getActivateRequirements())
		{
			if ( ! req.apply(dplayer.getSender(), ability)) 
			{
				if (verboseNot) dplayer.sendMessage(req.createErrorMessage(dplayer.getSender(), ability));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Tells whether or not the player can see said ability.
	 * This is based on the skill requirements
	 * @param {DPlayer} the player you want to check
	 * @param {Ability} ability to check for
	 * @param {boolean} true if error message should be sent
	 * @return {boolean} true if the player can see said ability
	 */
	public static boolean canPlayerSeeAbility(DPlayer dplayer, Ability ability, boolean verboseNot)
	{
		for (Req req : ability.getSeeRequirements())
		{
			if ( ! req.apply(dplayer.getSender(), ability)) 
			{
				if (verboseNot) dplayer.sendMessage(req.createErrorMessage(dplayer.getSender(), ability));
				return false;
			}
		}
		return true;
	}
	
	// -------------------------------------------- //
	// ACTIVATION: PUBLIC
	// -------------------------------------------- //
	
	/**
	 * Activates an ability
	 * mplayer is the proper way to activate an ability
	 * @param {DPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 * @param {Object} some abilities need another object. Check for the individual ability
	 * @param {boolean} inform the player if not allowed
	 * @param {Optional<Object>} the object passed from onActivate to onDeactivate
	 */
	public static Object activateAbility(DPlayer dplayer, final Ability ability, Object other, boolean verboseNot)
	{	
		// CHECKS
		if ( ! AbilityUtil.canPlayerActivateAbility(dplayer, ability, verboseNot)) return null;
		
		// ACTIVATE
		if (ability.getType() == AbilityType.PASSIVE)
		{
			return activatePassiveAbility(dplayer, ability, other);
		}
		else if (ability.getType() == AbilityType.ACTIVE)
		{
			return activateActiveAbility(dplayer, ability, other);
		}
		
		return null;
	}
	
	/**
	 * Deactivates an ability for mplayer player.
	 * This should however automatically be done by our scheduled tasks.
	 * @param {DPlayer} player to deactivate ability on
	 * @param {Ability} the ability to deactivate
	 */
	public static void deactivateActiveAbility(DPlayer dplayer, Object other)
	{
		AbilityDeactivateEvent e = new AbilityDeactivateEvent(dplayer.getActivatedAbility(), dplayer);
		Bukkit.getPluginManager().callEvent(e);
		if (e.isCancelled()) return;
		
		Ability ability = dplayer.getActivatedAbility();
		ability.onDeactivate(dplayer, other);
		dplayer.setActivatedAbility(null);
		dplayer.setCooldownExpireIn(ability.getCooldownTicks());
	}
	
	// -------------------------------------------- //
	// ACTIVATION: PRIVATE
	// -------------------------------------------- //
	
	private static Object activatePassiveAbility(DPlayer dplayer, final Ability ability, Object other)
	{
		AbilityActivateEvent e = new AbilityActivateEvent(ability, dplayer);
		e.run();
		if (e.isCancelled()) return Optional.empty();
	
		return ability.onActivate(dplayer, other);
	}
	
	private static Object activateActiveAbility(final DPlayer dplayer, final Ability ability, Object other)
	{
		if (dplayer.hasActivatedAny()) return null;
		
		AbilityActivateEvent e = new AbilityActivateEvent(ability, dplayer);
		Bukkit.getPluginManager().callEvent(e);
		if (e.isCancelled()) return null;
		
		dplayer.setActivatedAbility(ability);
		
		dplayer.setPreparedTool(Optional.empty());

		final Object obj = ability.onActivate(dplayer, other);
		
		Bukkit.getScheduler().runTaskLater(DeriusCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				deactivateActiveAbility(dplayer, obj);
			}
		}, ability.getDuration(dplayer.getLvl(ability.getSkill())));
		
		return obj;
	}
	
}
