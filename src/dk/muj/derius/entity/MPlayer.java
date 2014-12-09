package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerTakeExpEvent;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.SpecialisationStatus;

public class MPlayer extends SenderEntity<MPlayer>
{

	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static MPlayer get(Object oid)
	{
		return MPlayerColl.get().get(oid, false);
	}
	
	//Used for inner class
	public MPlayer get()
	{
		return this;
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
	 * @param {Skill} the skill
	 * @param {long} the exp to set it to
	 */
	private void setExp(Skill skill, long exp)
	{
		this.exp.put(skill.getId(), exp);
	}
	
	/**
	 * Gets players exp in said skill.
	 * @param {Skill} the skill
	 * @return {long} players exp in said skill
	 */
	public long getExp(Skill skill)
	{
		return this.exp.get(skill.getId());
	}
	
	/**
	 * Adds exp to user in said skill
	 * @param {Skill} the skill
	 * @param {long} the amount to add to players exp
	 */
	public void AddExp(Skill skill, long exp)
	{
		int lvlBefore = this.getLvl(skill);
		
		if(lvlBefore >= this.MaxLevel(skill))
			return;
		
		PlayerAddExpEvent event = new PlayerAddExpEvent(this,skill,exp);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled())
			this.setExp(skill, this.getExp(skill)+exp);
		
		int lvlAfter = this.getLvl(skill);
		if(lvlBefore != lvlAfter)
			this.sendMessage(Txt.parse("<green>[DERIUS] <yellow>You leveled up <lime>%s <yellow>level in <aqua>%s", lvlAfter-lvlBefore+"", skill.getName()));
	}
	
	/**
	 * Takes users exp in said skill.
	 * @param {Skill} the skill
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
			this.sendMessage(Txt.parse("<green>[DERIUS] <yellow>You leveled down <b>%s <yellow>level in <aqua>%s", lvlBefore-lvlAfter+"", skill.getName()));
	}
	
	// -------------------------------------------- //
	// MANAGE SKILLS
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player has this skill initiated.
	 * @param {Skill} the skill
	 * @return true if the player has something in this skill (even 0)
	 */
	public boolean HasSkill(Skill skill)
	{
		return this.exp.containsKey(skill.getId());
	}
	
	/**
	 * Instantiates this skill for the player
	 * if not already instantiated
	 * @param {Skill} the skill
	 */
	public void InstantiateSkill(Skill skill)
	{
		if(!this.HasSkill(skill))
			this.exp.put(skill.getId(), new Long(0));
	}
	
	public int MaxLevel(Skill skill)
	{
		SpecialisationStatus status = this.isSpecialisedIn(skill);
		if(status == SpecialisationStatus.HAD || status == SpecialisationStatus.AUTO_ASSIGNED)
			return MConf.get().hardCap;
		return MConf.get().softCap;
	}
	
	/**
	 * Tells whether or not the player is specialised in this skill
	 * @param {Skill} the skill
	 * @return {boolean} true if the player is specialised in the skill
	 */
	public SpecialisationStatus isSpecialisedIn(Skill skill)
	{
		if( specialised.contains(skill.getId()) )
			return SpecialisationStatus.HAD;
		
		if(MConf.get().specialisationAutomatic.contains(skill.getId()))
			return SpecialisationStatus.AUTO_ASSIGNED;
		
		if(MConf.get().specialisationBlacklist.contains(skill.getId()))
			return SpecialisationStatus.BLACK_LISTED;
		
		return SpecialisationStatus.DIDNT_HAVE;
	}
	
	/**
	 * Sets the player specialised in the skill.
	 * This will not succeed if the player is filled with specialisations already
	 * or the skill is on the spcialisationAutomatic or black list.
	 * @param {Skill} the skill
	 * @return Whether or not the player is ,specialised in the skill now.
	 */
	public SpecialisationStatus setSpecialisedIn(Skill skill)
	{
		if( specialised.contains(skill.getId()) )
			return SpecialisationStatus.HAD;
		
		if(MConf.get().specialisationAutomatic.contains(skill.getId()))
			return SpecialisationStatus.AUTO_ASSIGNED;
		
		if(MConf.get().specialisationBlacklist.contains(skill.getId()))
			return SpecialisationStatus.BLACK_LISTED;
		
		if(MConf.get().specialisationMax >= specialised.size())
			return SpecialisationStatus.TOO_MANY;
		
		specialised.add(skill.getId());
		return SpecialisationStatus.HAS_NOW;
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
	 * @param {int} the amount of ticks to add to players global cooldown
	 */
	public void ExtendCooldown(int ticksToAdd)
	{
		this.setCooldownExpire(getCooldownExpire()+ticksToAdd/20*1000);
	}
	
	/**
	 * Lowers users cooldown expire time.
	 * @param {int} the amount of millis to lower the  global cooldown.
	 */
	public void ReduceCooldown(int ticksToReduce)
	{
		this.setCooldownExpire(getCooldownExpire()-ticksToReduce/20*1000);
	}
	
	/**
	 * Checks whether the Cooldown has expired and if sendMessage is true
	 * send the Cooldown message along.
	 * @return {boolean} whether the Cooldown has expired or not
	 */
	public boolean hasCooldownExpired (boolean sendMessage)
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime >= getCooldownExpire()) 
			return true;
		
		if (sendMessage) 
			AbilityCooldownMsg(currentTime); 
		return false;
	}
	
	/**
	 * Sends out the Cooldown message to the player. The Message itself can be changed in the MConf.
	 */
	public void AbilityCooldownMsg (long timeNow)
	{
		long currentTime = timeNow;
		long timeRemainingSeconds = (getCooldownExpire()-currentTime)/1000;
		
		msg(Txt.parse(MConf.get().abilityCooldownMsg, timeRemainingSeconds));
	}
	
	/**
	 * Sets the Cooldown to run out the passed amount of ticks in the future
	 * @param {int} ticks in the future the cooldown should be set to.
	 */
	public void setCooldownExpireIn (int ticks)
	{
		long currentTime = System.currentTimeMillis();
		setCooldownExpire(currentTime+ticks/20*1000);
	}
	
	/**
	 * Sets the Cooldown to be between the passed ticks in the future.
	 * @param {int} minimum ticks in the future the cooldown should be set to.
	 * @param {int} maximum ticks in the future the cooldown should be set to.
	 */
	public void setCooldownExpireBetween (int secondsMin, int secondsMax)
	{
		long currentTime = System.currentTimeMillis();
		int difference = RandomBetween(secondsMin, secondsMax);

		setCooldownExpire(currentTime+difference/20*1000);
	}
	
	// -------------------------------------------- //
	// MANAGE ACTIVATED ABILITIES
	// -------------------------------------------- //
	
	/**
	 * Activates an ability for this player.
	 * This is also for easily cross plugin data sharing 
	 * but will call the onActivate methods in ability
	 * @param {Ability} the ability activate
	 */
	public void ActivateAbility(final Ability ability, int ticksToLast)
	{
		AbilityActivateEvent e = new AbilityActivateEvent(ability, this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
		this.activatedAbilities.add(ability.getId());
		Bukkit.getScheduler().runTaskLater(Derius.get(), new Runnable(){
			@Override
			public void run()
			{
				DeactivateAbility(ability);
				setCooldownExpireIn(ability.getCooldownTime(get()));
			}
		}, ticksToLast);
		ability.onActivate(this);
		this.msg(Txt.parse(MConf.get().abilityActivatedMsg, ability.getName()));
	}
	
	/**
	 * Deactivates an ability for this player.
	 * This should however automatically be done
	 * by our scheduled tasks.
	 * @param {Ability} id of the ability
	 */
	public void DeactivateAbility(Ability ability)
	{
		AbilityDeactivateEvent e = new AbilityDeactivateEvent(ability, this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
		this.activatedAbilities.remove(new Integer(ability.getId()));
		ability.onDeactivate(this);
		this.msg(Txt.parse(MConf.get().abilityDeactivatedMsg, ability.getName()));
	}
	
	/**
	 * Checks if the player has ability activated.
	 * This is only for easily cross plugin data sharing 
	 * & making storing of data easier for you
	 * @param {int} id of the ability
	 */
	public boolean HasActivated(Ability ability)
	{
		return this.activatedAbilities.contains(ability.getId());
	}
	
	/**
	 * Tells whether or not the player has ANY abilities activated.
	 * @return {boolean} true if the player has ANY abilities activated.
	 */
	public boolean HasActivatedAny()
	{
		return this.activatedAbilities.size() != 0;
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
	
	private int RandomBetween(int from, int to)
	{
		int range = to - from + 1;
		return (int) (Math.random()*range) + to;
	}
	
}
