package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import dk.muj.derius.ability.Ability;

public class AbilityRegisteredEvent extends AbilityEvent
{
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	public AbilityRegisteredEvent(Ability ability)
	{
		super(ability);
	}
	
}
