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
	
	//		Global Cooldown for all the skills/abilities (exhaustion), individual cooldowns can be added by the skill writer
	//		Long is the millis (starting 1 January 1970), when the abilitys cooldown expires.
	private transient long cooldown = 0;
	
	
	// -------------------------------------------- //
	// MANAGING EXP
	// -------------------------------------------- //
	/**
	 * Sets users exp in said skill.
	 * @param {String} id of the skill
	 * @param {long} the exp to set it to
	 */
	public void setExp(String skillId, long exp)
	{
		this.exp.put(skillId, exp);
	}
	
	/**
	 * Gets players exp in said skill.
	 * @param {String} id of the skill
	 * @return {long} players exp in said skill
	 */
	public long getExp(String skillId)
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
		this.exp.put(skillId, getExp(skillId)+exp);
	}
	
	/**
	 * Takes users exp in said skill.
	 * @param {String} id of the skill
	 * @param {long} the amount of exp to take away.
	 */
	public void TakeExp(String skillId, long exp)
	{
		this.exp.put(skillId, getExp(skillId)-exp);
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
		if(!this.HasSkill(skillId))
			this.exp.put(skillId, new Long(0));
	}
	
	// -------------------------------------------- //
	// MANAGING COOLDOWN
	// -------------------------------------------- //
	
	/**
	 * Sets users time when the global cooldown should expire.
	 * @param {long} the cooldown to set it to
	 */
	public void setCooldownExpire( long cooldownTime)
	{
		this.cooldown = cooldownTime;
	}
	
	/**
	 * Gets players cooldown.
	 * @return {long} players global cooldown
	 */
	public long getCooldownExpire()
	{
		return cooldown;
	}
	
	/**
	 * Adds millis to the users cooldown.
	 * @param {long} the amount of millis to add to players global cooldown
	 */
	public void ExtendCooldown(long millisMore)
	{
		this.setCooldownExpire(getCooldownExpire()+millisMore);
	}
	
	/**
	 * Lowers users cooldown expire time.
	 * @param {long} the amount of millis to lower the  global cooldown.
	 */
	public void ReduceCooldown(long millisLess)
	{
		this.setCooldownExpire(getCooldownExpire()-millisLess);
	}
}
