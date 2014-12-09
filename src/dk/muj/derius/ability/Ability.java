package dk.muj.derius.ability;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

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
	 * Gets the name of the ability. This is seen by players.
	 * This MUST be unique but can always be changed.
	 * However changin it may confuse players.
	 * @return {String} The abilities name
	 */
	public abstract String getName();
	
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
	public abstract void onActivate(MPlayer p);
	
	/**
	 * Turns off the ability for said player.
	 * THIS IS NOT THE PROPER WAY TO DEACTIVATE AN ABILITY
	 * @param {MPlayer} the player to stop using the ability
	 */
	public abstract void onDeactivate(MPlayer p);
	
	/**
	 * Gets a collection of the materials that can activate this ability
	 * when a player right clicks with that material
	 * @return {Collection<Material>}
	 */
	public abstract Collection<Material> getInteractKeys();
	
	/**
	 * Gets the skill related to this ability
	 * @return {Skill} the skill related to this ability
	 */
	public abstract Skill getSkill();
}
