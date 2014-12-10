package dk.muj.derius.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MPlayer;

public class AbilityDeactivateEvent extends AbilityEvent implements Cancellable
{

	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	private final MPlayer mplayer;
	private boolean cancelled = false;
	
	public AbilityDeactivateEvent(Ability ability, MPlayer player)
	{
		super(ability);
		this.mplayer = player;
	}
	
	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	/**
	 * THIS IS HEAVILY DISCOURAGED AND CAN CAUSE MAJOR ISSUES
	 */
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
		
	}
	public MPlayer getMPlayer()
	{
		return mplayer;
	}
}
