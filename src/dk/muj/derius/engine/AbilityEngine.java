package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
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
import dk.muj.derius.req.ReqAreaIsOkForAbility;
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
			MConf.get().worldAbilityUse.put(ability.getId(), new WorldExceptionSet());
		// Requirements
		ability.addActivateRequirements(ReqAreaIsOkForAbility.get(ability));
		ability.addActivateRequirements(ReqHasPerm.get(Perm.ABILITY_USE.node + ability.getId()));
		ability.addSeeRequirements(ReqHasPerm.get(Perm.ABILITY_SEE.node + ability.getId()));
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
