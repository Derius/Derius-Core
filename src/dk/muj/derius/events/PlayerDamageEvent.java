package dk.muj.derius.events;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import com.massivecraft.massivecore.event.EventMassiveCore;

public class PlayerDamageEvent extends EventMassiveCore
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
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerDamageEvent(EntityDamageEvent innerEvent)
	{
		this.inner = innerEvent;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean isCancelled()
	{
		return this.inner.isCancelled();
	}
	
	@Override
	public void setCancelled(boolean cancelled)
	{
		this.inner.setCancelled(cancelled);
	}
}
