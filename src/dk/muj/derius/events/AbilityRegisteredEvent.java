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
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return ability.getName() + " is registered";
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if(obj == null)
			return false;
		if(!(obj instanceof AbilityRegisteredEvent))
			return false;
		AbilityRegisteredEvent that = (AbilityRegisteredEvent) obj;
	
		if(that.ability == this.ability)
			return true;
		
		return false;
	}
	
}
