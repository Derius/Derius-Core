package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Ability.AbilityType;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;
import dk.muj.derius.events.PlayerLevelUpEvent;

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
	
}
