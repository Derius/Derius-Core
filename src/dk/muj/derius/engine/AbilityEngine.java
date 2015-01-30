package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.collections.WorldExceptionSet;

import dk.muj.derius.Derius;
import dk.muj.derius.entity.Ability;
import dk.muj.derius.entity.Ability.AbilityType;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.req.ReqAbilityCanBeUsedInArea;
import dk.muj.derius.util.ChatUtil;

public class AbilityEngine extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static AbilityEngine i = new AbilityEngine();
	public static AbilityEngine get() { return i; }
	public AbilityEngine() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return Derius.get();
	}
	
	// -------------------------------------------- //
	// EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRegister(AbilityRegisteredEvent e)
	{
		Ability ability = e.getAbility();
		Skill skill = ability.getSkill();
		if (ability.getType() == AbilityType.ACTIVE)
		{
			skill.getActiveAbilities().add(ability);
		}
		else if (ability.getType() == AbilityType.PASSIVE)
		{
			skill.getPassiveAbilities().add(ability);
		}
		
		if ( ! MConf.get().worldAbilityUse.containsKey(ability.getId()))
		{
			MConf.get().worldAbilityUse.put(ability.getId(), new WorldExceptionSet());
		}
		
		// Requirements
		ability.addActivateRequirements(ReqAbilityCanBeUsedInArea.get());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onActivate(AbilityActivateEvent event)
	{
		Ability ability = event.getAbility();
		MPlayer mplayer = event.getMPlayer();
		if (ability.getType() == AbilityType.ACTIVE)
		{
			ChatUtil.msgAbilityActivate(mplayer, ability);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeactivate(AbilityDeactivateEvent event)
	{
		Ability ability = event.getAbility();
		MPlayer mplayer = event.getMPlayer();
		if (ability.getType() == AbilityType.ACTIVE)
		{
			ChatUtil.msgAbilityDeactivate(mplayer, ability);
		}
	}
	
}
