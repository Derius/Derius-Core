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

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.events.SpecialisationSlotEvent;
import dk.muj.derius.api.events.StaminaMaxEvent;
import dk.muj.derius.api.events.player.PlayerExpAddEvent;
import dk.muj.derius.api.events.player.PlayerExpTakeEvent;
import dk.muj.derius.api.events.player.PlayerLevelDownEvent;
import dk.muj.derius.api.events.player.PlayerLevelUpEvent;
import dk.muj.derius.api.events.player.PlayerStaminaAddEvent;
import dk.muj.derius.api.events.player.PlayerStaminaTakeEvent;
import dk.muj.derius.api.events.player.PlayerToolPrepareEvent;
import dk.muj.derius.api.events.player.PlayerToolUnprepareEvent;
import dk.muj.derius.api.lvl.LvlStatus;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.util.ScoreboardUtil;

public class MPlayer extends SenderEntity<MPlayer> implements DPlayer
{	
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
		this.stamina = that.stamina;
		this.staminaBonus = that.staminaBonus;
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
	@Override public long getSpecialisationChangeMillis() { return this.specialisedMillis; }
	@Override public void setSpecialisationChangeMillis(long millis) { this.specialisedMillis = millis; this.changed();}
	@Override public long getSpecialisationCooldownExpire() { return this.specialisedMillis + MConf.get().specialisationCooldown; }
	@Override public boolean isSpecialisationCooldownExpired() { return this.getSpecialisationCooldownExpire() < System.currentTimeMillis(); }
	
	protected Map<String, Integer> specialisationBonus = new HashMap<>();

	// Global Cooldown for all the skills/abilities (exhaustion), individual cooldowns can be added by the skill writer
	// Long is the millis when the abilitys cooldown expires.
	private transient long cooldown = 0;
	@Override public void setCooldownExpire( long cooldownTime) { this.cooldown = cooldownTime; }
	@Override public long getCooldownExpire() { return cooldown; }
	@Override public long getCooldownExpireIn() { return cooldown - System.currentTimeMillis(); }
	
	// Which Ability<?> is currently activated.
	private transient Optional<Ability<?>> activatedAbility = Optional.empty();
	@Override public boolean hasActivatedAny() { return this.activatedAbility.isPresent(); }
	@Override public Optional<Ability<?>> getActivatedAbility() { return this.activatedAbility; }
	@Override public void setActivatedAbility(Optional<Ability<?>> ability) { this.activatedAbility = ability;}
	
	// The tool which the user has prepared.
	// A tool is prepared by right clicking, then can activate abilities
	private transient Optional<Material> preparedTool = Optional.empty();
	
	// The stamina of the player
	protected transient double stamina = 100.0;
	
	protected transient Map<String, Double> staminaBonus = new HashMap<>();
	@Override public Map<String, Double> getStaminaBonus() { return staminaBonus; }
	
	// The boolean whether the board doesn't fade out
	protected boolean staminaBoardStay = false;
	@Override public void setStaminaBoardStay(boolean value) { this.staminaBoardStay = value; this.changed();}
	@Override public boolean getStaminaBoardStay() { return this.staminaBoardStay; }
	
	// Whether the scoreboard should be used at all or not.
	protected boolean boardShowAtAll = true;
	@Override public void setBoardShowAtAll(boolean value) { this.boardShowAtAll = value; this.changed();}
	@Override public boolean getBoardShowAtAll() { return this.boardShowAtAll; }
	
	// -------------------------------------------- //
	// FIELD: EXP
	// -------------------------------------------- //
	
	public void setExp(Skill skill, long exp) { this.exp.put(skill.getId(), exp); this.changed();}
	
	@Override
	public long getExp(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		if (this.exp == null) throw new RuntimeException("exp is null");
		this.instantiateSkill(skill);
		return this.exp.get(skill.getId());
	}
	
	@Override
	public void addExp(Skill skill, double exp)
	{
		Validate.notNull(skill, "skill mustn't be null");
		if (exp < 0)
		{
			this.takeExp(skill, Math.abs(exp));
			return;
		}
		
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore >= this.getMaxLevel(skill)) return;
		
		PlayerExpAddEvent event = new PlayerExpAddEvent(this, skill, exp);
		event.run();
		if (event.isCancelled()) return;
		
		long eventExp = MUtil.probabilityRound(event.getExpAmount());
		this.setExp(skill, this.getExp(skill) + eventExp);
		
		int lvlAfter = this.getLvl(skill);
		if (lvlBefore != lvlAfter)
		{
			PlayerLevelUpEvent lvlUp = new PlayerLevelUpEvent(this, skill);
			lvlUp.run();
		}
	}
	
	@Override
	public void takeExp(Skill skill, double exp)
	{
		Validate.notNull(skill, "skill mustn't be null");
		if (exp < 0)
		{
			this.addExp(skill, Math.abs(exp));
			return;
		}
		
		int lvlBefore = this.getLvl(skill);
		
		if (lvlBefore <= 0) return;
		
		PlayerExpTakeEvent event = new PlayerExpTakeEvent(this, skill, exp);
		event.run();
		if (event.isCancelled()) return;
		long eventExp = MUtil.probabilityRound(event.getExpAmount());
		this.setExp(skill, this.getExp(skill) - eventExp);
		
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
	public void setStamina(double newStamina)
	{
		Validate.isTrue(Double.isFinite(newStamina), "Stamina value must be finite.");
		Validate.isTrue( ! Double.isNaN(newStamina), "Stamina value must be a number.");
		
		double max = this.getStaminaMax();
		double min = 0.0;
		
		newStamina = Math.max(newStamina, min);
		newStamina = Math.min(newStamina, max);
		
		double oldStamina = this.getStamina();
		
		// In case of minimal change we won't update.
		if (newStamina == oldStamina) return;
		
		this.stamina = newStamina;
		
		if (Math.round(newStamina) == Math.round(oldStamina)) return;
		
		if (this.isPlayer())
		{
			ScoreboardUtil.updateStaminaScore(this, MConf.get().staminaBoardStay, newStamina);
		}
		
		return;
	}
	
	@Override public double getStamina() { return this.stamina; }
	
	// Finer
	@Override
	public void addStamina(double stamina)
	{
		if (stamina < 0) this.takeStamina(-stamina);
		Validate.isTrue(Double.isFinite(stamina), "Stamina value must be finite.");
		Validate.isTrue( ! Double.isNaN(stamina), "Stamina value must be a number.");
		
		PlayerStaminaAddEvent event = new PlayerStaminaAddEvent(this, stamina);
		if ( ! event.runEvent()) return;
		stamina = event.getStaminaAmount();
		
		double staminaAfter = this.getStamina() + stamina;
		this.setStamina(staminaAfter);
		
		return;
	}
	
	@Override
	public void takeStamina(double stamina)
	{
		if (stamina < 0) this.addStamina(-stamina);
		Validate.isTrue(Double.isFinite(stamina), "Stamina value must be finite.");
		Validate.isTrue( ! Double.isNaN(stamina), "Stamina value must be a number.");
		
		PlayerStaminaTakeEvent event = new PlayerStaminaTakeEvent(this, stamina);
		if ( ! event.runEvent()) return;
		stamina = event.getStaminaAmount();
		
		double staminaAfter = this.getStamina() - stamina;
		this.setStamina(staminaAfter);
		
		return;
	}
	
	// We have a task running 10 times per second.
	// It calls this method, it sets stamina (also calls this)
	// and it updates the socreboard (which alo calls this)
	// We get this value atlast 30 times per second.
	// So we cach it and ask for a new value every once in a while.
	public static final transient int STAMINA_MAX_ATTEMPTS = 100;
	private transient int staminaLastCall = STAMINA_MAX_ATTEMPTS-1;
	private transient double staminaMaxCach = 0;
	@Override
	public double getStaminaMax()
	{
		// Use the cache
		staminaLastCall++;
		if (this.staminaLastCall % STAMINA_MAX_ATTEMPTS != 0)
		{
			return staminaMaxCach;
		}
		
		// Now actual try
		double ret = 0.0;
		
		ret += MConf.get().staminaMax;
	
		// Event to update values
		StaminaMaxEvent event = new StaminaMaxEvent(this);
		event.run();
		
		for (Double bonus : this.getStaminaBonus().values())
		{
			// Bonus is valid
			if (bonus == null) continue;
			if (bonus.isNaN()) continue;
			if (bonus.isInfinite()) continue;
			
			ret += bonus;
		}
		
		staminaMaxCach = ret;
		return ret;
	}
	
	// -------------------------------------------- //
	// LEVEL
	// -------------------------------------------- //
	
	@Override
	public LvlStatus getLvlStatus(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return skill.getLvlStatusFromExp(this.getExp(skill));
	}
	
	@Override public int getLvl(Skill skill) { return this.getLvlStatus(skill).getLvl(); }
	
	@Override
	public int getMaxLevel(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return DeriusAPI.getMaxLevelMixin().getMaxLevel(this, skill);
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
	
	@Override
	public boolean isSpecialisedIn(Skill skill)
	{
		Validate.notNull(skill, "skill mustn't be null");
		return this.specialised.contains(skill.getId()) || skill.isSpAutoAssigned();
	}
	
	@Override
	public boolean setSpecialisedIn(Skill skill, boolean verbooseNot)
	{
		Validate.notNull(skill, "skill mustn't be null");
		for (Req req : skill.getSpecialiseRequirements())
		{
			if ( ! req.apply(this, skill))
			{
				if (verbooseNot) this.sendMessage(req.createErrorMessage(this, skill));
				return false;
			}
		}
		
		this.specialised.add(skill.getId());
		this.changed();
		return true;
	}
	
	@Override
	public void setNotSpecialisedIn(Skill skill)
	{	
		Validate.notNull(skill, "skill mustn't be null");
		this.specialised.remove(skill.getId());
		this.setExp(skill, 0);
		return;
	}
	
	@Override
	public List<Skill> getSpecialisedSkills()
	{		
		List<Skill> ret = new ArrayList<Skill>();
		for (String id : this.specialised)
		{
			Skill skill = DeriusAPI.getSkill(id);
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
			ret += bonus;
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
	// FIELD: PREPARED TOOL
	// -------------------------------------------- //
	
	@Override public Optional<Material> getPreparedTool() { return preparedTool; }
	
	@Override 
	public void setPreparedTool(final Optional<Material> tool)
	{
		Validate.notNull(tool, "Tool mustn't be null, it is an optional for gods sake.");
		
		if (tool.isPresent())
		{
			if (this.hasActivatedAny() || this.getPreparedTool().isPresent()) return;
			if ( ! DeriusAPI.isRegisteredAsPreparable(tool.get())) return;
		
			PlayerToolPrepareEvent event = new PlayerToolPrepareEvent(tool.get(), this);
			if ( ! event.runEvent()) return;
			
			this.preparedTool = tool;
			Bukkit.getScheduler().runTaskLaterAsynchronously(DeriusPlugin.get(), new Runnable()
					{
						@Override
						public void run()
						{
							setPreparedTool(Optional.empty());
						}
				
					}, 20*2);
			
		}
		else 
		{
			if (this.getPreparedTool().isPresent() && ! this.hasActivatedAny())
			{
				PlayerToolUnprepareEvent event = new PlayerToolUnprepareEvent(this.getPreparedTool().get(), this);
				if ( ! event.runEvent()) return;
			}
			this.preparedTool = Optional.empty();
		}
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
