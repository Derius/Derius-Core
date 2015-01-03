package dk.muj.derius.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

/**
 * This event is thrown every time a player loses exp
 */
public class PlayerTakeExpEvent extends SkillEvent implements Cancellable
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final MPlayer player;
	public MPlayer getPlayer() { return player; }
	
	private long amount;
	public long getExpAmount() { return amount; }
	public void setExpAmount(long expamount) { this.amount = expamount; }
	
	private boolean cancelled = false;
	public boolean isCancelled() { return cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerTakeExpEvent(MPlayer player, Skill skill, long expAmount)
	{
		super(skill);
		this.player = player;
		this.amount = expAmount;
		
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return player.getName() + " lost " + amount + " exp in " + skill.getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerTakeExpEvent)) return false;
		PlayerTakeExpEvent that = (PlayerTakeExpEvent) obj;
	
		if (that.skill == this.skill && this.player == that.player && this.amount == that.amount) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += skill.hashCode();
		result += amount *31;
		result += player.hashCode();
		
		return result;
	}
	
}
