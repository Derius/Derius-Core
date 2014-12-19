package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import dk.muj.derius.skill.Skill;

/**
 * This event is called when a skill is registered into the system.
 * Note the same skills will be registered on server startup each time.
 */
public class SkillRegisteredEvent extends SkillEvent
{
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	public SkillRegisteredEvent(Skill registeredSkill)
	{
		super(registeredSkill);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return skill.getName() + " is registered";
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if(obj == null)
			return false;
		if(!(obj instanceof SkillRegisteredEvent))
			return false;
		SkillRegisteredEvent that = (SkillRegisteredEvent) obj;
	
		if(that.skill == this.skill)
			return true;
		
		return false;
	}
}
