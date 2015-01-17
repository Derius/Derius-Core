package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.collections.WorldExceptionSet;

import dk.muj.derius.Derius;
import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.AbilityActivateEvent;
import dk.muj.derius.events.AbilityDeactivateEvent;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.req.ReqAbilityCanBeUsedInArea;
import dk.muj.derius.req.ReqHasPerm;
import dk.muj.derius.skill.Skill;
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
		if(ability.getType() == AbilityType.ACTIVE)
			skill.getActiveAbilities().add(ability);
		else if(ability.getType() == AbilityType.PASSIVE)
			skill.getPassiveAbilities().add(ability);
		
		if(MConf.get().worldAbilityUse.get(new Integer(ability.getId())) == null)
		{
			MConf.get().worldAbilityUse.put(ability.getId(), new WorldExceptionSet());
		}
		
		String name = ability.getName();
		int id = ability.getId();
		
		Permission usePerm = new Permission(Perm.ABILITY_USE.node + id, "use the ability " + name, PermissionDefault.FALSE);
		Permission seePerm = new Permission(Perm.ABILITY_SEE.node + id, "see the ability " + name, PermissionDefault.FALSE);
		
		// Requirements
		ability.addActivateRequirements(ReqAbilityCanBeUsedInArea.get());
		ability.addActivateRequirements(ReqHasPerm.get(usePerm));
		ability.addSeeRequirements(ReqHasPerm.get(seePerm));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onActivate(AbilityActivateEvent event)
	{
		Ability ability = event.getAbility();
		MPlayer player = event.getMPlayer();
		if(ability.getType() == AbilityType.ACTIVE)
			ChatUtil.msgAbilityActivate(player, ability);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeactivate(AbilityDeactivateEvent event)
	{
		Ability ability = event.getAbility();
		MPlayer player = event.getMPlayer();
		if(ability.getType() == AbilityType.ACTIVE)
			ChatUtil.msgAbilityDeactivate(player, ability);
	}
}
