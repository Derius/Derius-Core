package dk.muj.derius.util;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;
import dk.muj.spigot.chat.ActionBar_v1_8_R1;

public final class ChatUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	
	private ChatUtil() {}
	
	// -------------------------------------------- //
	// MSG
	// -------------------------------------------- //
	
	public static void msgLevelUp(DPlayer p, Skill s, int level)
	{
		MLang lang = MLang.get();
		ChatUtil.sendTitle(p.getPlayer(), Optional.empty(), Optional.of(Txt.parse(lang.levelUp, level, s.getDisplayName(p))), 
				lang.timeLvlUpFadeIn, lang.timeLvlUpStay, lang.timeLvlUpFadeOut);
	}
	
	public static void msgLevelDown(DPlayer mPlayer, Skill skill, int lvlAfter)
	{
		// TODO Auto-generated method stub
		
	}
	
	public static void msgAbilityActivate(DPlayer p, Ability a)
	{
		MLang lang = MLang.get();
		ChatUtil.sendTitle(p.getPlayer(), Optional.empty(), Optional.of(Txt.parse(lang.abilityActivated, a.getDisplayName(p))), 
				lang.timeAbilityActivateFadeIn, lang.timeAbilityActivateStay, lang.timeAbilityActivateFadeOut);
	}
	
	public static void msgAbilityDeactivate(DPlayer p, Ability a)
	{
		MLang lang = MLang.get();
		ChatUtil.sendTitle(p.getPlayer(), Optional.empty(), Optional.of(Txt.parse(lang.abilityDeactivated, a.getDisplayName(p))), 
				lang.timeAbilityDeactivateFadeIn, lang.timeAbilityDeactivateStay, lang.timeAbilityDeactivateFadeOut);
		
	}
	
	public static void msgToolPrepared(DPlayer mplayer, Material tool)
	{
		sendActionBar(mplayer.getPlayer(), Txt.parse(MLang.get().toolPrepared, toolToString(tool)));
	}
	
	public static void msgToolNotPrepared(DPlayer mplayer, Material tool)
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
	
	public static boolean sendTitle(Player player, Optional<String> title, Optional<String> subtitle, int fadeIn, int stay, int fadeOut)
	{
		String mainTitle = title.isPresent() ? title.get() : null;
		String subTitle = subtitle.isPresent() ? subtitle.get() : null;
		return Mixin.sendTitleMessage(player, fadeIn, stay, fadeOut, mainTitle, subTitle);
	}
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
	private static String toolToString(Material tool)
	{
		return Txt.upperCaseFirst(tool.name().substring(tool.name().indexOf("_")+1).toLowerCase());
	}


}
