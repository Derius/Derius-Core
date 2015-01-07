package dk.muj.derius.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusSender;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.spigot.chat.ActionBar_v1_8_R1;

public final class ChatUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	private ChatUtil(){}
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private final static String space = " ";
	private final static String titleCmd = "title ";
	
	// -------------------------------------------- //
	// MSG
	// -------------------------------------------- //
	
	public static void msgLevelUp(MPlayer p, Skill s, int level)
	{
		MConf c = MConf.get();
		ChatUtil.sendSubTitle(p.getPlayer(), Txt.parse(c.msgSkillLvlUp, level, s.getDisplayName(p)), 
				c.timeLvlUpFadeIn, c.timeLvlUpStay, c.timeLvlUpFadeOut);
	}
	
	public static void msgLevelDown(MPlayer mPlayer, Skill skill, int lvlAfter)
	{
		// TODO Auto-generated method stub
		
	}
	
	public static void msgAbilityActivate(MPlayer p, Ability a)
	{
		MConf c = MConf.get();
		ChatUtil.sendSubTitle(p.getPlayer(), Txt.parse(c.msgAbilityActivated, a.getDisplayName(p)), 
				c.timeAbilityActivateFadeIn, c.timeAbilityActivateStay, c.timeAbilityActivateFadeOut);
	}
	
	public static void msgAbilityDeactivate(MPlayer p, Ability a)
	{
		MConf c = MConf.get();
		ChatUtil.sendSubTitle(p.getPlayer(), Txt.parse(c.msgAbilityDeactivated, a.getDisplayName(p)), 
				c.timeAbilityDeactivateFadeIn, c.timeAbilityDeactivateStay, c.timeAbilityDeactivateFadeOut);
		
	}
	
	public static void msgToolPrepared(MPlayer mplayer, Material tool)
	{
		sendActionBar(mplayer.getPlayer(), Txt.parse(MConf.get().msgToolPrepared, toolToString(tool)));
	}
	
	public static void msgToolNotPrepared(MPlayer mplayer, Material tool)
	{
		sendActionBar(mplayer.getPlayer(), Txt.parse(MConf.get().msgToolNotPrepared, toolToString(tool)));
	}
	
	// -------------------------------------------- //
	// MSG TYPES (NMS $W@G)
	// -------------------------------------------- //
	
	public static boolean sendActionBar(Player player, String msg)
	{
		return (new ActionBar_v1_8_R1()).send(player, msg);
	}
	
	public static boolean sendTitle(Player player, String msg, int fadeIn, int stay, int fadeOut)
	{
		if(player == null || msg == null) return false;
		
		CommandSender sender = new DeriusSender();
		
		String name = sender.getName();
		msg = Txt.parse(msg);
		msg = "{\"text\":\"\",\"extra\":[{\"text\":\""+msg+"\"}]}";
		
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + fadeIn + space + stay + space + fadeOut);
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" title "+ msg);
		
		return true;
	}
	
	public static boolean sendSubTitle(Player player, String msg, int fadeIn, int stay, int fadeOut)
	{
		if(player == null || msg == null) return false;
		
		CommandSender sender = new DeriusSender();
		
		String name = player.getName();
		if(name == null) return false;
		
		msg = Txt.parse(msg);
		msg = "{\"text\":\"\",\"extra\":[{\"text\":\""+msg+"\"}]}";
		
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + fadeIn + space + stay + space + fadeOut);
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" subtitle "+ msg);
		
		return true;
	}
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
	private static String toolToString(Material tool)
	{
		return Txt.upperCaseFirst(tool.name().substring(tool.name().indexOf("_")+1).toLowerCase());
	}


}
