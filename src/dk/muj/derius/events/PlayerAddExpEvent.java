package dk.muj.derius.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;

/**
 * This event is thrown every time a player receives exp
 */
public class PlayerAddExpEvent extends DeriusEvent implements CancellableEvent, MPlayerEvent, SkillEvent
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
	
	private long amount;
	public long getExpAmount() { return amount; }
	public void setExpAmount(long expamount) { this.amount = expamount; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerAddExpEvent(MPlayer mplayer, Skill skill, long expAmount)
	{
		this.skill = skill;
		this.mplayer = mplayer;
		this.amount = expAmount;
		
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.getMPlayer().getName() + " earned " + this.getExpAmount() + " exp in " + this.getSkill().getName();
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
	
		if (that.getSkill() == this.getSkill() && this.getMPlayer() == that.getMPlayer() && this.getExpAmount() == that.getExpAmount()) return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getSkill().hashCode();
		result += this.getExpAmount() *31;
		result += this.getMPlayer().hashCode();
		
		return result;
	}
	
}
