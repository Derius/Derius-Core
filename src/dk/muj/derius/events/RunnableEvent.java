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
		this.preRun();
		if (!(this instanceof Event)) throw new UnsupportedOperationException("This interface should only be implemented by Bukkit events");
		Bukkit.getPluginManager().callEvent((Event) this);
		this.postRun();
	}
	
	// -------------------------------------------- //
	// PRE & POST
	// -------------------------------------------- //
	
	public default void preRun() {};
	public default void postRun() {};
	
}
