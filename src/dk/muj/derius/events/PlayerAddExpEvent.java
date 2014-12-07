package dk.muj.derius.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

/**
 * This event is thrown every time a player receives exp
 */
public class PlayerAddExpEvent extends SkillEvent implements Cancellable
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
	
	private final MPlayer player;
	private long amount;
	private boolean cancelled = false;
	
	public PlayerAddExpEvent(MPlayer player, Skill skill, long expAmount)
	{
		super(skill);
		this.player = player;
		this.amount = expAmount;
		
	}

	/**
	 * Get the player who is getting exp
	 * @return {MPlayer} player getting exp
	 */
	public MPlayer getPlayer()
	{
		return player;
	}

	/**
	 * Gets the amount of exp being added
	 * @return {long} the amount of added exp
	 */
	public long getExpamount()
	{
		return amount;
	}

	/**
	 * Sets the amount of exp being added
	 * @param {long} the amount of exp added
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
}
