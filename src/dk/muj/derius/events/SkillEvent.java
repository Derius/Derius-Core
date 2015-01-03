package dk.muj.derius.events;

import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.event.EventMassiveCore;

import dk.muj.derius.skill.Skill;

public abstract class SkillEvent extends EventMassiveCore
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
	
	protected final Skill skill;
	public SkillEvent(Skill skill) { this.skill = skill; }
	public Skill getSkill() { return skill; }
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return skill.getName() + " event";
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof SkillEvent)) return false;
		SkillEvent that = (SkillEvent) obj;
	
		if (that.skill == this.skill) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += skill.hashCode();
		
		return result;
	}
	
}