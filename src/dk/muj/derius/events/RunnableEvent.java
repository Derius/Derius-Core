package dk.muj.derius.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public interface RunnableEvent extends Runnable
{
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	default void run()
	{
		if ( ! (this instanceof Event)) throw new UnsupportedOperationException("This interface should only be implemented by Bukkit events");
		this.preRun();
		Bukkit.getPluginManager().callEvent((Event) this);
		this.postRun();
		if (this instanceof CancellableEvent) CancellableEvent.events.remove(this);
	}
	
	// -------------------------------------------- //
	// PRE & POST
	// -------------------------------------------- //
	
	public default void preRun() {};
	public default void postRun() {};
	
}
