package dk.muj.derius.events.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;

/**
 * This event is thrown when stamina is taken from a player
 */
public class PlayerStaminaTakeEvent extends PlayerStaminaUpdateEvent implements Cancellable, DPlayerEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerStaminaTakeEvent(DPlayer dplayer, double stamina)
	{
		super(dplayer, stamina);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " got " + this.getStaminaAmount() + " stamina taken from him.";
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == this) return true;
		if ( ! (obj instanceof PlayerStaminaTakeEvent)) return false;
		PlayerStaminaTakeEvent that = (PlayerStaminaTakeEvent) obj;
	
		if (this.getStaminaAmount() != that.getStaminaAmount()) return false;
		if (this.getDPlayer() != that.getDPlayer()) return false;
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		int prime = 31;
		
		result += this.getStaminaAmount() * prime;
		result += this.getDPlayer().hashCode() * prime;
		
		return result;
	}

}
