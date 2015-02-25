package dk.muj.derius.events;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.events.player.DPlayerEvent;

public class AbilityActivateEvent extends DeriusEvent implements Cancellable, AbilityEvent, DPlayerEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() {	return handlers;	} 
	public static HandlerList getHandlerList() {	return handlers;	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Ability ability;
	public Ability getAbility() { return ability; }
	
	private DPlayer dplayer;
	public DPlayer getDPlayer() { return dplayer; }
	
	private boolean cancelled = false;
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancel) { this.cancelled = cancel; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AbilityActivateEvent(Ability ability, DPlayer dplayer)
	{
		Validate.notNull(dplayer, "dplayer mustn't be null");
		Validate.notNull(ability, "ability mustn't be null");
		
		this.dplayer  = dplayer;
		this.ability = ability;
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " activated " + getAbility().getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof AbilityActivateEvent)) return false;
		AbilityActivateEvent that = (AbilityActivateEvent) obj;
	
		if (that.getDPlayer() == this.getDPlayer() && that.getAbility() == this.getAbility()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		int prime = 31;
		
		result += this.getDPlayer().hashCode()*prime;
		result += this.getAbility().hashCode()*prime;
		
		return result;
	}

}
