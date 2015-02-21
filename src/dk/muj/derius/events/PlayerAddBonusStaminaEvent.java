package dk.muj.derius.events;

import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;

/**
 * This event is thrown when an amount is added to the players maxStamina
 */
public class PlayerAddBonusStaminaEvent extends DeriusEvent implements CancellableEvent, DPlayerEvent
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
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerAddBonusStaminaEvent(DPlayer dplayer, double staminaAmount)
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
		if ( ! (obj instanceof PlayerAddBonusStaminaEvent)) return false;
		PlayerAddBonusStaminaEvent that = (PlayerAddBonusStaminaEvent) obj;
	
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
