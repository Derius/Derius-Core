package dk.muj.derius.events;

import org.bukkit.Material;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;

public class PlayerPrepareToolEvent extends DeriusEvent implements CancellableEvent, DPlayerEvent
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
	
	private final Material tool;
	public Material getTool() { return tool; }
	
	private DPlayer mplayer;
	public DPlayer getDPlayer() { return mplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerPrepareToolEvent(Material material, DPlayer mplayer)
	{
		this.mplayer  = mplayer;
		this.tool = material;
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " prepared " + getTool().name();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerPrepareToolEvent)) return false;
		PlayerPrepareToolEvent that = (PlayerPrepareToolEvent) obj;
	
		if (that.getDPlayer() == this.getDPlayer() && that.getTool() == this.getTool()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		int prime = 31;
		
		result += this.getDPlayer().hashCode()*prime;
		result += this.getTool().hashCode()*prime;
		
		return result;
	}
	
}