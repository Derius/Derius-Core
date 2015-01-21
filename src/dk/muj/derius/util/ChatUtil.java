package dk.muj.derius.util;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusSender;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MLang;
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
		MLang lang = MLang.get();
		ChatUtil.sendTitle(p.getPlayer(), Optional.empty(), Optional.of(Txt.parse(lang.levelUp, level, s.getDisplayName(p))), 
				lang.timeLvlUpFadeIn, lang.timeLvlUpStay, lang.timeLvlUpFadeOut);
	}
	
	public static void msgLevelDown(MPlayer mPlayer, Skill skill, int lvlAfter)
	{
		// TODO Auto-generated method stub
		
	}
	
	public static void msgAbilityActivate(MPlayer p, Ability a)
	{
		MLang lang = MLang.get();
		ChatUtil.sendTitle(p.getPlayer(), Optional.empty(), Optional.of(Txt.parse(lang.abilityActivated, a.getDisplayName(p))), 
				lang.timeAbilityActivateFadeIn, lang.timeAbilityActivateStay, lang.timeAbilityActivateFadeOut);
	}
	
	public static void msgAbilityDeactivate(MPlayer p, Ability a)
	{
		MLang lang = MLang.get();
		ChatUtil.sendTitle(p.getPlayer(), Optional.empty(), Optional.of(Txt.parse(lang.abilityDeactivated, a.getDisplayName(p))), 
				lang.timeAbilityDeactivateFadeIn, lang.timeAbilityDeactivateStay, lang.timeAbilityDeactivateFadeOut);
		
	}
	
	public static void msgToolPrepared(MPlayer mplayer, Material tool)
	{
		sendActionBar(mplayer.getPlayer(), Txt.parse(MLang.get().toolPrepared, toolToString(tool)));
	}
	
	public static void msgToolNotPrepared(MPlayer mplayer, Material tool)
	{
		sendActionBar(mplayer.getPlayer(), Txt.parse(MLang.get().toolNotPrepared, toolToString(tool)));
	}
	
	// -------------------------------------------- //
	// MSG TYPES (NMS $W@G)
	// -------------------------------------------- //
	
	public static boolean sendActionBar(Player player, String msg)
	{
		return (new ActionBar_v1_8_R1()).send(player, msg);
	}
	
	public static boolean sendTitle(Player player, Optional<String> title, Optional<String> subTitle, int fadeIn, int stay, int fadeOut)
	{
		if(player == null || title == null) return false;
		
		CommandSender sender = new DeriusSender();
		
		String name = player.getName();
		
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" reset");
		Bukkit.getServer().dispatchCommand(sender, titleCmd + name +" times " + fadeIn + space + stay + space + fadeOut);
		
		if (title.isPresent())
		{	
			String theTitle = "{\"text\":\"\",\"extra\":[{\"text\":\""+Txt.parse(title.get())+"\"}]}";
			Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" title "+ theTitle);
		}
		
		if (subTitle.isPresent())
		{	
			String theSubTitle = "{\"text\":\"\",\"extra\":[{\"text\":\""+Txt.parse(subTitle.get())+"\"}]}";
			Bukkit.getServer().dispatchCommand(sender, titleCmd + name+" subtitle "+ theSubTitle);
		}

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
