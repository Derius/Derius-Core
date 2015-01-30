package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.Skill;

/**
 * This event is called when a skill is registered into the system.
 * Note the same skills will be registered on server startup each time.
 */
public class SkillRegisteredEvent extends DeriusEvent implements CancellableEvent, SkillEvent
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
	
	private final Skill skill;
	public Skill getSkill() { return skill; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public SkillRegisteredEvent(Skill registeredSkill)
	{
		this.skill = registeredSkill;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getSkill().getName() + " is registered";
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof SkillRegisteredEvent)) return false;
		SkillRegisteredEvent that = (SkillRegisteredEvent) obj;
	
		if (that.getSkill() == this.getSkill()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getSkill().hashCode();
		
		return result;
	}
	
}
