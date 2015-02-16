package dk.muj.derius.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;

/**
 * This event is thrown every time a player loses exp
 */
public class PlayerTakeExpEvent extends DeriusEvent implements CancellableEvent, DPlayerEvent, SkillEvent
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
	
	private final DPlayer mplayer;
	public DPlayer getDPlayer() { return mplayer; }
	public Player getPlayer() { return mplayer.getPlayer(); }
	
	private double amount;
	public double getExpAmount() { return amount; }
	public void setExpAmount(double expamount) { this.amount = expamount; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerTakeExpEvent(DPlayer mplayer, Skill skill, long expAmount)
	{
		this.skill = skill;
		this.mplayer = mplayer;
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
		if ( ! (obj instanceof PlayerTakeExpEvent)) return false;
		PlayerTakeExpEvent that = (PlayerTakeExpEvent) obj;
	
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
