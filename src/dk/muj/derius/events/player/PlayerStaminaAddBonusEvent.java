package dk.muj.derius.events.player;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.events.DeriusEvent;

/**
 * This event is thrown when an amount is added to the players maxStamina
 */
public class PlayerStaminaAddBonusEvent extends DeriusEvent implements Cancellable, DPlayerEvent
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
	
	private final DPlayer dplayer;
	public DPlayer getDPlayer() { return dplayer; }
	
	private double amount;
	public double getStaminaAmount() { return amount; }
	public void setStaminaAmount(double staminaAmount) { this.amount = staminaAmount; }
	
	private boolean cancelled = false;
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancel) { this.cancelled = cancel; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerStaminaAddBonusEvent(DPlayer dplayer, double staminaAmount)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		
		this.dplayer = dplayer;
		this.amount = staminaAmount;
		
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " got " + this.getStaminaAmount() + " maxStamina added.";
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerStaminaAddBonusEvent)) return false;
		PlayerStaminaAddBonusEvent that = (PlayerStaminaAddBonusEvent) obj;
	
		// We can't use the amount in equals & hashcode, because it can be changed and this is used as a hashmap key.
		if (this.getDPlayer() == that.getDPlayer()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		int prime = 31;
		
		// We can't use the amount in equals & hashcode, because it can be changed and this is used as a hashmap key.
		result += this.getDPlayer().hashCode() * prime;
		
		return result;
	}

}
