package dk.muj.derius.entity.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Material;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.LvlStatusDefault;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.LvlStatus;
import dk.muj.derius.api.LvlStatusCalculator;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.util.SkillUtil;

public abstract class DeriusSkill extends Entity<DeriusSkill> implements Skill
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Enabled
	protected boolean enabled = true;
	@Override public boolean isEnabled() { return enabled; }
	@Override public void setEnabled(boolean enabled) { this.enabled = enabled; }
	
	// Abilities
	private transient List<Ability> abilities = new CopyOnWriteArrayList<>();
	@Override public List<Ability> getAbilities() { return this.abilities; }
	
	// -------------------------------------------- //
	// FIELDS: DESCRIPTIVE
	// -------------------------------------------- //
	
	private String name;
	@Override public String getName() { return name; }
	@Override public void setName(String newName) { this.name = newName; }
	
	private String desc = "";
	@Override public String getDesc() { return desc; }
	@Override public void setDesc(String newDescription) { this.desc = newDescription; }
	
	private List<String> earnExpDescs = new CopyOnWriteArrayList<String>();
	@Override public List<String> getEarnExpDescs() { return new ArrayList<>(earnExpDescs); }
	@Override public void setEarnExpDescs(List<String> descs) { this.earnExpDescs = descs; }
	@Override public void addEarnExpDescs(String... desc) { this.earnExpDescs.addAll(Arrays.asList(desc)); }
	
	private Material icon = Material.AIR;
	@Override public Material getIcon() { return this.icon; }
	@Override public void setIcon(Material icon) { this.icon = icon; }
	
	// -------------------------------------------- //
	// FIELDS: CONFIGURIVE
	// -------------------------------------------- //
	
	private int softCap = 1000;
	public int getSoftCap() { return  this.softCap; }
	public void setSoftCap(int cap) { this.softCap = cap; }
	
	private int hardCap = 2000;
	public int getHardCap() { return  this.hardCap; }
	public void setHardtCap(int cap) { this.hardCap = cap; }
	
	private boolean spAutoAssigned = false;
	@Override public boolean isSpAutoAssigned() { return this.spAutoAssigned; }
	@Override public void setSpAutoAssiged(boolean assigned) { this.spAutoAssigned = assigned; }
	
	private boolean spBlacklisted = false;
	@Override public boolean isSpBlackListed() { return this.spBlacklisted; }
	@Override public void setSpBlackListed(boolean assigned) { this.spAutoAssigned = assigned; }
	
	private WorldExceptionSet worldsEarn = new WorldExceptionSet();
	@Override public WorldExceptionSet getWorldsEarn() { return worldsEarn; }
	@Override public void setWorldsEarn(WorldExceptionSet worldsEarn) { this.worldsEarn = worldsEarn; }
	
	// Configuration
	protected JsonObject configuration = new JsonObject();
	public Object getConfiguration() { return this.configuration; }
	public void setConfiguration(JsonObject conf) { this.configuration = conf; }
	
	// -------------------------------------------- //
	// FIELDS: REQUIRING
	// -------------------------------------------- //
	
	// See requirements
	private transient List<Req> seeRequirements = new CopyOnWriteArrayList<Req>();
	@Override public List<Req> getSeeRequirements() { return this.seeRequirements; }
	@Override public void setSeeRequirements(List<Req> requirements) { this.seeRequirements = requirements; }
	@Override public void addSeeRequirements(Req... requirements) { this.seeRequirements.addAll(Arrays.asList(requirements)); }
	
	// Learn requirements
	private transient List<Req> learnRequirements = new CopyOnWriteArrayList<Req>();
	@Override public List<Req> getLearnRequirements() { return this.learnRequirements; }
	@Override public void setLearnRequirements(List<Req> requirements) { this.learnRequirements = requirements; }
	@Override public void addLearnRequirements(Req... requirements) { this.learnRequirements.addAll(Arrays.asList(requirements)); }
	
	// Specialise requirements
	private transient List<Req> specialiseRequirements = new CopyOnWriteArrayList<Req>();
	@Override public List<Req> getSpecialiseRequirements() { return this.specialiseRequirements; }
	@Override public void setSpecialiseRequirements(List<Req> requirements) { this.specialiseRequirements = requirements; }
	@Override public void addSpecialiseRequirements(Req... requirements) { this.specialiseRequirements.addAll(Arrays.asList(requirements)); }
	
	// -------------------------------------------- //
	// FIELDS: CALCULATIVE
	// -------------------------------------------- //
	
	// Lambda, This is the default algorithm
	private transient LvlStatusCalculator expToLvlStatus = (long exp) -> 	
	{	
		int level = 0, nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; level++)
		{
			exp -= nextLvlExp;
			nextLvlExp *= 1.01;
		}
		
		return new LvlStatusDefault(level, Optional.of( (int) exp), Optional.of(nextLvlExp));
	};
	@Override public final LvlStatus getLvlStatusFromExp(long exp) { return this.expToLvlStatus.calculateLvlStatus(exp); }
	@Override public final void setLvlStatusAlgorithm(LvlStatusCalculator algorithm) { this.expToLvlStatus = algorithm; }
	@Override public final LvlStatusCalculator getLvlStatusAlgorithm() { return this.expToLvlStatus; }
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public DeriusSkill load(DeriusSkill that)
	{
		if (that == null || that == this) return that;
		
		this.enabled = that.enabled;
		this.spAutoAssigned = that.spAutoAssigned;
		this.spBlacklisted = that.spBlacklisted;
		
		if (that.name != null && ! that.name.equalsIgnoreCase("null")) this.name = that.name;
		if (that.desc != null && ! that.desc.equalsIgnoreCase("null")) this.desc = that.desc;
		if (that.earnExpDescs != null)this.earnExpDescs = that.earnExpDescs;
		if (that.icon != null) this.icon = that.icon;
		if (that.worldsEarn != null) this.worldsEarn = that.worldsEarn;
		if (that.configuration != null) this.configuration = that.configuration;
		
		return this;
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	@Override
	public void register()
	{
		SkillRegisteredEvent event = new SkillRegisteredEvent(this);
		if ( ! event.runEvent()) return;
		
		if ( ! this.attached())
		{
			DeriusSkill old = SkillColl.get().get(this.getId(), false);
			if (old != null)
			{
				this.load(old);
				SkillColl.get().removeAtLocal(this.getId());
			}
			SkillColl.get().attach(this, this.getId());
		}
		
		return;
	}
	
	@Override
	public boolean isRegistered()
	{
		return SkillColl.getAllSkills().contains(this);
	}
	
	// -------------------------------------------- //
	// CONFIGURATION
	// -------------------------------------------- //

	/**
	 * Reads a value from the skills custom configuration
	 * @param {String} name of the value
	 * @param {Class<T>} type of value
	 * @return {T} the value
	 */
	public <T> T readConfig(String field, Class<T> fieldType)
	{
		return DeriusCore.get().gson.fromJson(this.configuration.get(field), fieldType);
	}
	
	/**
	 * Reads a value from the skills custom configuration
	 * @param {String} name of the value
	 * @param {TypeToken<T>} typetoken for generics
	 * @return {T} the value
	 */
	public <T> T readConfig(String field, TypeToken<T> typeToken)
	{
		return DeriusCore.get().gson.fromJson(this.configuration.get(field), typeToken.getType());
	}
	
	/**
	 * Writes a value to the skills custom configuration
	 * @param {String} name of the value
	 * @param {Class<T>} type of value
	 * @return {T} the value
	 */
	public void writeConfig(String field, Object value)
	{
		this.configuration.add(field, DeriusCore.get().gson.toJsonTree(value));
	}
	
	/**
	 * Writes a value to the skills custom configuration
	 * @param {String} name of the value
	 * @param {TypeToken<T>} typetoken for generics
	 * @return {T} the value
	 */
	public <T> void writeConfig(String field, Object value, TypeToken<T> typeToken)
	{
		this.configuration.add(field, DeriusCore.get().gson.toJsonTree(value, typeToken.getType()));
	}
	
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public String getDisplayedDescription(Object watcherObject)
	{
		return Txt.parse("%s: <i>%s", this.getDisplayName(watcherObject), this.getDesc());
	}
	
	public String getDisplayName (Object watcherObject)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(watcherObject);
		if (dplayer.isSpecialisedIn(this))
		{
			return Txt.parse(MLang.get().skillColorPlayerIsSpecialised + this.getName());
		}
		else if (SkillUtil.canPlayerLearnSkill(dplayer, this, VerboseLevel.ALWAYS))
		{
			return Txt.parse(MLang.get().skillColorPlayerCanUse + this.getName());
		}
		else
		{
			return  Txt.parse(MLang.get().skillColorPlayerCantUse + this.getName());
		}
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the skill. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {String} the skills unique id.
	 */
	public abstract String getId();
	
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
		
		String id = this.getId();
		result += (id != null) ? id.hashCode() : 1;
		
		return result;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}

	
}
