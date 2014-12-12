package dk.muj.derius.engine;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.WorldException;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.AbilityActivateEvent;
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
	
	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e)
	{	
		Player p = e.getPlayer();
		Action action = e.getAction();
		if(action == Action.RIGHT_CLICK_AIR)
			return;
		
		Ability ability = Ability.getAbilityByInteractKey(e.getMaterial());
		if(ability == null)
			return;
		
		MPlayer mplayer = MPlayer.get(p.getUniqueId().toString());
		
		if(mplayer.HasActivatedAny())
			return;

		if (!mplayer.hasCooldownExpired(true))
			return;

		if(!ability.CanPlayerActivateAbility(mplayer))
			return;

		if(ability.CanAbilityBeUsedInArea(p.getLocation()))
			mplayer.ActivateAbility(ability);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e)
	{	
		Player p = e.getPlayer();
		
		Ability ability = Ability.getAbilityByBlockBreakKey(e.getBlock().getType());
		if(ability == null)
			return;
		
		MPlayer mplayer = MPlayer.get(p.getUniqueId().toString());
		
		if(ability.CanAbilityBeUsedInArea(p.getLocation()))
			mplayer.ActivateAbility(ability, e.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onActivate(AbilityActivateEvent e)
	{
		Ability a = e.getAbility();
		if(a.getType() == AbilityType.ACTIVE)
			e.getMPlayer().msg(Txt.parse(MConf.get().abilityActivatedMsg, a.getName()));
		
		if(!MConf.get().worldAbilityUse.containsKey(a.getId()))
			MConf.get().worldAbilityUse.put(a.getId(), new WorldException());
	}
}
