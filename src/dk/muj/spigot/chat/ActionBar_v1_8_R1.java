package dk.muj.spigot.chat;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.Txt;

public class ActionBar_v1_8_R1 implements ActionBarMessage
{	
	public boolean send(Player player, String msg)
	{
		// Null checks, just in case
		if(msg == null || player == null) return false;
		// If not a craftplayer, then idk. (Should never happen)
		if( ! (player instanceof CraftPlayer)) return false;
		
		// Prepare the message
		msg = Txt.parse(msg);
		
		// The actual player
		CraftPlayer	cplayer = (CraftPlayer) player;
		
		// The packet and stuff, that gets send to the player
	    IChatBaseComponent	component = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
	    PacketPlayOutChat	packet = new PacketPlayOutChat(component, (byte) 2);
	    
	    // Send the packet
	    cplayer.getHandle().playerConnection.sendPacket(packet);
	    
	    // Success
		return true;
	}
	
	
}
