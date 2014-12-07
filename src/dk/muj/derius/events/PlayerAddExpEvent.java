package dk.muj.derius.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

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

	public MPlayer getPlayer()
	{
		return player;
	}

	public long getExpamount()
	{
		return amount;
	}

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
