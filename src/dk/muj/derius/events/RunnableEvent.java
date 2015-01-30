package dk.muj.derius.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public interface RunnableEvent extends Runnable
{
	@Override
	default void run()
	{
		if (!(this instanceof Event)) throw new UnsupportedOperationException("This interface should only be implemented by Bukkit events");
		Bukkit.getPluginManager().callEvent((Event) this);
	}
}
