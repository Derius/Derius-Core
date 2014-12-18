package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerTakeExpEvent;
import dk.muj.derius.skill.LvlStatus;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.SpecialisationStatus;
import dk.muj.derius.util.ChatUtil;
import dk.muj.derius.util.Listener;

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
	
	// Integer is id for the skill
	// Long is the exp
	private Map<Integer, Long> exp = new HashMap<Integer,Long>();
	
	private List<Integer> specialised = new CopyOnWriteArrayList<Integer>();
	
	private long specialisedMillis = System.currentTimeMillis();
	
	// Global Cooldown for all the skills/abilities (exhaustion), individual cooldowns can be added by the skill writer
	// Long is the millis (starting 1 January 1970), when the abilitys cooldown expires.
	private transient long cooldown = 0;
	
	// An int that stores which Ability is currently activated.
	private transient int activatedAbility = 0;
	
	// The tool which the user has prepared.
	// A tool is prepared by right clicking, then can activate abilities
	private transient Material preparedTool = null;
	
	// A boolean that defines whether the player wants to activate abilities by chat or not.
	private boolean isListeningToChat = false;
	
	// A Map that stores which string a player types in chat should activate what ability.
	private Map<String, Ability> chatKeys = new HashMap<String, Ability>();
	
	// The players choice on how he wants his messages sent.
	private MsgType type = MsgType.TITLE;
	
	
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
			ChatUtil.msgLevelUp(this, skill, lvlAfter);
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
	
	// -------------------------------------------- //
	// SPECIALISATION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player is specialised in this skill
	 * @param {Skill} the skill
	 * @return {boolean} true if the player is specialised in the skill
	 */
	public SpecialisationStatus isSpecialisedIn(Skill skill)
	{
		if( specialised.contains(skill.getId()))
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
		
		if(MConf.get().specialisationMax <= specialised.size())
			return SpecialisationStatus.TOO_MANY;
		
		specialised.add(skill.getId());
		return SpecialisationStatus.HAS_NOW;
	}
	
	
	/**
	 * Sets the player to not be specialised in the skill.
	 * This will not succeed if the player isn't specialised beforehand
	 * or the skill is on the spcialisationAutomatic or black list.
	 * @param {Skill} the skill
	 * @return {SpecialisationStatus} Whether or not the player is, specialised in the skill now.
	 */
	public SpecialisationStatus setNotSpecialisedIn(Skill skill)
	{	
		if(MConf.get().specialisationAutomatic.contains(skill.getId()))
			return SpecialisationStatus.AUTO_ASSIGNED;
		
		if(MConf.get().specialisationBlacklist.contains(skill.getId()))
			return SpecialisationStatus.BLACK_LISTED;
		
		if(!this.specialised.contains(skill.getId()))
			return SpecialisationStatus.DIDNT_HAVE;
		
		specialised.remove(skill.getId());
		exp.put(skill.getId(), 0l);
		return SpecialisationStatus.DONT_HAVE_NOW;
	}
	
	/**
	 * Gets an array of the skills this player has specialised in
	 * @return {Skill[]} the skills this player has specialised in
	 */
	public List<Skill> getSpecialisedSkills()
	{		
		List<Skill> ret = new ArrayList<Skill>();
		for(int i: specialised)
		{
			Skill s = Skill.GetSkillById(i);
					if(s != null)
						ret.add(s);
		}
		return ret;
	}
	
	/**
	 * Gets the last time a player either specialised or unspecialised in a skill
	 * This is used so players don't change their specialisation all the time.
	 * @return {long} system millis for last specialisation change
	 */
	public long getSpecialisationChangeMillis()
	{
		return this.specialisedMillis;
	}
	
	/**
	 * Sets the last time a player either specialised or unspecialised in a skill
	 * This is used so players don't change their specialisation all the time.
	 * @param {long} system millis for last specialisation change
	 */
	public void setSpecialisationChangeMillis(long millis)
	{
		this.specialisedMillis = millis;
	}
	
	/**
	 * Gets when the specialisation cooldown will expire
	 * This is the last time they changed any specialisation + some time modfied by server owner
	 * This is used so players don't change their specialisation all the time.
	 * @return {long} system millis for last specialisation change + some cooldown
	 */
	public long getSpecialisationCooldownExpire()
	{
		return this.specialisedMillis + MConf.get().specialisationCooldown;
	}
	
	/**
	 * Checks whether the specialisation cooldown has expired
	 * @return {boolean} true if specialisation cooldown has expired
	 */
	public boolean hasSpecialisationCooldownExpired()
	{
		return this.getSpecialisationCooldownExpire() < System.currentTimeMillis();
	}
	
	// -------------------------------------------- //
	// ABILITIES
	// -------------------------------------------- //

	/**
	 * Activates an ability
	 * this is the proper way to activate an ability
	 * @param {Ability} the ability to activate
	 * @param {Object} some abilities need another object. Check for the individual ability
	 */
	public void ActivateAbility(final Ability ability, Object other)
	{	
		//CHECKS
		if(!ability.CanPlayerActivateAbility(this))
			return;
		if(this.isPlayer() && !ability.CanAbilityBeUsedInArea(this.getPlayer().getLocation()))
			return;
		
		
		//ACTIVATE
		if(ability.getType() == AbilityType.PASSIVE)
			this.ActivatePassiveAbility(ability, other);
		
		if(ability.getType() == AbilityType.ACTIVE)
			this.ActivateActiveAbility(ability, other);
	}
	
	/**
	 * Activates an ability
	 * this is the proper way to activate an ability
	 * @param {Ability} the ability to activate
	 */
	public void ActivateAbility(final Ability ability)
	{
		this.ActivateAbility(ability, null);
	}
	
	private void ActivatePassiveAbility(final Ability ability, Object other)
	{
		AbilityActivateEvent e = new AbilityActivateEvent(ability, this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
	
		ability.onActivate(this, other);
	}
	
	private void ActivateActiveAbility(final Ability ability, Object other)
	{
		if(this.HasActivatedAny())
			return;

		if (!this.hasCooldownExpired(true))
			return;
		
		AbilityActivateEvent e = new AbilityActivateEvent(ability, this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
		
		this.activatedAbility = ability.getId();

		Bukkit.getScheduler().runTaskLaterAsynchronously(Derius.get(), new Runnable(){
			@Override
			public void run()
			{
				DeactivateActiveAbility(ability);
				setCooldownExpireIn(ability.getCooldownTime(get()));
			}
		}, ability.getTicksLast(this.getLvl(ability.getSkill())));
		ability.onActivate(this, other);
	}
	
	/**
	 * Deactivates an ability for this player.
	 * This should however automatically be done by our scheduled tasks.
	 * @param {Ability} the ability to deactivate
	 */
	public void DeactivateActiveAbility(Ability ability)
	{
		AbilityDeactivateEvent e = new AbilityDeactivateEvent(ability, this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return;
		this.activatedAbility = 0;
		ability.onDeactivate(this);
		
	}
	
	/**
	 * Checks if the player has ability activated.
	 * This is only for easily cross plugin data sharing 
	 * & making storing of data easier for you
	 * @param {int} id of the ability
	 */
	public boolean HasActivated(Ability ability)
	{
		return this.activatedAbility == ability.getId();
	}
	
	/**
	 * Tells whether or not the player has ANY abilities activated.
	 * @return {boolean} true if the player has ANY abilities activated.
	 */
	public boolean HasActivatedAny()
	{
		return this.activatedAbility != 0;
	}
	
	/**
	 * Gets the id of the activated ability
	 * 0 if no ability is activated
	 * @return {int} id of activated ability. 0 if none
	 */
	public int getActivated()
	{
		return this.activatedAbility;
	}
	
	// -------------------------------------------- //
	// PREPARED TOOL
	// -------------------------------------------- //
	
	/**
	 * Gets the tool which the user has prepared
	 * this is used for activating active abilities
	 * @return {Material} the tool the player has prepared
	 */
	public Material getPreparedTool()
	{
		return preparedTool;
	}

	/**
	 * Sets the tool which the user has prepared
	 * this is used for activating active abilities
	 * @param {Material} the tool the player will have prepared
	 */
	public void setPreparedTool(final Material tool)
	{
		if(this.HasActivatedAny())
			return;
		if(this.getPreparedTool() != null)
			return;
		if(!Listener.isRegistered(tool))
			return;
		if(tool != null)
		{
			ChatUtil.msgToolPrepared(this, tool);
			this.preparedTool = tool;
			Bukkit.getScheduler().runTaskLaterAsynchronously(Derius.get(), new Runnable()
			{
				@Override
				public void run()
				{
					preparedTool = null;
					if(getActivated() == 0)
						ChatUtil.msgToolNotPrepared(get(), tool);
					
				}
			}, 20*2);
		}
		else
			this.preparedTool = null;
		
	}

	// -------------------------------------------- //
	// MANAGING CHAT | ACTIVATION & MSGTYPE
	// -------------------------------------------- //
	
	// set and get for isListeningToChat boolean
	/**
	 * Gets the boolean if someone is listening to ability activation through chat
	 * @return {Boolean} returns if listening.
	 */
	public boolean getIsListeningToChat()
	{
		return this.isListeningToChat;
	}
	
	/**
	 * Sets the boolean if someone is listening to ability activation through chat
	 * @param {boolean} set whether it should listen to chat or not.
	 */
	public void setIsListeningToChat (boolean state)
	{
		this.isListeningToChat = state;
	}
	
	// Adding, getting, setting to the chatKeys map
	
	/**
	 * Adds an entry to the mplayers chatkey map
	 * @param {String} The key that the Chat should listen for
	 * @param {Ability} the ability it should activate
	 */
	public void addChatKeys(String key, Ability ability)
	{
		this.chatKeys.put(key, ability);
	}
	
	/**
	 * Removes an entry to the mplayers chatkey map
	 * @param {String} The key and its ability that should be removed
	 */
	public void removeChatKeys (String key)
	{
		this.chatKeys.remove(key);
	}
	
	/**
	 * Gets all the keys from the chatKeys map
	 * @return {List<String>} Get's you all keys from the map
	 */
	public List<String> getChatKeys()
	{
		List<String> list = new ArrayList<String>();
		list.addAll(this.chatKeys.keySet());
		return list;
	}
	
	/**
	 * Removes all entries of this mplayers map.
	 */
	public void clearChatKeys()
	{
		this.chatKeys.clear();
	}
	
	/**
	 * Checks whether this String is already in use or not
	 * @param {String} the key you want to check for
	 * @return {boolean} if it is or is not a chat key
	 */
	public boolean isAlreadyChatKey(String key)
	{
		for(String string:  this.chatKeys.keySet())
		{
			if (string.equals(key))
				return true;
		}
		return false;
	}
	/**
	 * Has the mplayer any chat keys registered?
	 * @return {boolean} whether it is empty or not
	 */
	public boolean hasAnyChatKeys ()
	{
		return !this.chatKeys.isEmpty();
	}
	
	/**
	 * 
	 * @param {String} The message to check for
	 * @return {Ability} the ability we get from this message
	 */
	public Ability getAbilityBySubString (String message)
	{
		int pos = -1;
		
		for (String key: this.getChatKeys())
		{
			pos = message.indexOf(key);
			if (pos != -1)
			{
				return this.getAbilityfromChatKey(key);
			}
		}
		return null;
	}
	
	/**
	 * Gets an ability from it's chat key
	 * @param {String} the key you want to get the ability by
	 * @return {Ability} the ability you got from the map
	 */
	public Ability getAbilityfromChatKey(String key)
	{
		return this.chatKeys.get(key);
	}
	
	/**
	 * Gets the map of this mplayer as a list of strings
	 * @return {List<String>} the list of all the map data
	 */
	public List<String> chatKeysToString()
	{
		List<String> msgLines = new ArrayList<String>();
		
		for(String str: chatKeys.keySet())
		{
			msgLines.add(Txt.parse("<lime>" + str + "<i> activates " + getAbilityfromChatKey(str).toString()));
		}
		
		return msgLines;
	}
	
	// get/set msgType
	
	public MsgType getMsgType()
	{
		return this.type;
	}
	
	public void setMsgType(MsgType newType)
	{
		this.type = newType;
	}

	// -------------------------------------------- //
	// MANAGING COOLDOWN
	// -------------------------------------------- //
	
	/**
	 * Sets users time when the global cooldown should expire.
	 * this is system millis
	 * @param {long} the cooldown to set it to
	 */
	public void setCooldownExpire( long cooldownTime)
	{
		this.cooldown = cooldownTime;
	}
	
	/**
	 * Gets players cooldown.
	 * this is system millis
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
	 * Checks whether the cooldown has expired and if sendMessage is true
	 * send the cooldown message along.
	 * @return {boolean} whether the cooldown has expired or not
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
		
		msg(Txt.parse(MConf.get().msgAbilityCooldown, timeRemainingSeconds));
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
	public void setCooldownExpireBetween (int ticksMin, int ticksMax)
	{
		long currentTime = System.currentTimeMillis();
		int difference = RandomBetween(ticksMin, ticksMax);

		setCooldownExpire(currentTime+difference/20*1000);
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
		List<String> ret = new ArrayList<String>();
		int level = this.getLvl(skill);
		for(Ability a: skill.getAllAbilities())
			ret.add(a.getLvlDescription(level));
		return ret;
	}
	
	/**
	 * Checks whether a player qualifies for chatListening
	 * @return {boolean} are the conditions met?
	 */
	public boolean isChatListeningOk()
	{
		if (this.getIsListeningToChat() && this.hasAnyChatKeys())
			return true;
		return false;
	}
	
	private int RandomBetween(int from, int to)
	{
		int range = to - from + 1;
		return (int) (Math.random()*range) + to;
	}
}
