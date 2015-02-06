package dk.muj.derius.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;

public class PlayerLevelDownEvent extends DeriusEvent implements SkillEvent, DPlayerEvent
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
	
	private final DPlayer mplayer;
	public DPlayer getDPlayer() { return mplayer; }
	public Player getPlayer() { return mplayer.getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerLevelDownEvent(DPlayer mplayer, Skill skill)
	{
		this.skill = skill;
		this.mplayer = mplayer;		
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getDPlayer().getName() + " leveled down in " + this.getSkill().getName();
	}
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if (obj == null) return false;
		if ( ! (obj instanceof PlayerAddExpEvent)) return false;
		PlayerAddExpEvent that = (PlayerAddExpEvent) obj;
	
		return that.getSkill() == this.getSkill() && this.getDPlayer() == that.getDPlayer();
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getSkill().hashCode();
		result += this.getDPlayer().hashCode();
		
		return result;
	}
}
