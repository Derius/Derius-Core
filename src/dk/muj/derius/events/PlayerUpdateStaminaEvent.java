package dk.muj.derius.events;

import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;

public class PlayerUpdateStaminaEvent extends DeriusEvent implements CancellableEvent, DPlayerEvent
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
	
	private double newAmount;
	public double getStaminaAmount() { return newAmount; }
	public void setStaminaAmount(double staminaAmount) { this.newAmount = staminaAmount; }
	
	private StaminaUpdateReason reason;
	public StaminaUpdateReason getUpdateReason() { return reason; }
	public void setUpdateReason(StaminaUpdateReason reason) { this.reason = reason; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerUpdateStaminaEvent(DPlayer dplayer, double staminaAmount, StaminaUpdateReason reason)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		
		this.dplayer = dplayer;
		this.newAmount = staminaAmount;
		this.reason = reason;
		
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " got his stamina updated to " + this.getStaminaAmount();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerUpdateStaminaEvent)) return false;
		PlayerUpdateStaminaEvent that = (PlayerUpdateStaminaEvent) obj;
	
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
	
	// -------------------------------------------- //
	// REASON ENUM
	// -------------------------------------------- //
	
	public enum StaminaUpdateReason
	{
		TIME,
		ABILITY,
		COMMAND,
		UNDEFINED,
		;
	}
	
}
