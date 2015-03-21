package dk.muj.derius.engine;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.ability.Ability.AbilityType;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.events.AbilityActivatePreEvent;
import dk.muj.derius.api.events.AbilityDeactivateEvent;
import dk.muj.derius.api.events.player.PlayerLevelUpEvent;
import dk.muj.derius.api.events.player.PlayerToolPrepareEvent;
import dk.muj.derius.api.events.player.PlayerToolUnprepareEvent;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;

public class EngineMsg extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMsg i = new EngineMsg();
	public static EngineMsg get() { return i; }
	private EngineMsg() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusPlugin.get();
	}
	
	// -------------------------------------------- //
	// ABILITY (DE)ACTIVATE
	// -------------------------------------------- //
	
	// Ability
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendAbilityActivateMsg(AbilityActivatePreEvent event)
	{
		Ability ability = event.getAbility();
		DPlayer dplayer = event.getDPlayer();
		if (ability.getType() != AbilityType.ACTIVE) return;
		
		int fadeIn = DLang.get().getTimeAbilityActivateFadeIn();
		int stay = DLang.get().getTimeAbilityActivateStay();
		int fadeOut = DLang.get().getTimeAbilityActivateFadeOut();
		
		String name = ability.getDisplayName(dplayer);
		String message =  DLang.get().getAbilityActivated().replace("{ability}", name);
		
		Mixin.sendTitleMsg(dplayer, fadeIn, stay, fadeOut, null, message);
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendAbilityDeactivateMsg(AbilityDeactivateEvent event)
	{
		Ability ability = event.getAbility();
		DPlayer dplayer = event.getDPlayer();
		if (ability.getType() != AbilityType.ACTIVE) return;
		
		int fadeIn = DLang.get().getTimeAbilityDeactivateFadeIn();
		int stay = DLang.get().getTimeAbilityDeactivateStay();
		int fadeOut = DLang.get().getTimeAbilityDeactivateFadeOut();
		
		String name = ability.getDisplayName(dplayer);
		String message =  DLang.get().getAbilityDeactivated().replace("{ability}", name);
		
		Mixin.sendTitleMsg(dplayer, fadeIn, stay, fadeOut, null, message);
		
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
		
		int fadeIn = DLang.get().getTimeLvlUpFadeIn();
		int stay = DLang.get().getTimeLvlUpStay();
		int fadeOut = DLang.get().getTimeLvlUpFadeOut();
		
		String name = skill.getDisplayName(dplayer);
		String message = DLang.get().getLevelUp().replace("{level}", String.valueOf(dplayer.getLvl(skill))).replace("{skill}", name);
		
		Mixin.sendTitleMsg(dplayer, fadeIn, stay, fadeOut, null, message);
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendPrepareToolMsg(PlayerToolPrepareEvent event)
	{
		DPlayer dplayer = event.getDPlayer();
		Material tool = event.getTool();
		String toolName = toolToString(tool);
		String message = Txt.parse(DLang.get().getToolPrepared().replace("{tool}", toolName));
		
		sendActionBar(dplayer.getPlayer(), message);
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sendUnprepareToolMsg(PlayerToolUnprepareEvent event)
	{
		DPlayer dplayer = event.getDPlayer();
		Material tool = event.getTool();
		String toolName = toolToString(tool);
		String message = Txt.parse(DLang.get().getToolNotPrepared().replace("{tool}", toolName));
		
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
