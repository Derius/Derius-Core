package dk.muj.derius.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.ability.Abilities;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MPlayer;

public class PlayerListener implements Listener
{
	MassivePlugin plugin;
	public PlayerListener(MassivePlugin plugin)
	{
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e)
	{	
		Player p = e.getPlayer();
		Action action = e.getAction();
		if(action != Action.RIGHT_CLICK_AIR)
			return;
		Ability ability = Abilities.getAbilityByInteractKey(e.getMaterial());
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
			mplayer.ActivateActiveAbility(ability, ability.getTicksLast(mplayer.getLvl(ability.getSkill())));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e)
	{	
		Player p = e.getPlayer();
		
		Ability ability = Abilities.getAbilityByBlockBreakKey(e.getBlock().getType());
		if(ability == null)
			return;
		
		MPlayer mplayer = MPlayer.get(p.getUniqueId().toString());
		
		if(ability.CanAbilityBeUsedInArea(p.getLocation()))
			mplayer.ActivatePassiveAbility(ability);
	}
}