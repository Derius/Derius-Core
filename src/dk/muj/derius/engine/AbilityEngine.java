package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Ability.AbilityType;
import dk.muj.derius.api.Skill;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.req.ReqAbilityCanBeUsedInArea;
import dk.muj.derius.req.ReqIsEnabled;

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
		return DeriusCore.get();
	}
	
	// -------------------------------------------- //
	// EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRegister(AbilityRegisteredEvent event)
	{
		Ability ability = event.getAbility();
		Skill skill = ability.getSkill();
		if (ability.getType() == AbilityType.ACTIVE)
		{
			skill.getAbilities().add(ability);
		}
		else if (ability.getType() == AbilityType.PASSIVE)
		{
			skill.getAbilities().add(ability);
		}
		// Requirements
		ability.addActivateRequirements(ReqAbilityCanBeUsedInArea.get());
		
		ability.addActivateRequirements(ReqIsEnabled.get());
		ability.addSeeRequirements(ReqIsEnabled.get());
		
		return;
	}
	
}
