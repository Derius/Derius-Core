package dk.muj.derius.events.player;

import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.events.DeriusEvent;
import dk.muj.derius.events.SkillEvent;

public class PlayerLevelUpEvent extends DeriusEvent implements SkillEvent, DPlayerEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Skill skill;
	public Skill getSkill() { return skill; }
	
	private final DPlayer dplayer;
	public DPlayer getDPlayer() { return dplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerLevelUpEvent(DPlayer dplayer, Skill skill)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(skill, "skill mustn't be null");
		
		this.skill = skill;
		this.dplayer = dplayer;		
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " leveled up in " + this.getSkill().getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerExpAddEvent)) return false;
		PlayerExpAddEvent that = (PlayerExpAddEvent) obj;
	
		return that.getSkill() == this.getSkill() && this.getDPlayer() == that.getDPlayer();
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getSkill().hashCode();
		result += this.getDPlayer().hashCode();
		
		return result;
	}
	
}
