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
	// ACTIVATION
	// -------------------------------------------- //
	
	/**
	 * Activates an ability
	 * mplayer is the proper way to activate an ability
	 * @param {MPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 * @param {Object} some abilities need another object. Check for the individual ability
	 */
	public static void activateAbility(MPlayer mplayer, final Ability ability, Object other)
	{	
		//CHECKS
		if ( ! ability.canPlayerActivateAbility(mplayer, true)) return;
		
		//ACTIVATE
		if (ability.getType() == AbilityType.PASSIVE)
		{
			activatePassiveAbility(mplayer, ability, other);
		}
		else if (ability.getType() == AbilityType.ACTIVE)
		{
			activateActiveAbility(mplayer, ability, other);
		}
	}
	
	/**
	 * Activates an ability
	 * @param {MPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 */
	public static void activateAbility(MPlayer mplayer, final Ability ability)
	{
		activateAbility(mplayer, ability, null);
	}
	
	private static void activatePassiveAbility(MPlayer mplayer, final Ability ability, Object other)
	{
		AbilityActivateEvent e = new AbilityActivateEvent(ability, mplayer);
		e.run();
		if(e.isCancelled())
			return;
	
		ability.onActivate(mplayer, other);
	}
	
	private static void activateActiveAbility(final MPlayer mplayer, final Ability ability, Object other)
	{
		if(mplayer.hasActivatedAny())
			return;
		
		AbilityActivateEvent e = new AbilityActivateEvent(ability, mplayer);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
		
		mplayer.setActivatedAbility(ability);
		
		mplayer.setPreparedTool(Optional.empty());

		Bukkit.getScheduler().runTaskLater(Derius.get(), new Runnable(){
			@Override
			public void run()
			{
				deactivateActiveAbility(mplayer);
				mplayer.setCooldownExpireIn(ability.getCooldownTime(mplayer));
			}
		}, ability.getTicksLast(mplayer.getLvl(ability.getSkill())));
		ability.onActivate(mplayer, other);
	}
	
	/**
	 * Deactivates an ability for mplayer player.
	 * This should however automatically be done by our scheduled tasks.
	 * @param {MPlayer} player to deactivate ability on
	 * @param {Ability} the ability to deactivate
	 */
	public static void deactivateActiveAbility(MPlayer mplayer)
	{
		AbilityDeactivateEvent e = new AbilityDeactivateEvent(mplayer.getActivatedAbility(), mplayer);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
		mplayer.getActivatedAbility().onDeactivate(mplayer);
		mplayer.setActivatedAbility(null);
	}
}
