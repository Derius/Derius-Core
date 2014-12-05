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
	
	//		String is id for the skill
	//		Long is the millis (starting 1 January 1970), when its ability was last used
	private Map<String,Long> cooldown= new HashMap<String,Long>();
	
	
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
	
	// -------------------------------------------- //
	// MANAGING COOLDOWN
	// -------------------------------------------- //
	
	/**
	 * Sets users cooldown in said skill. Most used to set it to the current millis of the server running.
	 * @param {String} id of the skill
	 * @param {long} the cooldown to set it to
	 */
	public void SetCooldown(String skillId, long cooldownTime)
	{
		this.cooldown.put(skillId, cooldownTime);
	}
	
	/**
	 * Gets players cooldown in said skill.
	 * @param {String} id of the skill
	 * @return {long} players cooldown in said skill
	 */
	public long GetCooldown(String skillId)
	{
		return this.cooldown.get(skillId);
	}
	
	/**
	 * Adds to the users cooldown in said skill
	 * @param {String} id of the skill
	 * @param {long} the amount to add to players cooldown
	 */
	public void ExtendCooldown(String skillId, long millismore)
	{
		this.cooldown.put(skillId, GetCooldown(skillId)+millismore);
	}
	
	/**
	 * Lowers users cooldown in said skill.
	 * @param {String} id of the skill
	 * @param {long} the amount of millis to take away from cooldown.
	 */
	public void ReduceCooldown(String skillId, long millisless)
	{
		this.cooldown.put(skillId, GetCooldown(skillId)-millisless);
	}


}