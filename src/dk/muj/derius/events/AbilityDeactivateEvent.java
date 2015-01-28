package dk.muj.derius.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MPlayer;

public class AbilityDeactivateEvent extends AbilityEvent implements Cancellable
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
	
	private final MPlayer mplayer;
	public MPlayer getMPlayer() { return mplayer; }
	
	private boolean cancelled = false;
	public boolean getCancelled() { return cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AbilityDeactivateEvent(Ability ability, MPlayer player)
	{
		super(ability);
		this.mplayer = player;
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return mplayer.getName() + " deactivated " + ability.getName();
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
	
		if (that.mplayer == this.mplayer && that.ability == this.ability) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += mplayer.hashCode();
		result += ability.hashCode();
		result += cancelled ? 1 : 2;
		
		return result;
	}
}
