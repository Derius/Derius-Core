package dk.muj.derius.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MPlayer;

public class AbilityDeactivateEvent extends DeriusEvent implements CancellableEvent, AbilityEvent
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
	
	private MPlayer mplayer;
	public MPlayer getMPlayer() { return mplayer; }
	public Player getPlayer() { return mplayer.getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AbilityDeactivateEvent(Ability ability, MPlayer mplayer)
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
		return this.getMPlayer().getName() + " deactivated " + this.getAbility().getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if (!(obj instanceof AbilityDeactivateEvent)) return false;
		AbilityDeactivateEvent that = (AbilityDeactivateEvent) obj;
	
		if (that.getMPlayer() == this.getMPlayer() && that.getMPlayer() == this.getMPlayer()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		int prime = 31;
		
		result += this.getMPlayer().hashCode()*prime;
		result += this.getAbility().hashCode()*prime;
		result += this.isCancelled() ? 1 : 2;
		
		return result;
	}
	
}
