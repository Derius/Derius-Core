package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerLevelDownEvent;
import dk.muj.derius.events.PlayerLevelUpEvent;
import dk.muj.derius.events.PlayerTakeExpEvent;
import dk.muj.derius.lambda.LvlStatus;
import dk.muj.derius.req.Req;
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
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public MPlayer load(MPlayer that)
	{
		this.exp = that.exp;
		this.specialised = that.specialised;
		this.specialisedMillis = that.specialisedMillis;
		this.isListeningToChat = that.isListeningToChat;
		this.chatKeys = that.chatKeys;
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	//			id		exp
	private Map<String, Long> exp = new HashMap<String, Long>();
	
	private Set<String> specialised = new CopyOnWriteArraySet<String>();
	
	private long specialisedMillis = 0;
	
	// A boolean that defines whether the player wants to activate abilities by chat or not.
	private boolean isListeningToChat = false;
	
	// A Map that stores which string a player types in chat should activate what ability.
	private Map<String, String> chatKeys = new HashMap<String, String>();
	
	// Global Cooldown for all the skills/abilities (exhaustion), individual cooldowns can be added by the skill writer
	// Long is the millis when the abilitys cooldown expires.
	private transient long cooldown = 0;
	
	// An int that stores which Ability is currently activated.
	private transient Ability activatedAbility = null;
	
	// The tool which the user has prepared.
	// A tool is prepared by right clicking, then can activate abilities
	private transient Optional<Material> preparedTool = Optional.empty();
	
	// -------------------------------------------- //
	// FIELD: EXP
	// -------------------------------------------- //
	
	private void setExp(Skill skill, long exp) { this.exp.put(skill.getId(), exp); }
	
	/**
	 * Gets players exp in said skill.
	 * @param {Skill} the skill
	 * @return {long} players exp in said skill
	 */
	public long getExp(Skill skill) { return this.exp.get(skill.getId()); }
	
	/**
	 * Adds exp to user in said skill
	 * @param {Skill} the skill
	 * @param {long} the amount to add to players exp
	 */
	public void addExp(Skill skill, long exp)
	{
		if (exp < 0) throw new IllegalArgumentException("Exp values must be positive");
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore >= this.getMaxLevel(skill)) return;
		
		PlayerAddExpEvent event = new PlayerAddExpEvent(this,skill,exp);
		event.run();
		if (event.isCancelled()) return;
		exp = event.getExpAmount();
		this.setExp(skill, this.getExp(skill) + exp);
		
		int lvlAfter = this.getLvl(skill);
		if (lvlBefore != lvlAfter)
		{
			PlayerLevelUpEvent lvlUp = new PlayerLevelUpEvent(this, skill);
			lvlUp.run();
			ChatUtil.msgLevelUp(this, skill, lvlAfter);
		}
	}
	
	/**
	 * Takes users exp in said skill.
	 * @param {Skill} the skill
	 * @param {long} the amount of exp to take away.
	 */
	public void takeExp(Skill skill, long exp)
	{
		if (exp < 0) throw new IllegalArgumentException("Exp values must be positive");
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore >= this.getMaxLevel(skill)) return;
		
		PlayerTakeExpEvent event = new PlayerTakeExpEvent(this,skill,exp);
		event.run();
		if (event.isCancelled()) return;
		exp = event.getExpAmount();
		this.setExp(skill, this.getExp(skill) - exp);
		
		int lvlAfter = this.getLvl(skill);
		if (lvlBefore != lvlAfter)
		{
			PlayerLevelDownEvent lvlDown = new PlayerLevelDownEvent(this, skill);
			lvlDown.run();
			ChatUtil.msgLevelDown(this, skill, lvlAfter);
		}
	}
	
	// -------------------------------------------- //
	// LEVEL
	// -------------------------------------------- //
	
	/**
	 * Gets a LvlStatus for said skill in this MPlayer
	 * @param {String} id of the skill
	 * @return {LvlStatus} The LvlStatus for said skill & this player
	 */
	public LvlStatus getLvlStatus(Skill skill) { return skill.getLvlStatusFromExp(this.getExp(skill)); }
	
	/**
	 * Gets level for said skill in this MPlayer
	 * @param {String} id of the skill
	 * @return {int} players level in said skill
	 */
	public int getLvl(Skill skill) { return this.getLvlStatus(skill).getLvl(); }
	
	/**
	 * The maximum level this player can reach in said skill
	 * @param {Skill} skill to check for
	 * @return {int} the level the player can reach
	 */
	public int getMaxLevel(Skill skill) { return Derius.get().getMaxLevelMixin().getMaxLevel(this, skill); }
	
	// -------------------------------------------- //
	// SKILL
	// -------------------------------------------- //
	
	public void instantiateSkill(Skill skill)
	{
		if (this.hasSkill(skill)) return;
		this.exp.put(skill.getId(), 0L);
	}
	
	public boolean hasSkill(Skill skill)
	{
		return this.exp.containsKey(skill.getId());
	}
	
	// -------------------------------------------- //
	// SPECIALISATION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player is specialised in this skill
	 * @param {Skill} the skill
	 * @return {boolean} true if the player is specialised in the skill
	 */
	public boolean isSpecialisedIn(Skill skill)
	{
		return  this.specialised.contains(skill.getId()) || skill.isSpAutoAssigned();
	}
	
	/**
	 * Sets the player specialised in the skill.
	 * This will not succeed if the player is filled with specialisations already
	 * or the skill is on the spcialisationAutomatic or black list.
	 * @param {Skill} the skill
	 * @param {boolean} inform them if not
	 * @return {boolean} true if the player is explicitely specialised in the skill now.
	 * by explicitly we do not mean auto assigning.
	 */
	public boolean setSpecialisedIn(Skill skill, boolean verbooseNot)
	{
		for (Req req : skill.getSpecialiseRequirements())
		{
			if ( ! req.apply(this.getSender(), skill))
			{
				if (verbooseNot) this.sendMessage(req.createErrorMessage(this.sender, skill));
				return false;
			}
		}
		
		this.specialised.add(skill.getId());
		return true;
	}
	
	
	/**
	 * Sets the player to not be specialised in the skill.
	 * you must do all checks yourself.
	 * @param {Skill} the skill
	 */
	public void setNotSpecialisedIn(Skill skill)
	{	
		this.specialised.remove(skill.getId());
		this.setExp(skill, 0);
	}
	
	/**
	 * Gets a list of the skills this player has explicitely specialised in
	 * not the ones they are automatically specialised in.
	 * @return {Skill[]} the skills this player has specialised in
	 */
	public List<Skill> getSpecialisedSkills()
	{		
		List<Skill> ret = new ArrayList<Skill>();
		for (String i : this.specialised)
		{
			Skill skill = SkillColl.get().get(i);
			if (skill != null) ret.add(skill);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// SPECIALISATION: SLOTS
	// -------------------------------------------- //
	
	/**
	 * Gets the maximum amount of specialisation slots
	 * this player could have open at once, based on permissions.
	 * @return {int} maximum possible amount of open specialisation slots
	 */
	public int getMaxSpecialisationSlots()
	{
		return Derius.get().getSpSlotMixin().getMaxSlots(this);
	}
	
	/**
	 * Gets the amount of open specialisation slots this player has
	 * @return
	 */
	public int getOpenSpecialisationSlots()
	{
		return this.getMaxSpecialisationSlots() - this.getSpecialisedSkills().size();
	}
	
	// -------------------------------------------- //
	// FIELD: ABILITY ACTIVATION COOLDOWN
	// -------------------------------------------- //
	
	/**
	 * This cooldown is for activating abilities.
	 * Sets users time when the global cooldown should expire.
	 * this is system millis
	 * @param {long} the cooldown to set it to
	 */
	public void setCooldownExpire( long cooldownTime) { this.cooldown = cooldownTime; }
	
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
	 * Gets players cooldown.
	 * this is system millis
	 * @return {long} players global cooldown
	 */
	public long getCooldownExpire() { return cooldown; }
	
	/**
	 * Gets amount of milliseconds till cooldown expire
	 * @return {long} amount of milliseconds till cooldown expire
	 */
	public long getCooldownExpireIn() { return cooldown - System.currentTimeMillis(); }
	
	/**
	 * Checks whether the cooldown has expired or not
	 * @return {boolean} true if cooldown has expired
	 */
	public boolean isCooldownExpired ()
	{
		return System.currentTimeMillis() >= getCooldownExpire();
	}
	
	// -------------------------------------------- //
	// FIELD: SPECIALISATION COOLDOWN
	// -------------------------------------------- //
	
	/**
	 * Gets the last time a player either specialised or unspecialised in a skill
	 * This is used so players don't change their specialisation all the time.
	 * @return {long} system millis for last specialisation change
	 */
	public long getSpecialisationChangeMillis() { return this.specialisedMillis; }
	
	/**
	 * Sets the last time a player either specialised or unspecialised in a skill
	 * This is used so players don't change their specialisation all the time.
	 * @param {long} system millis for last specialisation change
	 */
	public void setSpecialisationChangeMillis(long millis) { this.specialisedMillis = millis; }
	
	/**
	 * Gets when the specialisation cooldown will expire
	 * This is the last time they changed any specialisation + some time modfied by server owner
	 * This is used so players don't change their specialisation all the time.
	 * @return {long} system millis for last specialisation change + some cooldown
	 */
	public long getSpecialisationCooldownExpire() { return this.specialisedMillis + MConf.get().specialisationCooldown; }
	
	/**
	 * Checks whether the specialisation cooldown has expired
	 * @return {boolean} true if specialisation cooldown has expired
	 */
	public boolean isSpecialisationCooldownExpired() { return this.getSpecialisationCooldownExpire() < System.currentTimeMillis(); }
	
	// -------------------------------------------- //
	// FIELD: PREPARED TOOL
	// -------------------------------------------- //
	
	/**
	 * Gets the tool which the user has prepared
	 * this is used for activating active abilities
	 * @return {Material} the tool the player has prepared
	 */
	public Optional<Material> getPreparedTool() { return preparedTool; }

	/**
	 * Sets the tool which the user has prepared
	 * this is used for activating active abilities
	 * @param {Material} the tool the player will have prepared
	 */
	public void setPreparedTool(final Optional<Material> tool)
	{
		if (tool.isPresent())
		{
			if ( ! this.isCooldownExpired()) { setPreparedTool(Optional.empty()); }
			if (this.hasActivatedAny() || this.getPreparedTool().isPresent())	return;
			if ( ! Listener.isRegistered(tool.get())) return;
		
			ChatUtil.msgToolPrepared(this, tool.get());
			this.preparedTool = tool;
			Bukkit.getScheduler().runTaskLaterAsynchronously(Derius.get(), () -> setPreparedTool(Optional.empty()), 20*2);
		}
		else 
		{
			if(this.getPreparedTool().isPresent() && ! this.hasActivatedAny())
			{
				ChatUtil.msgToolNotPrepared(this, this.getPreparedTool().get());
			}
			this.preparedTool = Optional.empty();
		}
		
	}
	
	// -------------------------------------------- //
	// ABILITIES
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not the player has ANY abilities activated.
	 * @return {boolean} true if the player has ANY abilities activated.
	 */
	public boolean hasActivatedAny() { return this.activatedAbility != null; }
	
	/**
	 * Gets the id of the activated ability
	 * null if no ability is activated
	 * @return {Ability} the ability which is activated. null if none
	 */
	public Ability getActivatedAbility() { return this.activatedAbility; }
	
	/**
	 * Gets the id of the activated ability
	 * null if no ability is activated
	 * @return {Ability} the ability which is activated. null if none
	 */
	public void setActivatedAbility(Ability ability) { this.activatedAbility = ability; }

	// -------------------------------------------- //
	// MANAGING CHAT | ACTIVATION
	// -------------------------------------------- //
	
	/**
	 * Gets the boolean if someone is listening to ability activation through chat
	 * @return {Boolean} returns if listening.
	 */
	public boolean getIsListeningToChat() { return this.isListeningToChat; }
	
	/**
	 * Sets the boolean if someone is listening to ability activation through chat
	 * @param {boolean} set whether it should listen to chat or not.
	 */
	public void setIsListeningToChat (boolean state) { this.isListeningToChat = state; }
	
	// Adding, getting, setting to the chatKeys map
	
	/**
	 * Adds an entry to the mplayers chatkey map
	 * @param {String} The key that the Chat should listen for
	 * @param {Ability} the ability it should activate
	 */
	public void addChatKey(String key, Ability ability) { this.chatKeys.put(key, ability.getId()); }
	
	/**
	 * Removes an entry to the mplayers chatkey map
	 * @param {String} The key and its ability that should be removed
	 */
	public void removeChatKey (String key) { this.chatKeys.remove(key); }
	
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
	public void clearChatKeys() { this.chatKeys.clear(); }
	
	/**
	 * Checks whether this String is already in use or not
	 * @param {String} the key you want to check for
	 * @return {boolean} if it is or is not a chat key
	 */
	public boolean isAlreadyChatKey(String key)
	{
		for (String string :  this.chatKeys.keySet())
		{
			if (string.equalsIgnoreCase(key)) return true;
		}
		return false;
	}
	/**
	 * Has the mplayer any chat keys registered?
	 * @return {boolean} whether it is empty or nottil.InstantiateSkill(skill, mplayer);
	 */
	public boolean hasAnyChatKeys () { return ! this.chatKeys.isEmpty(); }
	
	/**
	 * 
	 * @param {String} The message to check for
	 * @return {Ability} the ability we get from this message
	 */
	public Ability getAbilityBySubString (String message)
	{
		int pos = -1;
		
		for (String key : this.getChatKeys())
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
		String id = this.chatKeys.get(key);
		if (null == id) return null;
		
		return AbilityColl.get().get(id);
	}
	
	/**
	 * Gets the map of this mplayer as a list of strings
	 * @return {List<String>} the list of all the map data
	 */
	public List<String> chatKeysToString()
	{
		List<String> messages = new ArrayList<String>();	
		for(String str: chatKeys.keySet())
		{
			messages.add(Txt.parse("<lime>" + str + "<i> activates " + getAbilityfromChatKey(str).toString()));
		}
		return messages;
	}

	/**
	 * Checks whether a player qualifies for chatListening
	 * @return {boolean} are the conditions met?
	 */
	public boolean isChatListeningOk()
	{
		return this.getIsListeningToChat() && this.hasAnyChatKeys();
	}
	
	// -------------------------------------------- //
	// CLEAN
	// -------------------------------------------- //
	
	/**
	 * Cleans player for skills & abilities with this id
	 * cleans even if the skill or ability exists
	 * @param {int} id to clean
	 */
	public void cleanNoCheck(String id)
	{
		this.exp.remove(id);
		this.specialised.remove(id);
		for (Entry<String, String> entry : this.chatKeys.entrySet())
		{
			if (entry.getKey().equals(new Integer(id)))
			 {
				 this.chatKeys.remove(entry.getKey());
			 }
		}
	}
	
	/**
	 * Cleans player for skills & abilities with this id
	 * doesn't clean if skill or ability exists
	 * @param {int} id to clean
	 */
	public void cleanWithCheck(String id)
	{
		if (SkillColl.get().get(id) == null)
		{
			this.exp.remove(id);
			this.specialised.remove(id);
		}
		
		if (AbilityColl.get().get(id) == null)
		{
			for (Entry<String, String> entry : this.chatKeys.entrySet())
			{
				if (entry.getKey().equals(new Integer(id)))
				 {
					 this.chatKeys.remove(entry.getKey());
				 }
			}
		}
	}
	
	/**
	 * Cleans player for all skills and abilities
	 * even if those skills/abilities exists
	 */
	public void cleanAllNoCheck()
	{
		for (String id : this.exp.keySet())
		{
			this.cleanNoCheck(id);
		}
		for (String id : this.specialised)
		{
			this.cleanNoCheck(id);
		}
		for (String id : this.chatKeys.values())
		{
			this.cleanNoCheck(id);
		}
	}
	
	/**
	 * Cleans player for all skills and abilities
	 * but not if those skills/abilities exists
	 */
	public void cleanAllWithCheck()
	{
		for (String id : this.exp.keySet())
		{
			this.cleanWithCheck(id);
		}
		for (String id : this.specialised)
		{
			this.cleanWithCheck(id);
		}
		for (String id : this.chatKeys.values())
		{
			this.cleanWithCheck(id);
		}
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		return obj == this;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		result += this.getId().hashCode();
		return result;
	}
	
	// -------------------------------------------- //
	// RAW DATA
	// -------------------------------------------- //
	
	/**
	 * DON'T USE THIS IT IS EXTREMELY DANGEROUS USE
	 * THIS IS ONLY FOR INTERNAL DEBUG USE
	 * @deprecated can change at any given time might be removed, extremely dangerous
	 */
	@Deprecated
	public Map<String, Long> getRawExpData() { return this.exp; }
	
	/**
	 * DON'T USE THIS IT IS EXTREMELY DANGEROUS USE
	 * THIS IS ONLY FOR INTERNAL DEBUG USE
	 * @deprecated can change at any given time might be removed, extremely dangerous
	 */
	@Deprecated
	public Set<String> getRawSpecialisedData() { return this.specialised; }
	
}