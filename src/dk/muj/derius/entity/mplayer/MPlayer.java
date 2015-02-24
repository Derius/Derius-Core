package dk.muj.derius.entity.mplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang.Validate;
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
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.ability.AbilityColl;
import dk.muj.derius.entity.skill.SkillColl;
import dk.muj.derius.events.PlayerStaminaAddBonusEvent;
import dk.muj.derius.events.PlayerExpAddEvent;
import dk.muj.derius.events.PlayerStaminaAddEvent;
import dk.muj.derius.events.PlayerLevelDownEvent;
import dk.muj.derius.events.PlayerLevelUpEvent;
import dk.muj.derius.events.PlayerToolPrepareEvent;
import dk.muj.derius.events.PlayerStaminaTakeBonusEvent;
import dk.muj.derius.events.PlayerExpTakeEvent;
import dk.muj.derius.events.PlayerStaminaTakeEvent;
import dk.muj.derius.events.PlayerToolUnprepareEvent;
import dk.muj.derius.events.SpecialisationSlotEvent;
import dk.muj.derius.scoreboard.ScoreboardUtil;
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
		if (that == null || that == this) return that;
		this.exp = that.exp;
		this.specialised = that.specialised;
		this.specialisedMillis = that.specialisedMillis;
		this.isListeningToChat = that.isListeningToChat;
		this.chatKeys = that.chatKeys;
		this.stamina = that.stamina;
		this.bonusStamina = that.bonusStamina;
		this.staminaBoardStay = that.staminaBoardStay;
		this.boardShowAtAll = that.boardShowAtAll;
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	//			id		exp
	protected Map<String, Long> exp = new HashMap<String, Long>();
	
	protected Set<String> specialised = new CopyOnWriteArraySet<String>();
	
	private long specialisedMillis = 0;
	
	protected Map<String, Integer> specialisationBonus = new HashMap<>();
	
	// A boolean that defines whether the player wants to activate abilities by chat or not.
	private boolean isListeningToChat = false;
	
	// A Map that stores which string a player types in chat should activate what ability.
	protected Map<String, String> chatKeys = new HashMap<String, String>();
	
	// Global Cooldown for all the skills/abilities (exhaustion), individual cooldowns can be added by the skill writer
	// Long is the millis when the abilitys cooldown expires.
	private transient long cooldown = 0;
	
	// Which Ability is currently activated.
	private transient Optional<Ability> activatedAbility = Optional.empty();
	
	// The tool which the user has prepared.
	// A tool is prepared by right clicking, then can activate abilities
	private transient Optional<Material> preparedTool = Optional.empty();
	
	// The stamina of the player
	protected double stamina = 100.0;
	
	// The maximal stamina of a player
	protected double bonusStamina = 0.0;
	
	// The boolean whether the board doesn't fade out
	protected boolean staminaBoardStay = false;
	
	// Whether the scoreboard should be used at all or not.
	protected boolean boardShowAtAll = true;
	
	// -------------------------------------------- //
	// FIELD: EXP
	// -------------------------------------------- //
	
	private void setExp(Skill skill, long exp) { this.exp.put(skill.getId(), exp); }
	
	public long getExp(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return this.exp.get(skill.getId());
	}
	
	public void addExp(Skill skill, long exp)
	{
		Validate.notNull(skill, "skill mustn't be null");
		Validate.isTrue(exp > 0, "Exp values must be positive");
		
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore >= this.getMaxLevel(skill)) return;
		
		PlayerExpAddEvent event = new PlayerExpAddEvent(this, skill, exp);
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
		Validate.notNull(skill, "skill mustn't be null");
		Validate.isTrue(exp > 0, "Exp values must be positive");
		
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore <= 0) return;
		
		PlayerExpTakeEvent event = new PlayerExpTakeEvent(this, skill, exp);
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
	// FIELD: STAMINA
	// -------------------------------------------- //
	
	// Raw
	private void setStamina(double newStamina)
	{
		Validate.isTrue(newStamina > -0.001, "Stamina value must be positive.");
		Validate.isTrue(Double.isFinite(newStamina), "Stamina value must be finite.");
		Validate.isTrue( ! Double.isNaN(newStamina), "Stamina value must be a number.");
		
		if (Math.round(newStamina) == Math.round(this.getStamina())) return;
		
		double max = DeriusCore.getStaminaMixin().getMax(this);
		double min = 0.0;
		
		newStamina = Math.max(newStamina, min);
		newStamina = Math.min(newStamina, max);
		
		this.stamina = newStamina;
		this.changed();
		ScoreboardUtil.updateStaminaScore(this, MConf.get().staminaBoardStay);
	}
	
	public double getStamina() { return this.stamina; }
	
	// Finer
	public void addStamina(double stamina)
	{
		Validate.isTrue(stamina > -0.001, "Stamina value must be positive.");
		Validate.isTrue(Double.isFinite(stamina), "Stamina value must be finite.");
		Validate.isTrue( ! Double.isNaN(stamina), "Stamina value must be a number.");
		
		PlayerStaminaAddEvent event = new PlayerStaminaAddEvent(this, stamina);
		if ( ! event.runEvent()) return;
		
		double staminaAfter = this.getStamina() + event.getStaminaAmount();
		this.setStamina(staminaAfter);
	}
	
	public void takeStamina(double stamina)
	{
		Validate.isTrue(stamina > -0.001, "Stamina value must be positive.");
		Validate.isTrue(Double.isFinite(stamina), "Stamina value must be finite.");
		Validate.isTrue( ! Double.isNaN(stamina), "Stamina value must be a number.");
		
		PlayerStaminaTakeEvent event = new PlayerStaminaTakeEvent(this, stamina);
		if ( ! event.runEvent()) return;
		
		double staminaAfter = this.getStamina() - event.getStaminaAmount();
		this.setStamina(staminaAfter);
	}
	
	public boolean hasEnoughStamina(double amount)
	{
		return this.getStamina() >= amount;
	}
	
	// -------------------------------------------- //
	// FIELD: STAMINASCORE
	// -------------------------------------------- //
	
	public void setStaminaBoardStay(boolean value) { this.staminaBoardStay = value; }
	
	public boolean getStaminaBoardStay() { return this.staminaBoardStay; }
	
	// -------------------------------------------- //
	// FIELD: STAMINASCORE
	// -------------------------------------------- //
	
	public void setBoardShowAtAll(boolean value) { this.boardShowAtAll = value; }
	
	public boolean getBoardShowAtAll() { return this.boardShowAtAll; }
	
	// -------------------------------------------- //
	// FIELD: bonusStamina
	// -------------------------------------------- //
	
	private void setBonusStamina(double bonusStamina) { this.bonusStamina = bonusStamina; }
	
	public double getBonusStamina() { return this.bonusStamina; }
	
	public void addBonusStamina(double bonusStamina)
	{
		Validate.isTrue(bonusStamina > 0.0, "BonusStamina value must be positive.");
		
		double highCap = MConf.get().bonusStaminaMax + MConf.get().staminaMax;
		double staminaMax = DeriusCore.getStaminaMixin().getMax(this);
		
		if (staminaMax >= highCap) return;
		
		double bonusStaminaAfter = staminaMax + bonusStamina;
		bonusStaminaAfter = Math.min(highCap, bonusStaminaAfter);

		PlayerStaminaAddBonusEvent event = new PlayerStaminaAddBonusEvent(this, stamina);
		event.run();
		if (event.isCancelled()) return;
		
		this.setBonusStamina(bonusStaminaAfter);
		this.changed();
	}
	
	public void takeBonusStamina(double bonusStamina)
	{
		Validate.isTrue(bonusStamina > 0.0, "BonusStamina value must be positive.");
		double min = MConf.get().bonusStaminaMin;
		
		if (this.getBonusStamina() <= min) return;
		
		double bonusStaminaAfter = this.getBonusStamina() - bonusStamina;
		bonusStaminaAfter = Math.min(min, bonusStaminaAfter);

		PlayerStaminaTakeBonusEvent event = new PlayerStaminaTakeBonusEvent(this, stamina);
		event.run();
		if (event.isCancelled()) return;
		
		this.setBonusStamina(bonusStaminaAfter);
		this.changed();
	}
	
	// -------------------------------------------- //
	// LEVEL
	// -------------------------------------------- //
	
	public LvlStatus getLvlStatus(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return skill.getLvlStatusFromExp(this.getExp(skill));
	}
	
	public int getLvl(Skill skill) { return this.getLvlStatus(skill).getLvl(); }
	
	public int getMaxLevel(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return DeriusCore.getMaxLevelMixin().getMaxLevel(this, skill);
	}
	
	// -------------------------------------------- //
	// SKILL
	// -------------------------------------------- //
	
	public void instantiateSkill(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		if (this.hasSkill(skill)) return;
		this.exp.put(skill.getId(), 0L);
	}
	
	public boolean hasSkill(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return this.exp.containsKey(skill.getId());
	}
	
	// -------------------------------------------- //
	// SPECIALISATION
	// -------------------------------------------- //
	
	public boolean isSpecialisedIn(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return  this.specialised.contains(skill.getId()) || skill.isSpAutoAssigned();
	}
	
	public boolean setSpecialisedIn(Skill skill, boolean verbooseNot)
	{
		Validate.notNull(skill, "skill mustn't be null");
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
		Validate.notNull(skill, "skill mustn't be null");
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
	
	@Override
	public int getMaxSpecialisationSlots()
	{
		SpecialisationSlotEvent event = new SpecialisationSlotEvent(this);
		event.run();
		int ret = MConf.get().baseSpSlot;
		
		for (Integer bonus : this.getSpecialisationSlotBonus().values())
		{
			if (bonus == null) continue;
			ret += bonus.intValue();
		}
		return ret;
	}
	
	@Override
	public Map<String, Integer> getSpecialisationSlotBonus()
	{
		return specialisationBonus;
	}
	
	@Override
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
		Validate.notNull(tool, "Tool mustn't be null, it in an optional for gods sake.");
		if (tool.isPresent())
		{
			if ( ! this.isCooldownExpired()) setPreparedTool(Optional.empty());
			if (this.hasActivatedAny() || this.getPreparedTool().isPresent()) return;
			if ( ! Listener.isRegistered(tool.get())) return;
		
			PlayerToolPrepareEvent event = new PlayerToolPrepareEvent(tool.get(), this);
			event.run();
			if (event.isCancelled()) return;
			this.preparedTool = tool;
			Bukkit.getScheduler().runTaskLaterAsynchronously(DeriusCore.get(), () -> setPreparedTool(Optional.empty()), 20*2);
		}
		else 
		{
			if (this.getPreparedTool().isPresent() && ! this.hasActivatedAny())
			{
				PlayerToolUnprepareEvent event = new PlayerToolUnprepareEvent(this.getPreparedTool().get(), this);
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
	
	public Optional<Ability> getActivatedAbility() { return this.activatedAbility; }
	
	public void setActivatedAbility(Optional<Ability> ability) { this.activatedAbility = ability; }

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
