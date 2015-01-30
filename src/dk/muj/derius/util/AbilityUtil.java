package dk.muj.derius.util;

import java.util.Optional;

import org.bukkit.Bukkit;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;
import dk.muj.derius.req.Req;

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
	 * @param {MPlayer} the player you want to check
	 * @param {Ability} ability to check for
	 * @param {boolean} true if error message should be sent
	 * @return {boolean} true if the player can use said ability
	 */
	public static boolean canPlayerActivateAbility(MPlayer mplayer, Ability ability, boolean verboseNot)
	{
		for (Req req : ability.getActivateRequirements())
		{
			if ( ! req.apply(mplayer.getSender(), ability)) 
			{
				if (verboseNot) mplayer.sendMessage(req.createErrorMessage(mplayer.getSender(), ability));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Tells whether or not the player can see said ability.
	 * This is based on the skill requirements
	 * @param {MPlayer} the player you want to check
	 * @param {Ability} ability to check for
	 * @param {boolean} true if error message should be sent
	 * @return {boolean} true if the player can see said ability
	 */
	public static boolean canPlayerSeeAbility(MPlayer mplayer, Ability ability, boolean verboseNot)
	{
		for (Req req : ability.getSeeRequirements())
		{
			if ( ! req.apply(mplayer.getSender(), ability)) 
			{
				if (verboseNot) mplayer.sendMessage(req.createErrorMessage(mplayer.getSender(), ability));
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
	 * @param {MPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 * @param {Object} some abilities need another object. Check for the individual ability
	 * @param {boolean} inform the player if not allowed
	 * @param {Optional<Object>} the object passed from onActivate to onDeactivate
	 */
	public static Object activateAbility(MPlayer mplayer, final Ability ability, Object other, boolean verboseNot)
	{	
		// CHECKS
		if ( ! AbilityUtil.canPlayerActivateAbility(mplayer, ability, verboseNot)) return null;
		
		// ACTIVATE
		if (ability.getType() == AbilityType.PASSIVE)
		{
			return activatePassiveAbility(mplayer, ability, other);
		}
		else if (ability.getType() == AbilityType.ACTIVE)
		{
			return activateActiveAbility(mplayer, ability, other);
		}
		
		return null;
	}
	
	/**
	 * Activates an ability
	 * mplayer is the proper way to activate an ability
	 * @param {MPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 * @param {Object} some abilities need another object. Check for the individual ability
	 * @param {Optional<Object>} the object passed from onActivate to onDeactivate
	 */
	public static Object activateAbility(MPlayer mplayer, final Ability ability, Object other)
	{
		return activateAbility(mplayer, ability, other, false);
	}
	
	/**
	 * Activates an ability
	 * @param {MPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 * @param {boolean} inform the player if not allowed
	 */
	public static Object activateAbility(MPlayer mplayer, final Ability ability)
	{
		return activateAbility(mplayer, ability, null);
	}
	
	/**
	 * Deactivates an ability for mplayer player.
	 * This should however automatically be done by our scheduled tasks.
	 * @param {MPlayer} player to deactivate ability on
	 * @param {Ability} the ability to deactivate
	 */
	public static void deactivateActiveAbility(MPlayer mplayer, Object other)
	{
		AbilityDeactivateEvent e = new AbilityDeactivateEvent(mplayer.getActivatedAbility(), mplayer);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) return;
		
		Ability ability = mplayer.getActivatedAbility();
		ability.onDeactivate(mplayer, other);
		mplayer.setActivatedAbility(null);
		mplayer.setCooldownExpireIn(ability.getCooldownTime());
	}
	
	// -------------------------------------------- //
	// ACTIVATION: PRIVATE
	// -------------------------------------------- //
	
	private static Object activatePassiveAbility(MPlayer mplayer, final Ability ability, Object other)
	{
		AbilityActivateEvent e = new AbilityActivateEvent(ability, mplayer);
		e.run();
		if(e.isCancelled()) return Optional.empty();
	
		return ability.onActivate(mplayer, other);
	}
	
	private static Object activateActiveAbility(final MPlayer mplayer, final Ability ability, Object other)
	{
		if(mplayer.hasActivatedAny()) return null;
		
		AbilityActivateEvent e = new AbilityActivateEvent(ability, mplayer);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) return null;
		
		mplayer.setActivatedAbility(ability);
		
		mplayer.setPreparedTool(Optional.empty());

		final Object obj = ability.onActivate(mplayer, other);
		
		Bukkit.getScheduler().runTaskLater(Derius.get(), new Runnable()
		{
			@Override
			public void run()
			{
				deactivateActiveAbility(mplayer, obj);
			}
		}, ability.getDuration(mplayer.getLvl(ability.getSkill())));
		
		return obj;
	}
	
}
