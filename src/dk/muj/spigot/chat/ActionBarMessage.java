package dk.muj.spigot.chat;

import org.bukkit.entity.Player;

public interface ActionBarMessage
{
	public boolean send(Player player, String msg);
}
