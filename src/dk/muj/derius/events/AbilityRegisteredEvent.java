package dk.muj.derius.events;

import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.Ability;

public class AbilityRegisteredEvent extends DeriusEvent implements CancellableEvent, AbilityEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() {	return handlers;	} 
	public static HandlerList getHandlerList() {	return handlers;	}
	
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Ability ability;
	public Ability getAbility() { return ability; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AbilityRegisteredEvent(Ability ability)
	{
		Validate.notNull(ability, "ability mustn't be null");
		
		this.ability = ability;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getAbility().getName() + " is registered";
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
	
		if (that.getAbility() == this.getAbility()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		int prime = 31;
		
		result += this.getAbility().hashCode()*prime;
		
		return result;
	}
	
}