package dk.muj.derius;

public interface Skill
{
	/**
	 * Gets the id of the skill. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {String} the skills unique id.
	 */
	public String getId();
	
	/**
	 * Gets the name of the skill. This is seen by players.
	 * THis MUST be unique but can always be changed.
	 * @return {String} The skills name
	 */
	public String getName();
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * The level calculation algorithm can also be change later on.
	 * @param {long} the amount of exp to convert to level
	 * @return {int} The level equivalent to the amount of exp passed.
	 */
	public int ExpToLvl(long exp);
	
	
}
