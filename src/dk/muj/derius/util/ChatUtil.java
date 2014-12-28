package dk.muj.derius.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

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
		//this only works on players
		if (!mplayer.isPlayer()) return;
		Player player = mplayer.getPlayer();
		
		//Get item stack, we will display msg as itemstack name
		final ItemStack inHand = player.getItemInHand();
		final ItemMeta meta = inHand.getItemMeta();
		final String originalName = meta.getDisplayName();
		meta.setDisplayName(Txt.parse(MConf.get().msgToolPrepared, ToolToString(tool)));
		inHand.setItemMeta(meta);
		
		//Change name back
		Bukkit.getScheduler().runTaskLater(Derius.get(), new Runnable()
		{
			@Override
			public void run()
			{
				meta.setDisplayName(originalName);
				inHand.setItemMeta(meta);
			}

		}, 2*20L);
		
	}
	
	public static void msgToolNotPrepared(MPlayer player, Material tool)
	{
		CommandSender sender = Bukkit.getConsoleSender();
		String name = player.getName();
		
		switch(player.getMsgType())
		{
			case CHAT:
				player.msg(Txt.parse(MConf.get().msgPrefix + MConf.get().msgToolNotPrepared, ToolToString(tool)));
			case TITLE:
				// Change fade in, stay and fade out times to according values
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
				Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeAbilityActivateFadeIn + space + MConf.get().timeAbilityActivateStay + space + MConf.get().timeAbilityActivateFadeOut);
				// Add in actual message
			case SCOREBOARD:
				
				break;
		}
	}
	
	private static String ToolToString(Material tool)
	{
		return Txt.upperCaseFirst(tool.name().substring(tool.name().indexOf("_")+1).toLowerCase());
	}
}
