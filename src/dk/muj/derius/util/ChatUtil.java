package dk.muj.derius.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.Txt;

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
		CommandSender sender = Bukkit.getConsoleSender();
		String name = p.getName();
		switch(p.getMsgType())
		{
			case CHAT:
				p.msg(Txt.parse("<i>You leveled up to level <lime>%s<i> in <aqua>%s.",level,s.getName()));
				break;
			case TITLE:
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name + " reset");
				
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeLvlUpFadeIn + space + MConf.get().timeLvlUpStay + space + MConf.get().timeLvlUpFadeOut);
				Bukkit.getServer().dispatchCommand(sender, Txt.parse(titleCmd + name + " subtitle {\"text\":\"\",\"extra\":[{\"text\":\"You leveled up to lvl \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" in \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"aqua\"}]}",level,s.getName()));
				break;
			case SCOREBOARD:
				
				break;
		}
	}
	
	public static void msgAbilityActivate(MPlayer p, Ability a)
	{
		CommandSender sender = Bukkit.getConsoleSender();
		String name = p.getName();
		
		switch(p.getMsgType())
		{
			case CHAT:
				p.msg(Txt.parse("<i>The Ability <lime>%s<i> was activated.",a.getName()));			
				break;
			case TITLE:
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeAbilityActivateFadeIn + space + MConf.get().timeAbilityActivateStay + space + MConf.get().timeAbilityActivateFadeOut);
				
				Bukkit.getServer().dispatchCommand(sender, Txt.parse("title "+name+" subtitle {\"text\":\"\",\"extra\":[{\"text\":\"The ability \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" was activated\",\"color\":\"yellow\"}]}",a.getName()));
				break;
			case SCOREBOARD:
				
				break;
		}
	}
	
	public static void msgAbilityDeactivate(MPlayer p, Ability a)
	{
		CommandSender sender = Bukkit.getConsoleSender();
		String name = p.getName();
		
		switch(p.getMsgType())
		{
			case CHAT:
				p.msg(Txt.parse("<i>The Ability <lime>%s<i> ran out.",a.getName()));					
				break;
			case TITLE:
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeAbilityDeactivateFadeIn + space + MConf.get().timeAbilityDeactivateStay + space + MConf.get().timeAbilityDeactivateFadeOut);
				
				Bukkit.getServer().dispatchCommand(sender, Txt.parse(titleCmd + name+" subtitle {\"text\":\"\",\"extra\":[{\"text\":\"The ability \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" ran out\",\"color\":\"yellow\"}]}",a.getName()));
				break;
			case SCOREBOARD:
				
				break;
		}
		
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
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
	private static String toolToString(Material tool)
	{
		return Txt.upperCaseFirst(tool.name().substring(tool.name().indexOf("_")+1).toLowerCase());
	}
}
