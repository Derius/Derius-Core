package dk.muj.derius.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Location;
import org.bukkit.Material;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Const;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.integration.FactionIntegration;
import dk.muj.derius.skill.Skill;

public abstract class Ability
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private Skill skill;
	private AbilityType type;
	
	private String desc = "";
	private String name;
	
	private boolean didChange = false;
	
	private List<Material> interactKeys = new CopyOnWriteArrayList<Material>();
	private List<Material> blockBreakKeys = new CopyOnWriteArrayList<Material>();
	
	private List<Material> interactKeysRemoved = new ArrayList<Material>();
	private List<Material> blockBreakKeysRemoved = new ArrayList<Material>();
	
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	public void register()
	{
		Abilities.AddAbility(this);
	}
	
	// -------------------------------------------- //
	// SKILL
	// -------------------------------------------- //
	
	/**
	 * Gets the skill associated with this ability
	 * @return {Skill} the skill associated with this ability
	 */
	public Skill getSkill()
	{
		return this.skill;
	}
	
	/**
	 * Sets the skill associated with this ability
	 * @param {Skill} the skill associated with this ability from now on
	 */
	protected void setSkill(Skill skill)
	{
		this.skill = skill;
	}
	
	/**
	 * Gets the ability type (passive/active) of this ability
	 * @return {AbilityType} the type of this ability
	 */
	public AbilityType getType()
	{
		return this.type;
	}
	
	/**
	 * Sets the ability type (passive/active) of this ability
	 * @param {AbilityType} the new type of this ability
	 */
	protected void setType(AbilityType newType)
	{
		this.type = newType;
	}
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	/**
	 * Sets the description of the ability
	 * @param {String} new description for this ability
	 */
	public void setDescription(String str)
	{
		this.desc = str;
	}
	
	/**
	 * Gets the description of the ability
	 * @param {String} description for this ability
	 */
	public String getDescription()
	{
		return this.desc;
	}
	
	/**
	 * Sets the name of the ability
	 * @param {String} new name for this ability
	 */
	protected void setName(String str)
	{
		this.name = str;
	}
	
	/**
	 * Gets the name of the ability
	 * @param {String} name for this ability
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Gets the name & description, as it would be displayed
	 * to the passed player
	 * @param {MPlayer} player to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayedDescription(MPlayer whatcherObject)
	{
		return Txt.parse("%s%s: <i>%s",AbilityUtil.CanUseAbilitySkillColor(this, whatcherObject),this.getName(), this.getDescription());
	}
	
	// -------------------------------------------- //
	// ACTIVATION KEYS
	// -------------------------------------------- //
	
	//INTERACT KEYS
	
	/**
	 * This will add said materials to its list of interact activation keys
	 * @param {Material...} an array of keys to add
	 */
	public void addInteractKeys(Material... keys)
	{
		for(Material m: keys)
			this.interactKeys.add(m);
		this.setChange(true);
	}
	
	/**
	 * This will add said materials to its list of interact activation keys
	 * @param {Material...} an array of keys to remove
	 */
	public void removeInteractKeys(Material... keys)
	{
		for(Material m: keys)
			this.interactKeys.remove(m);
		this.setChange(true);
	}
	
	
	/**
	 * Gets a collection of the materials that can activate this ability
	 * when a player right clicks with that material
	 * @return {Collection<Material>}
	 */
	public List<Material> getInteractKeys()
	{
		return this.interactKeys;
	}
	
	//BLOCK BREAK KEYS
	
	/**
	 * This will add said materials to its list of block break activation keys
	 * @param {Material...} an array of keys to add
	 */
	public void addBlockBreakKeys(Material... keys)
	{
		for(Material m: keys)
			this.blockBreakKeys.add(m);
		this.setChange(true);
	}
	
	/**
	 * This will add said materials to its list of block break activation keys
	 * @param {Material...} an array of keys to remove
	 */
	public void removeBlockBreakKeys(Material... keys)
	{
		for(Material m: keys)
			this.blockBreakKeys.remove(m);
		this.setChange(true);
	}
	
	
	/**
	 * Gets a collection of the materials that can activate this ability
	 * when a player breaks a block of that material
	 * @return {Collection<Material>}
	 */
	public List<Material> getBlockBreakKeys()
	{
		return this.blockBreakKeys;
	}
	
	// -------------------------------------------- //
	// CHANGE
	// -------------------------------------------- //

	public boolean DidChange() { return didChange; }
	public void setChange(boolean change) { this.didChange = change; }
	
	public List<Material> getRemovedBlockBreakKeys()
	{
		return this.blockBreakKeysRemoved;
	}
	
	public List<Material> getRemovedInteractKeys()
	{
		return this.interactKeysRemoved;
	}
	
	// -------------------------------------------- //
	// TIME
	// -------------------------------------------- //
	
	/**
	 * Gets how many ticks this ability will last
	 * on passed mplayer, in ticks
	 * @param {MPlayer} player to check
	 * @return {int} amount of ticks, this ability would last.
	 */
	public int getTicksLast(MPlayer p)
	{
		return this.getTicksLastDefault(p.getLvl(this.getSkill()));
	}
	
	
	private int getTicksLastDefault(int level)
	{
		return (2 + level/50)*20;
	}
	
	/**
	 * Gets how many ticks the cooldown will last
	 * after using this ability on passed mplayer
	 * @param {MPlayer} player to check
	 * @return {int} amount of ticks, the cooldown will be.
	 */
	public int getCooldownTime(MPlayer p)
	{
		return this.getCooldownTimedefault();
	}
	
	private int getCooldownTimedefault()
	{
		return 20*60*2;
	}

	// -------------------------------------------- //
	// AREA RESTRICTION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not this skill can be used in said area
	 * @param {Location} the are you want to check for
	 * @return {boolean} true if the skill can be used
	 */
	public boolean CanAbilityBeUsedInArea(Location loc)
	{
		if(FactionIntegration.EstablishIntegration())
		{
			Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));
			if(f != null)
				if(f.getFlag(Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD))
					return f.getFlag(Const.FACTION_FLAG_ABILITIES_USE);
			
		}
		return MConf.get().worldAbilityUse.get(this.getId()).EnabledInWorld(loc.getWorld());
	}

	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the ability. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * @return {int} the abilities unique id.
	 */
	public abstract int getId();
	
	/**
	 * Gets a description based on passed level
	 * example "Double drop. Chance for double drop is 10%"
	 * if someone with that level had 10% chance to double drop.
	 * @param {int} the level you want to test for
	 * @return {String} the actual string message
	 */
	public abstract String getLvlDescription(int lvl);
	
	/**
	 * Tells whether or not the player can use the ability .
	 * The ability can have different reasons the player might not.
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can use said ability
	 */
	public abstract boolean CanPlayerActivateAbility(MPlayer p);
	
	/**
	 * Turns on the ability for said player.
	 * THIS IS NOT THE PROPER WAY TO ACTIVATE AN ABILITY
	 * @param {MPlayer} the player to use the ability
	 */
	public void onActivate(MPlayer p){}
	
	/**
	 * Turns off the ability for said player.
	 * THIS IS NOT THE PROPER WAY TO DEACTIVATE AN ABILITY
	 * @param {MPlayer} the player to stop using the ability
	 */
	public void onDeactivate(MPlayer p){}
	
}
