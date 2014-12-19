package dk.muj.derius.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

/**
 * This event is thrown every time a player loses exp
 */
public class PlayerTakeExpEvent extends SkillEvent implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {    return handlers;	} 
	public static HandlerList getHandlerList() {    return handlers;	}
	
	private final MPlayer player;
	private long amount;
	private boolean cancelled = false;
	
	public PlayerTakeExpEvent(MPlayer player, Skill skill, long expAmount)
	{
		super(skill);
		this.player = player;
		this.amount = expAmount;
		
	}

	/**
	 * Get the player who is losing exp
	 * @return {MPlayer} player getting exp
	 */
	public MPlayer getPlayer()
	{
		return player;
	}

	/**
	 * Gets the amount of exp being lost
	 * @return {long} the amount of lost exp
	 */
	public long getExpAmount()
	{
		return amount;
	}

	/**
	 * Sets the amount of exp being lost
	 * @param {long} the amount of exp lost
	 */
	public void setExpAmount(long expamount)
	{
		this.amount = expamount;
	}

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
		
	}

	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return player.getName() + " lost " + amount + " exp in " + skill.getName();
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		if(obj == null)
			return false;
		if(!(obj instanceof PlayerTakeExpEvent))
			return false;
		PlayerTakeExpEvent that = (PlayerTakeExpEvent) obj;
	
		if(that.player == this.player && that.amount == this.amount && that.skill == this.skill)
			return true;
		
		return false;
	}
}
