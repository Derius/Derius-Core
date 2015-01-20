package dk.muj.derius.util;

import java.util.Optional;

import org.bukkit.Bukkit;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
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
		if ( ! ability.canPlayerActivateAbility(mplayer, verboseNot)) return null;
		
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
		}, ability.getTicksLast(mplayer.getLvl(ability.getSkill())));
		
		return obj;
	}
	
}
