package dk.muj.derius.events.player;

import org.bukkit.entity.Player;

import dk.muj.derius.api.DPlayer;

public interface DPlayerEvent
{
	public DPlayer getDPlayer();
	default Player getPlayer()
	{
		return this.getDPlayer().getPlayer();
	}
}
