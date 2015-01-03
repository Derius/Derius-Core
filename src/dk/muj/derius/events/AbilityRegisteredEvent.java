package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import dk.muj.derius.ability.Ability;

public class AbilityRegisteredEvent extends AbilityEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
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
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof AbilityRegisteredEvent)) return false;
		AbilityRegisteredEvent that = (AbilityRegisteredEvent) obj;
	
		if (that.ability == this.ability) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += ability.hashCode();
		
		return result;
	}
	
}