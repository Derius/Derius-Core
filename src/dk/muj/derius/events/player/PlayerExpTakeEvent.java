package dk.muj.derius.events.player;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.events.DeriusEvent;
import dk.muj.derius.events.SkillEvent;

/**
 * This event is thrown every time a player loses exp
 */
public class PlayerExpTakeEvent extends DeriusEvent implements Cancellable, DPlayerEvent, SkillEvent
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
	
	private double amount;
	public double getExpAmount() { return amount; }
	public void setExpAmount(double expamount) { this.amount = expamount; }
	
	private boolean cancelled = false;
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancel) { this.cancelled = cancel; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerExpTakeEvent(DPlayer dplayer, Skill skill, long expAmount)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(skill, "skill mustn't be null");
		
		this.skill = skill;
		this.dplayer = dplayer;
		this.amount = expAmount;
		
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " lost " + this.getExpAmount() + " exp in " + this.getSkill().getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerExpTakeEvent)) return false;
		PlayerExpTakeEvent that = (PlayerExpTakeEvent) obj;
	
		// We can't use the amount in equals & hashcode, because it can be changed and this is used as a hashmap key.
		if (this.getSkill() == that.getSkill() && this.getDPlayer() == that.getDPlayer()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		// We can't use the amount in equals & hashcode, because it can be changed and this is used as a hashmap key.
		result += this.getSkill().hashCode();
		result += this.getDPlayer().hashCode();
		
		return result;
	}
	
}
