package dk.muj.derius.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.LvlStatus;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.ability.AbilityColl;
import dk.muj.derius.entity.skill.SkillColl;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerLevelDownEvent;
import dk.muj.derius.events.PlayerLevelUpEvent;
import dk.muj.derius.events.PlayerPrepareToolEvent;
import dk.muj.derius.events.PlayerTakeExpEvent;
import dk.muj.derius.events.PlayerUnprepareToolEvent;
import dk.muj.derius.util.Listener;

public class MPlayer extends SenderEntity<MPlayer> implements DPlayer
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static DPlayer get(Object oid)
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
	
	public long getExp(Skill skill) { return this.exp.get(skill.getId()); }
	
	public void addExp(Skill skill, long exp)
	{
		if (exp < 0) throw new IllegalArgumentException("Exp values must be positive");
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore >= this.getMaxLevel(skill)) return;
		
		PlayerAddExpEvent event = new PlayerAddExpEvent(this, skill, exp);
		event.run();
		if (event.isCancelled()) return;
		exp = MUtil.probabilityRound(event.getExpAmount());
		this.setExp(skill, this.getExp(skill) + exp);
		
		int lvlAfter = this.getLvl(skill);
		if (lvlBefore != lvlAfter)
		{
			PlayerLevelUpEvent lvlUp = new PlayerLevelUpEvent(this, skill);
			lvlUp.run();
		}
	}
	
	public void takeExp(Skill skill, long exp)
	{
		if (exp < 0) throw new IllegalArgumentException("Exp values must be positive");
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore <= 0) return;
		
		PlayerTakeExpEvent event = new PlayerTakeExpEvent(this,skill,exp);
		event.run();
		if (event.isCancelled()) return;
		exp = MUtil.probabilityRound(event.getExpAmount());
		this.setExp(skill, this.getExp(skill) - exp);
		
		int lvlAfter = this.getLvl(skill);
		if (lvlBefore != lvlAfter)
		{
			PlayerLevelDownEvent lvlDown = new PlayerLevelDownEvent(this, skill);
			lvlDown.run();
		}
	}
	
	// -------------------------------------------- //
	// LEVEL
	// -------------------------------------------- //
	
	public LvlStatus getLvlStatus(Skill skill) { return skill.getLvlStatusFromExp(this.getExp(skill)); }
	
	public int getLvl(Skill skill) { return this.getLvlStatus(skill).getLvl(); }
	
	public int getMaxLevel(Skill skill) { return DeriusCore.getMaxLevelMixin().getMaxLevel(this, skill); }
	
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
	
	public boolean isSpecialisedIn(Skill skill)
	{
		return  this.specialised.contains(skill.getId()) || skill.isSpAutoAssigned();
	}
	
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
	
	
	public void setNotSpecialisedIn(Skill skill)
	{	
		this.specialised.remove(skill.getId());
		this.setExp(skill, 0);
	}
	
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
	
	public int getMaxSpecialisationSlots()
	{
		return DeriusCore.getSpSlotMixin().getMaxSlots(this);
	}
	
	public int getOpenSpecialisationSlots()
	{
		return this.getMaxSpecialisationSlots() - this.getSpecialisedSkills().size();
	}
	
	// -------------------------------------------- //
	// FIELD: ABILITY ACTIVATION COOLDOWN
	// -------------------------------------------- //
	
	public void setCooldownExpire( long cooldownTime) { this.cooldown = cooldownTime; }
	
	public void setCooldownExpireIn (int ticks)
	{
		long currentTime = System.currentTimeMillis();
		setCooldownExpire(currentTime+ticks/20*1000);
	}
	
	public long getCooldownExpire() { return cooldown; }
	
	public long getCooldownExpireIn() { return cooldown - System.currentTimeMillis(); }
	
	public boolean isCooldownExpired ()
	{
		return System.currentTimeMillis() >= getCooldownExpire();
	}
	
	// -------------------------------------------- //
	// FIELD: SPECIALISATION COOLDOWN
	// -------------------------------------------- //
	
	public long getSpecialisationChangeMillis() { return this.specialisedMillis; }
	
	public void setSpecialisationChangeMillis(long millis) { this.specialisedMillis = millis; }
	
	public long getSpecialisationCooldownExpire() { return this.specialisedMillis + MConf.get().specialisationCooldown; }
	
	public boolean isSpecialisationCooldownExpired() { return this.getSpecialisationCooldownExpire() < System.currentTimeMillis(); }
	
	// -------------------------------------------- //
	// FIELD: PREPARED TOOL
	// -------------------------------------------- //
	
	public Optional<Material> getPreparedTool() { return preparedTool; }

	public void setPreparedTool(final Optional<Material> tool)
	{
		if (tool.isPresent())
		{
			if ( ! this.isCooldownExpired()) setPreparedTool(Optional.empty());
			if (this.hasActivatedAny() || this.getPreparedTool().isPresent()) return;
			if ( ! Listener.isRegistered(tool.get())) return;
		
			PlayerPrepareToolEvent event = new PlayerPrepareToolEvent(tool.get(), this);
			event.run();
			if (event.isCancelled()) return;
			this.preparedTool = tool;
			Bukkit.getScheduler().runTaskLaterAsynchronously(DeriusCore.get(), () -> setPreparedTool(Optional.empty()), 20*2);
		}
		else 
		{
			if (this.getPreparedTool().isPresent() && ! this.hasActivatedAny())
			{
				PlayerUnprepareToolEvent event = new PlayerUnprepareToolEvent(this.getPreparedTool().get(), this);
				event.run();
				if (event.isCancelled()) return;
			}
			this.preparedTool = Optional.empty();
		}
		
	}
	
	// -------------------------------------------- //
	// ABILITIES
	// -------------------------------------------- //
	
	public boolean hasActivatedAny() { return this.activatedAbility != null; }
	
	public Ability getActivatedAbility() { return this.activatedAbility; }
	
	public void setActivatedAbility(Ability ability) { this.activatedAbility = ability; }

	// -------------------------------------------- //
	// MANAGING CHAT | ACTIVATION
	// -------------------------------------------- //
	
	public boolean getIsListeningToChat() { return this.isListeningToChat; }
	
	public void setIsListeningToChat (boolean state) { this.isListeningToChat = state; }
	
	// Adding, getting, setting to the chatKeys map
	
	public void addChatKey(String key, Ability ability) { this.chatKeys.put(key, ability.getId()); }
	
	public void removeChatKey (String key) { this.chatKeys.remove(key); }
	
	public List<String> getChatKeys()
	{
		List<String> list = new ArrayList<String>();
		list.addAll(this.chatKeys.keySet());
		return list;
	}
	
	public void clearChatKeys() { this.chatKeys.clear(); }
	
	public boolean isAlreadyChatKey(String key)
	{
		for (String string :  this.chatKeys.keySet())
		{
			if (string.equalsIgnoreCase(key)) return true;
		}
		return false;
	}
	
	public boolean hasAnyChatKeys () { return ! this.chatKeys.isEmpty(); }
	
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
	
	public Ability getAbilityfromChatKey(String key)
	{
		String id = this.chatKeys.get(key);
		if (null == id) return null;
		
		return AbilityColl.get().get(id);
	}
	
	public List<String> chatKeysToString()
	{
		List<String> messages = new ArrayList<String>();	
		for(String str: chatKeys.keySet())
		{
			messages.add(Txt.parse("<lime>" + str + "<i> activates " + getAbilityfromChatKey(str).toString()));
		}
		return messages;
	}

	public boolean isChatListeningOk()
	{
		return this.getIsListeningToChat() && this.hasAnyChatKeys();
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