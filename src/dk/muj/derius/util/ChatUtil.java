package dk.muj.derius.util;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

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
	
	private ChatUtil() {}
	
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
	
	public static boolean sendTitle(Player player, Optional<String> title, Optional<String> subtitle, int fadeIn, int stay, int fadeOut)
	{
		return Mixin.sendTitleMessage(player, 10, 1000, 2, "Title", "SubTitle");
	}
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
	private static String toolToString(Material tool)
	{
		return Txt.upperCaseFirst(tool.name().substring(tool.name().indexOf("_")+1).toLowerCase());
	}


}
