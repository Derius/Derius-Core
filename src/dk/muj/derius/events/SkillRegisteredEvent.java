package dk.muj.derius.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.muj.derius.skill.Skill;

/**
 * This event is called when a skill is registered into the system.
 * Note the same skills will be registered on server startup each time.
 */
public class SkillRegisteredEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() 
	{
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() 
	{
	    return handlers;
	}
	
	final Skill skill;
	
	public SkillRegisteredEvent(Skill registeredSkill)
	{
		this.skill = registeredSkill;
	}
	
	public Skill getSkill()
	{
		return this.skill;
	}
}
