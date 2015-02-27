package dk.muj.derius.engine;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Ability.AbilityType;
import dk.muj.derius.api.events.AbilityActivateEvent;
import dk.muj.derius.api.events.AbilityDeactivateEvent;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.events.player.PlayerLevelUpEvent;
import dk.muj.derius.events.player.PlayerToolPrepareEvent;
import dk.muj.derius.events.player.PlayerToolUnprepareEvent;

public class MsgEngine extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MsgEngine i = new MsgEngine();
	public static MsgEngine get() { return i; }
	public MsgEngine() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusCore.get();
	}
	
	// -------------------------------------------- //
	// ABILITY (DE)ACTIVATE
	// -------------------------------------------- //
	
	// Ability
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendAbilityActivateMsg(AbilityActivateEvent event)
	{
		Ability ability = event.getAbility();
		DPlayer dplayer = event.getDPlayer();
		if (ability.getType() != AbilityType.ACTIVE) return;
		
		int fadeIn = MLang.get().timeAbilityActivateFadeIn;
		int stay = MLang.get().timeAbilityActivateStay;
		int fadeOut = MLang.get().timeAbilityActivateFadeOut;
		
		String name = ability.getDisplayName(dplayer);
		String message =  Txt.parse(MLang.get().abilityActivated, name);
		
		Mixin.sendTitleMessage(dplayer, fadeIn, stay, fadeOut, null, message);
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendAbilityDeactivateMsg(AbilityDeactivateEvent event)
	{
		Ability ability = event.getAbility();
		DPlayer dplayer = event.getDPlayer();
		if (ability.getType() != AbilityType.ACTIVE) return;
		
		int fadeIn = MLang.get().timeAbilityDeactivateFadeIn;
		int stay = MLang.get().timeAbilityDeactivateStay;
		int fadeOut = MLang.get().timeAbilityDeactivateFadeOut;
		
		String name = ability.getDisplayName(dplayer);
		String message =  Txt.parse(MLang.get().abilityDeactivated, name);
		
		Mixin.sendTitleMessage(dplayer, fadeIn, stay, fadeOut, null, message);
		
		return;
	}
	
	// -------------------------------------------- //
	// LEVEL
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendLevelUpMsg(PlayerLevelUpEvent event)
	{
		Skill skill = event.getSkill();
		DPlayer dplayer = event.getDPlayer();
		
		int fadeIn = MLang.get().timeLvlUpFadeIn;
		int stay = MLang.get().timeLvlUpStay;
		int fadeOut = MLang.get().timeLvlUpFadeOut;
		
		String name = skill.getDisplayName(dplayer);
		String message = Txt.parse(MLang.get().levelUp, dplayer.getLvl(skill), name);
		
		Mixin.sendTitleMessage(dplayer, fadeIn, stay, fadeOut, null, message);
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendPrepareToolMsg(PlayerToolPrepareEvent event)
	{
		DPlayer dplayer = event.getDPlayer();
		Material tool = event.getTool();
		String toolName = toolToString(tool);
		String message = Txt.parse(MLang.get().toolPrepared, toolName);
		
		sendActionBar(dplayer.getPlayer(), message);
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendUnprepareToolMsg(PlayerToolUnprepareEvent event)
	{
		DPlayer dplayer = event.getDPlayer();
		Material tool = event.getTool();
		String toolName = toolToString(tool);
		String message = Txt.parse(MLang.get().toolNotPrepared, toolName);
		
		sendActionBar(dplayer.getPlayer(), message);
		
		return;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static boolean sendActionBar(Player player, String msg)
	{
		// Null checks, just in case
		if (msg == null || player == null) return false;
		
		// If not a craftplayer, then idk. (Should never happen)
		if ( ! (player instanceof CraftPlayer)) return false;
		
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
	
	public static String toolToString(Material tool)
	{
		return Txt.upperCaseFirst(tool.name().substring(tool.name().indexOf("_")+1).toLowerCase());
	}
	
}
