package dk.muj.derius.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

public class PlayerLevelUpEvent extends DeriusEvent implements SkillEvent, MPlayerEvent
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
	
	private final MPlayer mplayer;
	public MPlayer getMPlayer() { return mplayer; }
	public Player getPlayer() { return mplayer.getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerLevelUpEvent(MPlayer mplayer, Skill skill)
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
		return this.getMPlayer().getName() + " leveled up in " + this.getSkill().getName();
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
	
		return that.getSkill() == this.getSkill() && this.getMPlayer() == that.getMPlayer();
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getSkill().hashCode();
		result += this.getMPlayer().hashCode();
		
		return result;
	}
	
}
