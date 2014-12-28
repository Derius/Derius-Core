package dk.muj.derius.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.muj.derius.skill.Skill;

public abstract class SkillEvent extends Event
{

	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	protected final Skill skill;
	
	public SkillEvent(Skill skill)
	{
		this.skill = skill;
	}

	public Skill getSkill()
	{
		return skill;
	}
}
