package dk.muj.derius.skill;

import dk.muj.derius.entity.MPlayer;

public abstract class Ability
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the ability. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {int} the skills unique id.
	 */
	public abstract int getId();
	
	/**
	 * Gets the name of the ability. This is seen by players.
	 * This MUST be unique but can always be changed.
	 * @return {String} The abilities name
	 */
	public abstract String getName();
	
	/**
	 * Tells whether or not the player can use the ability .
	 * The ability can have different reasons the player might not.
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can use said ability
	 */
	public abstract boolean CanPlayerActivateAbility(MPlayer p);
}
