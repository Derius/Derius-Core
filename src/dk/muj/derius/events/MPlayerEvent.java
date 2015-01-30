package dk.muj.derius.events;

import org.bukkit.entity.Player;

import dk.muj.derius.entity.MPlayer;

public interface MPlayerEvent
{
	public MPlayer getMPlayer();
	default Player getPlayer()
	{
		return this.getMPlayer().getPlayer();
	}
}
