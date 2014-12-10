package dk.muj.derius.engine;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.Derius;
import dk.muj.derius.WorldException;
import dk.muj.derius.ability.Abilities;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.skill.Skill;

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
	
	@Override
	public Long getPeriod()
	{
		return (long) (20*10);
	}
	
	@Override
	public Long getDelay()
	{
		return (long) (20*60);
	}
	
	// -------------------------------------------- //
	// EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRegister(AbilityRegisteredEvent e)
	{
		Ability a = e.getAbility();
		Skill s = a.getSkill();
		if(a.getType() == AbilityType.ACTIVE)
			s.getActiveAbilities().add(a);
		else if(a.getType() == AbilityType.PASSIVE)
			s.getPassiveAbilities().add(a);
		
		Ability ability = e.getAbility();
		if(MConf.get().worldAbilityUse.get(new Integer(ability.getId())) == null)
			MConf.get().worldAbilityUse.put(ability.getId(), new WorldException());
	}
	
	// -------------------------------------------- //
	// REPEAT TASK
	// -------------------------------------------- //

	@Override
	public void run()
	{
		for(Ability a: Abilities.GetAllAbilities())
		{
			if(!a.DidChange())
				return;
			
			for(Material m: a.getRemovedBlockBreakKeys())
				Abilities.removeBlockBreakKey(m);
			a.getRemovedBlockBreakKeys().clear();
			
			for(Material m: a.getRemovedInteractKeys())
				Abilities.removeInteractKey(m);
			a.getRemovedInteractKeys().clear();
			
			for(Material m: a.getBlockBreakKeys())
				Abilities.addBlockBreakKey(m, a);
			
			for(Material m: a.getInteractKeys())
				Abilities.addInteractKey(m, a);
			
			a.setChange(false);
		}
	}
}
