package dk.muj.derius.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;

public class AbilityActivateEvent extends DeriusEvent implements CancellableEvent, AbilityEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Ability ability;
	public Ability getAbility() { return ability; }
	
	private DPlayer mplayer;
	public DPlayer getDPlayer() { return mplayer; }
	public Player getPlayer() { return mplayer.getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AbilityActivateEvent(Ability ability, DPlayer mplayer)
	{
		this.mplayer  = mplayer;
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
