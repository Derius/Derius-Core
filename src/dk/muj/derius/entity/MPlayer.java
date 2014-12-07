package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerTakeExpEvent;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;

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
	private void setExp(Skill skill, long exp)
	{
		this.exp.put(skill.getId(), exp);
	}
	
	/**
	 * Gets players exp in said skill.
	 * @param {String} id of the skill
	 * @return {long} players exp in said skill
	 */
	public long getExp(Skill skill)
	{
		return this.exp.get(skill);
	}
	
	/**
	 * Adds exp to user in said skill
	 * @param {int} id of the skill
	 * @param {long} the amount to add to players exp
	 */
	public void AddExp(Skill skill, long exp)
	{
		int lvlBefore = this.getLvl(skill);
		
		PlayerAddExpEvent event = new PlayerAddExpEvent(this,skill,exp);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled())
			this.setExp(skill, this.getExp(skill)+exp);
		
		int lvlAfter = this.getLvl(skill);
		if(lvlBefore != lvlAfter)
			this.sendMessage(Txt.parse("<green>[DERIUS] <yellow>You leveled up <lime>%s <yellow>level in <aqua>%s"), lvlAfter-lvlBefore+"", skill.getName());
	}
	
	/**
	 * Takes users exp in said skill.
	 * @param {int} id of the skill
	 * @param {long} the amount of exp to take away.
	 */
	public void TakeExp(Skill skill, long exp)
	{
		int lvlBefore = this.getLvl(skill);
		
		PlayerTakeExpEvent event = new PlayerTakeExpEvent(this,skill,exp);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled())
			this.setExp(skill, this.getExp(skill)+exp);
		
		int lvlAfter = this.getLvl(skill);
		if(lvlBefore != lvlAfter)
			this.sendMessage(Txt.parse("<green>[DERIUS] <yellow>You leveled down <b>%s <yellow>level in <aqua>%s"), lvlBefore-lvlAfter+"", skill.getName());
	}
	
	// -------------------------------------------- //
	// MANAGE SKILLS
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player has this skill initiated.
	 * @param {String} id of the skill
	 * @return true if the player has something in this skill (even 0)
	 */
	public boolean HasSkill(Skill skill)
	{
		return this.exp.containsKey(skill.getId());
	}
	
	/**
	 * Instantiates this skill for the player
	 * if not already instantiated
	 * @param {String} id of the skill
	 */
	public void InstantiateSkill(Skill skill)
	{
		if(!this.HasSkill(skill))
			this.exp.put(skill.getId(), new Long(0));
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
	public LvlStatus getLvlStatus(Skill skill)
	{
		return skill.LvlStatusFromExp(this.getExp(skill));
	}
	
	/**
	 * Gets level for said skill in this MPlayer
	 * @param {String} id of the skill
	 * @return {int} players level in said skill
	 */
	public int getLvl(Skill skill)
	{
		return skill.LvlStatusFromExp(this.getExp(skill)).getLvl();
	}
	
	/**
	 * Tells whether or not this player can learn said skill.
	 * The requirements is set up by the skill not the core plugin
	 * @param {String} id of the skill
	 * @return true if the player can learn this skill
	 */
	public boolean CanLearnSkill(Skill skill)
	{
		return skill.CanPlayerLearnSkill(this);
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
	public List<String> getAbilitiesDecriptionByLvl(Skill skill)
	{
		return skill.getAbilitiesDecriptionByLvl(this.getLvl(skill));
	}
	
}
