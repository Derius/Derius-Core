package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.store.SenderEntity;

import dk.muj.derius.Derius;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skills;

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
	private Map<Integer, Long> exp = new HashMap<Integer,Long>();
	
	private List<Integer> specialised = new ArrayList<Integer>();
	
	//		Global Cooldown for all the skills/abilities (exhaustion), individual cooldowns can be added by the skill writer
	//		Long is the millis (starting 1 January 1970), when the abilitys cooldown expires.
	private transient long cooldown = 0;
	
	//A list of the active abilities the user has activated
	//Each ability should have a unique number
	private transient List<Integer> activatedAbilities = new ArrayList<Integer>();
	
	
	// -------------------------------------------- //
	// MANAGING EXP
	// -------------------------------------------- //
	/**
	 * Sets users exp in said skill.
	 * @param {String} id of the skill
	 * @param {long} the exp to set it to
	 */
	public void setExp(int skillId, long exp)
	{
		this.exp.put(skillId, exp);
	}
	
	/**
	 * Gets players exp in said skill.
	 * @param {String} id of the skill
	 * @return {long} players exp in said skill
	 */
	public long getExp(int skillId)
	{
		return this.exp.get(skillId);
	}
	
	/**
	 * Adds exp to user in said skill
	 * @param {String} id of the skill
	 * @param {long} the amount to add to players exp
	 */
	public void AddExp(int skillId, long exp)
	{
		this.exp.put(skillId, getExp(skillId)+exp);
	}
	
	/**
	 * Takes users exp in said skill.
	 * @param {String} id of the skill
	 * @param {long} the amount of exp to take away.
	 */
	public void TakeExp(int skillId, long exp)
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
	public boolean HasSkill(int skillId)
	{
		return this.exp.containsKey(skillId);
	}
	
	/**
	 * Instantiates this skill for the player
	 * if not already instantiated
	 * @param {String} id of the skill
	 */
	public void InstantiateSkill(int skillId)
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
	
	// -------------------------------------------- //
	// MANAGE ACTIVATED ABILITIES
	// -------------------------------------------- //
	
	public void ActivateAbility(int ability)
	{
		this.activatedAbilities.add(ability);
	}
	
	public void ActivateAbility(final int ability, int deactivateTime)
	{
		this.activatedAbilities.add(ability);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Derius.get(), new Runnable(){
			@Override
			public void run()
			{
				DeactivateAbility(ability); 
			}
		}, deactivateTime);
	}
	
	public void DeactivateAbility(int ability)
	{
		this.activatedAbilities.remove(ability);
	}
	
	public boolean HasActivated(int ability)
	{
		return this.activatedAbilities.contains(ability);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE METHODS
	// -------------------------------------------- //
	
	/**
	 * Gets a LvlStatus for said skill in this MPlayer
	 * @param {String} id of the skill
	 * @return {LvlStatus} The LvlStatus for said skill & this player
	 */
	public LvlStatus getLvlStatus(int skillId)
	{
		return Skills.GetSkillById(skillId).LvlStatusFromExp(this.getExp(skillId));
	}
	
	/**
	 * Gets level for said skill in this MPlayer
	 * @param {String} id of the skill
	 * @return {int} players level in said skill
	 */
	public int getLvl(int skillId)
	{
		return Skills.GetSkillById(skillId).LvlStatusFromExp(this.getExp(skillId)).getLvl();
	}
	
	/**
	 * Tells whether or not this player can learn said skill.
	 * The requirements is set up by the skill not the core plugin
	 * @param {String} id of the skill
	 * @return true if the player can learn this skill
	 */
	public boolean CanLearnSkill(int skillId)
	{
		return Skills.GetSkillById(skillId).CanPlayerLearnSkill(this);
	}
	
	/**
	 * Gets a list of descriptions for the different abilities in said skill.
	 * But these should include level specific data (using this players level)
	 * So this would include data like your double drop chance
	 * or the length of an active ability being activated (for that lvl).
	 * These string should be passed directly to a player under normal circumstances.
	 * @param {String} id of the skill
	 * @return {List<String>} description of abilities for said skill, corresponding to the players level.
	 */
	public List<String> getAbilitiesDecriptionByLvl(int skillId)
	{
		return Skills.GetSkillById(skillId).getAbilitiesDecriptionByLvl(this.getLvl(skillId));
	}
	
}
