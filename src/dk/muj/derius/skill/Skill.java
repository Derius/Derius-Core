package dk.muj.derius.skill;

import java.util.ArrayList;
import java.util.List;

import dk.muj.derius.entity.MPlayer;

public abstract class Skill
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<String> earnExpDesc = new ArrayList<String>();
	private List<Description> passiveAbilityDesc = new ArrayList<Description>();
	private List<Description> activeAbilityDesc = new ArrayList<Description>();
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public void addEarnExpDesc (String desc) {	this.earnExpDesc.add(desc);	}
	
	public void addPassiveAbilityDesc (Description desc) {	this.passiveAbilityDesc.add(desc);	}
	
	public void addActiveAbilityDesc (Description desc) {	this.activeAbilityDesc.add(desc);	}
	
	public List<String> getEarnExpDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(String desc : earnExpDesc)
			descs.add(desc);
		return descs;
	}
	
	public List<String> getPassiveAbilityDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(Description desc : passiveAbilityDesc)
			descs.add(desc.toString());
		return descs;
	}
	
	public List<String> getActiveAbilityDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(Description desc : activeAbilityDesc)
			descs.add(desc.toString());
		return descs;
	}
	
	// -------------------------------------------- //
	// LEVELS
	// -------------------------------------------- //
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * The level calculation algorithm can also be change later on.
	 * @param {long} the amount of exp to convert to level
	 * @return {int} The level equivalent to the amount of exp passed.
	 */
	public LvlStatus LvlStatusFromExp(long exp)
	{
		return Skill.DefaultExpToLvlStatus(exp);
	}

	private static LvlStatus DefaultExpToLvlStatus(long exp)
	{
		int level = 0;
		int nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; nextLvlExp *= 1.1)
		{
			exp = exp-nextLvlExp;
			level++;
		}
		return new LvlStatus(level,(int)exp,nextLvlExp);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the skill. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {String} the skills unique id.
	 */
	public abstract String getId();
	
	/**
	 * Gets the name of the skill. This is seen by players.
	 * THis MUST be unique but can always be changed.
	 * @return {String} The skills name
	 */
	public abstract String getName();
	
	/**
	 * Tells whether or not the player can learn said skill.
	 * The skill can have different reasons the player might not.
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can learn said skill
	 */
	public abstract boolean CanPlayerLearnSkill(MPlayer p);
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}
	
}
