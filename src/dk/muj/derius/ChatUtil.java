package dk.muj.derius;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

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
		
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name + " reset");
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeLvlUpFadeIn + space + MConf.get().timeLvlUpStay + space + MConf.get().timeLvlUpFadeOut);
		Bukkit.getServer().dispatchCommand(sender, Txt.parse(titleCmd + name + " subtitle {\"text\":\"\",\"extra\":[{\"text\":\"You leveled up to lvl \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" in \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"aqua\"}]}",level,s.getName()));
		//Bukkit.getServer().dispatchCommand(sender, Txt.parse(titleCmd + name + " title {\"text\":\"\",\"extra\":[{\"text\":\"You leveled up to lvl \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" in \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"aqua\"}]}",level,s.getName()));
	}
	
	public static void msgAbilityActivate(MPlayer p, Ability a)
	{
		CommandSender sender = Bukkit.getConsoleSender();
		String name = p.getName();
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeAbilityActivateFadeIn + space + MConf.get().timeAbilityActivateStay + space + MConf.get().timeAbilityActivateFadeOut);
		
		Bukkit.getServer().dispatchCommand(sender, Txt.parse("title "+name+" subtitle {\"text\":\"\",\"extra\":[{\"text\":\"The ability \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" was activated\",\"color\":\"yellow\"}]}",a.getName()));
	}
	
	public static void msgAbilityDeactivate(MPlayer p, Ability a)
	{
		CommandSender sender = Bukkit.getConsoleSender();
		String name = p.getName();
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + MConf.get().timeAbilityDeactivateFadeIn + space + MConf.get().timeAbilityDeactivateStay + space + MConf.get().timeAbilityDeactivateFadeOut);
		
		Bukkit.getServer().dispatchCommand(sender, Txt.parse(titleCmd + name+" subtitle {\"text\":\"\",\"extra\":[{\"text\":\"The ability \",\"color\":\"yellow\"},{\"text\":\"%s\",\"color\":\"green\"},{\"text\":\" ran out\",\"color\":\"yellow\"}]}",a.getName()));
	}
}
