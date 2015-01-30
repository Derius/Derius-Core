package dk.muj.derius.events;

import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.event.Cancellable;

public interface CancellableEvent extends Cancellable
{
	// -------------------------------------------- //
	// EVENTS
	// -------------------------------------------- //
	
	public final static Map<CancellableEvent, Boolean> events = new WeakHashMap<>();
	
	// -------------------------------------------- //
	// MANAGE EVENTS
	// -------------------------------------------- //
	
	@Override
	default void setCancelled(boolean cancelled)
	{
		events.put(this, cancelled);
	}
	
	@Override
	default boolean isCancelled()
	{
		if ( ! events.containsKey(this)) events.put(this, Boolean.FALSE);
		return events.get(this);
	}
	
}
