package dk.muj.derius.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.muj.derius.ability.Ability;

public class AbilityEvent extends Event
{

	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	
	protected final Ability ability;
	
	public AbilityEvent(Ability ability)
	{
		this.ability = ability;
	}

	public Ability getAbility()
	{
		return this.ability;
	}

}
