package dk.muj.derius.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class DeriusEvent extends Event implements RunnableEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() {	return handlers;	} 
	public static HandlerList getHandlerList() {	return handlers;	}
}
