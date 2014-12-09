package dk.muj.derius.ability;

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
	 * Since it is not stored, it could be changed without fatal consequences.
	 * But it may also break things, in some cases.
	 * @return {int} the abilities unique id.
	 */
	public abstract int getId();
	
	/**
	 * Gets the name of the ability. This is seen by players.
	 * This MUST be unique but can always be changed.
	 * However changin it may confuse players.
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
	
	/**
	 * Turns on the ability for said player.
	 * @param {MPlayer} the player to use the ability
	 */
	public abstract void onActivate(MPlayer p);
	
	/**
	 * Turns off the ability for said player.
	 * @param {MPlayer} the player to stop using the ability
	 */
	public abstract void onDeactivate(MPlayer p);
}
