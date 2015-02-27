package dk.muj.derius.entity.ability;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.MillisLastCalculator;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.events.AbilityRegisteredEvent;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.util.AbilityUtil;

public abstract class DeriusAbility extends Entity<DeriusAbility> implements Ability
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	protected boolean enabled = true;
	public boolean isEnabled() { return enabled && this.getSkill().isEnabled(); }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	
	private String name;
	public String getName() { return name; }
	public void setName(String newName) { this.name = newName; }
	
	private String desc = "";
	public String getDesc() { return desc; }
	public void setDesc(String newDescription) { this.desc = newDescription; }
	
	private int millisCooldown = 1000*60*2;
	public void setMillisCooldown(int millis) { this.millisCooldown = millis; }
	public int getCooldownMillis() { return this.millisCooldown; }
	
	private WorldExceptionSet worldsUse = new WorldExceptionSet();
	public WorldExceptionSet getWorldsUse() { return this.worldsUse; }
	public void setWorldsUse(WorldExceptionSet worldsUse) { this.worldsUse = worldsUse; }
	
	private double staminaUsage = 0.0;
	public void setStaminaUsage(double stamina) { this.staminaUsage = stamina; }
	public double getStaminaUsage() { return this.staminaUsage; }
	
	private transient AbilityType type;
	public AbilityType getType() { return this.type; }
	public void setType(AbilityType newType){ this.type = newType; }
	
	protected transient List<Req> seeRequirements = new CopyOnWriteArrayList<Req>();
	public List<Req> getSeeRequirements() { return this.seeRequirements; }
	public void setSeeRequirements(List<Req> requirements) { this.seeRequirements = requirements; }
	public void addSeeRequirements(Req... requirements) { this.seeRequirements.addAll(Arrays.asList(requirements)); }
	
	protected transient List<Req> activateRequirements = new CopyOnWriteArrayList<Req>();
	public List<Req> getActivateRequirements() { return this.activateRequirements; }
	public void setActivateRequirements(List<Req> requirements) { this.activateRequirements = requirements; }
	public void addActivateRequirements(Req... requirements) { this.activateRequirements.addAll(Arrays.asList(requirements)); }
	
	// Lambda
	private transient MillisLastCalculator levelToTicks = level -> ((2 + level/50) * (int) TimeUnit.MILLIS_PER_SECOND);
	public int getDurationMillis(int level) { return this.levelToTicks.calcDuration(level); }
	public final void setDurationAlgorithm(MillisLastCalculator algorithm){ this.levelToTicks = algorithm; }
	public final MillisLastCalculator getDurationAlgorithm(){ return this.levelToTicks; }
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public DeriusAbility load(DeriusAbility that)
	{
		if (that == null || that == this) return that;
		
		this.enabled		= that.enabled;
		this.millisCooldown	= that.millisCooldown;
		this.staminaUsage	= that.staminaUsage;
		
		if (that.name != null) this.name = that.name;
		if (that.desc != null) this.desc = that.desc;
		if (that.worldsUse != null) this.worldsUse = that.worldsUse;
		
		return this;
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	/**
	 * Registers an ability to our system.
	 * This should be done on server startup.
	 */
	public void register()
	{
		AbilityRegisteredEvent event = new AbilityRegisteredEvent(this);
		if ( ! event.runEvent()) return;
		
		if (this.attached()) return;
		
		DeriusAbility old = AbilityColl.get().get(this.getId(), false);
		if (old != null)
		{
			this.load(old);
			AbilityColl.get().removeAtLocal(this.getId());
		}
		
		AbilityColl.get().attach(this, this.getId());
		return;
	}
	
	@Override
	public boolean isRegistered()
	{
		return this.attached();
	}
	
	// -------------------------------------------- //
	// DISPLAY
	// -------------------------------------------- //
	
	public String getDisplayedDescription(Object watcherObject)
	{
		String name = this.getDisplayName(watcherObject);
		if (name == null) return null;
		return Txt.parse(MLang.get().abilityDisplayedDescription, name, this.getDesc());
	}
	
	public String getDisplayName(Object watcherObject)
	{
		DPlayer player = DeriusAPI.getDPlayer(watcherObject);
		if (player == null) return null;
		String color = AbilityUtil.canPlayerActivateAbility(player, this, VerboseLevel.ALWAYS) ? MLang.get().abilityColorPlayerCanUse : MLang.get().abilityColorPlayerCantUse;
		return color + this.getName();
	}

	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Skill getSkill();
		
	public abstract String getId();
	
	public abstract String getLvlDescriptionMsg(int lvl);
	
	public abstract Object onActivate(DPlayer p, Object other);
	public abstract void onDeactivate(DPlayer p, Object other);
	
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
		
		result += (id != null) ? id.hashCode() : 1;
		
		return result;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getName();
	}
	
}
