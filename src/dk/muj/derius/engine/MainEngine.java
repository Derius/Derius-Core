package dk.muj.derius.engine;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.util.EventUtil;

import dk.muj.derius.Derius;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerDamageEvent;
import dk.muj.derius.util.Listener;

public class MainEngine extends EngineAbstract
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static MainEngine i = new MainEngine();
	public static MainEngine get() { return i; }
	public MainEngine() {}
	
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
	
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event)
	{
		MPlayer mplayer = MPlayerColl.get().get(event.getPlayer(), true);
		for (Skill skill : SkillColl.getAllSkills())
		{
			mplayer.instantiateSkill(skill);
		}
		if (mplayer.getSpecialisationCooldownExpire() == 0)
		{
			mplayer.setSpecialisationChangeMillis(System.currentTimeMillis());
		}
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event)
	{	
		Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR) return;
		
		MPlayer mplayer = MPlayer.get(event.getPlayer());
		
		mplayer.setPreparedTool(Optional.ofNullable(event.getMaterial()));
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerAttack(EntityDamageByEntityEvent event)
	{
		if ( ! (event.getDamager() instanceof Player)) return;
		Player p = (Player) event.getDamager();
		Listener listener = Listener.getPlayerAttackKeyListener(p.getItemInHand().getType());
		if (listener == null)
		{
			return;
		}
		
		listener.onPlayerAttack(MPlayer.get(p), event);
		
		return;
	}
	
	// -------------------------------------------- //
	// MUTIPLIER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void muliplier(PlayerAddExpEvent event)
	{
		CommandSender sender = event.getMPlayer().getSender();
		if (sender == null) return;
		Skill skill = event.getSkill();
		
		double exp = event.getExpAmount();
		
		exp *= MConf.get().baseMultiplier;
		exp *= event.getSkill().getMultiplier();
		
		for (int i = 100; i >= 0; i++)
		{
			if ( ! sender.hasPermission("derius.basemultiplier." + i)) continue;
			exp *= i/10.0;
			break;
		}
		
		for (int i = 100; i >= 0; i++)
		{
			if ( ! sender.hasPermission("derius.skillmultiplier." + skill.getId() + "." + i)) continue;
			exp *= i/10.0;
			break;
		}
		
		event.setExpAmount(exp);
	}
	
	// -------------------------------------------- //
	// PLAYER TAKE DAMAGE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageLowest(EntityDamageEvent event)
	{
		if ( ! (event.getEntity() instanceof Player)) return;
		PlayerDamageEvent thrown = new PlayerDamageEvent(event);
		EventUtil.callEventAt(thrown, EventPriority.LOWEST);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageLow(EntityDamageEvent event)
	{
		if ( ! (event.getEntity() instanceof Player)) return;
		PlayerDamageEvent thrown = new PlayerDamageEvent(event);
		EventUtil.callEventAt(thrown, EventPriority.LOW);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageNormal(EntityDamageEvent event)
	{
		if ( ! (event.getEntity() instanceof Player)) return;
		PlayerDamageEvent thrown = new PlayerDamageEvent(event);
		EventUtil.callEventAt(thrown, EventPriority.NORMAL);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageHigh(EntityDamageEvent event)
	{
		if ( ! (event.getEntity() instanceof Player)) return;
		PlayerDamageEvent thrown = new PlayerDamageEvent(event);
		EventUtil.callEventAt(thrown, EventPriority.HIGH);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageHighest(EntityDamageEvent event)
	{
		if ( ! (event.getEntity() instanceof Player)) return;
		PlayerDamageEvent thrown = new PlayerDamageEvent(event);
		EventUtil.callEventAt(thrown, EventPriority.HIGHEST);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageMonitor(EntityDamageEvent event)
	{
		if ( ! (event.getEntity() instanceof Player)) return;
		PlayerDamageEvent thrown = new PlayerDamageEvent(event);
		EventUtil.callEventAt(thrown, EventPriority.MONITOR);
	}

}
