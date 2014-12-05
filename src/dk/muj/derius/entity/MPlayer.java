package dk.muj.derius.entity;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.massivecore.store.SenderEntity;

public class MPlayer extends SenderEntity<MPlayer>
{

	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static MPlayer get(Object oid)
	{
		return MPlayerColl.get().get(oid, false);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	//		String is id for the skill
	//		Long is the exp
	private Map<String, Long> exp = new HashMap<String,Long>();
	
	
	// -------------------------------------------- //
	// MANAGING EXP
	// -------------------------------------------- //
	/**
	 * Sets users exp in said skill.
	 * @param {String} id of the skill
	 * @param {long} the exp to set it to
	 */
	public void SetExp(String skillId, long exp)
	{
		this.exp.put(skillId, exp);
	}
	
	/**
	 * Gets players exp in said skill.
	 * @param {String} id of the skill
	 * @return {long} players exp in said skill
	 */
	public long GetExp(String skillId)
	{
		return this.exp.get(skillId);
	}
	
	/**
	 * Adds exp to user in said skill
	 * @param {String} id of the skill
	 * @param {long} the amount to add to players exp
	 */
	public void AddExp(String skillId, long exp)
	{
		this.exp.put(skillId, GetExp(skillId)+exp);
	}
	
	/**
	 * Takes users exp in said skill.
	 * @param {String} id of the skill
	 * @param {long} the amount of exp to take away.
	 */
	public void TakeExp(String skillId, long exp)
	{
		this.exp.put(skillId, GetExp(skillId)-exp);
	}
	
	// -------------------------------------------- //
	// MANAGE SKILLS
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player has this skill initiated.
	 * @param {String} id of the skill
	 * @return true if the player has something in this skill (even 0)
	 */
	public boolean HasSkill(String skillId)
	{
		return this.exp.containsKey(skillId);
	}
	
	/**
	 * Instantiates this skill for the player
	 * if not already instantiated
	 * @param {String} id of the skill
	 */
	public void InstantiateSkill(String skillId)
	{
		if(!this.exp.containsKey(skillId))
			this.exp.put(skillId, new Long(0));
	}
	
	
	


}