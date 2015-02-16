package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;

public class AbilityDeactivateEvent extends DeriusEvent implements CancellableEvent, AbilityEvent, DPlayerEvent
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
	
	private DPlayer mplayer;
	public DPlayer getDPlayer() { return mplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AbilityDeactivateEvent(Ability ability, DPlayer mplayer)
	{
		this.ability = ability;
		this.mplayer = mplayer;
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " deactivated " + this.getAbility().getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof AbilityDeactivateEvent)) return false;
		AbilityDeactivateEvent that = (AbilityDeactivateEvent) obj;
	
		if (that.getDPlayer() == this.getDPlayer() && that.getDPlayer() == this.getDPlayer()) return true;
		
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
