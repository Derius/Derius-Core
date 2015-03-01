package dk.muj.derius.util;

import java.util.Optional;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Ability.AbilityType;
import dk.muj.derius.api.events.AbilityActivateEvent;
import dk.muj.derius.api.events.AbilityDeactivateEvent;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.lib.scheduler.TempTask;

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
	public static boolean canPlayerActivateAbility(DPlayer dplayer, Ability ability, VerboseLevel verboseLevel)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(ability, "ability mustn't be null");
		Validate.notNull(verboseLevel, "verboselevel mustn't be null");
		
		for (Req req : ability.getActivateRequirements())
		{
			if (req.apply(dplayer, ability)) continue;
			if (verboseLevel.includes(req.getVerboseLevel())) dplayer.sendMessage(req.createErrorMessage(dplayer, ability));
			return false;
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
	public static boolean canPlayerSeeAbility(DPlayer dplayer, Ability ability, VerboseLevel verboseLevel)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(ability, "ability mustn't be null");
		Validate.notNull(verboseLevel, "verboselevel mustn't be null");
		
		for (Req req : ability.getSeeRequirements())
		{
			if (req.apply(dplayer, ability)) continue;
			if (verboseLevel.includes(req.getVerboseLevel())) dplayer.sendMessage(req.createErrorMessage(dplayer, ability));
			return false;
		}
		return true;
	}
	
	// -------------------------------------------- //
	// ACTIVATION: PUBLIC
	// -------------------------------------------- //
	
	/**
	 * Activates an ability
	 * This is the proper way to activate an ability
	 * @param {DPlayer} player to activate ability on
	 * @param {Ability} the ability to activate
	 * @param {Object} some abilities need another object. Check for the individual ability
	 * @param {boolean} inform the player if not allowed
	 * @param {Optional<Object>} the object passed from onActivate to onDeactivate
	 */
	public static Object activateAbility(DPlayer dplayer, final Ability ability, Object other, VerboseLevel verboseLevel)
	{	
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(ability, "ability mustn't be null");
		Validate.notNull(verboseLevel, "verboselevel mustn't be null");
		
		// CHECKS
		if ( ! AbilityUtil.canPlayerActivateAbility(dplayer, ability, verboseLevel)) return null;
		
		// STAMINA
		dplayer.takeStamina(ability.getStaminaUsage());
		
		// EVENT
		AbilityActivateEvent event = new AbilityActivateEvent(ability, dplayer);
		if ( ! event.runEvent()) return null;
		
		// ACTIVATE
		if (ability.getType() == AbilityType.PASSIVE)
		{
			return activatePassiveAbility(dplayer, ability, other);
		}
		else if (ability.getType() == AbilityType.ACTIVE)
		{
			return activateActiveAbility(dplayer, ability, other);
		}
		
		// This might be cruel but will actually help squash bugs faster.
		throw new RuntimeException("Passed abiliy does not have a valid ability type");
	}
	
	/**
	 * Deactivates an ability for dplayer player.
	 * This should however automatically be done by our scheduled tasks.
	 * @param {DPlayer} player to deactivate ability on
	 * @param {Ability} the ability to deactivate
	 */
	public static void deactivateActiveAbility(DPlayer dplayer, Object other)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		
		Optional<Ability> optActivated = dplayer.getActivatedAbility();
		if ( ! optActivated.isPresent()) throw new IllegalStateException("The player does not currently have a enabled ability");
		Ability ability = optActivated.get();
		
		AbilityDeactivateEvent deactivateEvent = new AbilityDeactivateEvent(ability, dplayer);
		if ( ! deactivateEvent.runEvent()) return;
		
		ability.onDeactivate(dplayer, other);
		dplayer.setActivatedAbility(Optional.empty());
		
		if(ability.hasCooldown())
		{
			dplayer.setCooldownExpireInMillis(ability.getCooldownMillis());
		}
	}
	
	// -------------------------------------------- //
	// ACTIVATION: PRIVATE
	// -------------------------------------------- //
	
	private static Object activatePassiveAbility(DPlayer dplayer, final Ability ability, Object other)
	{
		Validate.isTrue(ability.getType() == AbilityType.PASSIVE, "abilitytype must be passive");
	
		final Object obj = ability.onActivate(dplayer, other);
		
		ability.onDeactivate(dplayer, other);
		
		if(ability.hasCooldown())
		{
			dplayer.setCooldownExpireInMillis(ability.getCooldownMillis());
		}
		
		return obj;
	}
	
	private static Object activateActiveAbility(final DPlayer dplayer, final Ability ability, Object other)
	{
		Validate.isTrue(ability.getType() == AbilityType.ACTIVE, "abilitytype must be active");
		if (dplayer.hasActivatedAny()) return null;

		dplayer.setActivatedAbility(Optional.of(ability));
		
		dplayer.setPreparedTool(Optional.empty());

		final Object obj = ability.onActivate(dplayer, other);
		int duration = ability.getDurationMillis(dplayer.getLvl(ability.getSkill()));
		
		TempTask run = new TempTask(duration, 1)
		{
			public void invoke() { deactivateActiveAbility(dplayer, obj); }
			public Plugin getPlugin() { return DeriusCore.get(); }
		};
		
		run.activate();
		
		return obj;
	}
	
}
