package dk.muj.derius.events.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import dk.muj.derius.lib.RunnableEvent;

public class PlayerDamageEvent extends Event implements Cancellable, RunnableEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final EntityDamageEvent inner;
	public EntityDamageEvent getInnerEvent() { return inner; }
	
	private final Player player;
	public Player getPlayer() { return this.player; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerDamageEvent(EntityDamageEvent innerEvent)
	{
		this.inner = innerEvent;
		Entity entity = this.inner.getEntity();
		if ( ! (entity instanceof Player)) throw new IllegalArgumentException("The damagee must be a player");
		this.player = (Player) entity;
	}
	
	@Override
	public boolean isCancelled()
	{
		return inner.isCancelled();
	}
	@Override
	public void setCancelled(boolean cancelled)
	{
		inner.setCancelled(cancelled);
	}

}
