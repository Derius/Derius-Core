package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.event.EventMassiveCore;

import dk.muj.derius.ability.Ability;

public class AbilityEvent extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final Ability ability;
	public AbilityEvent(Ability ability) { this.ability = ability; }
	public Ability getAbility(){ return this.ability; }
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof AbilityEvent)) return false;
		AbilityEvent that = (AbilityEvent) obj;
	
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
