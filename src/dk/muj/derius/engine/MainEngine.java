package dk.muj.derius.engine;

import java.util.Optional;

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
import dk.muj.derius.Perm;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.PlayerDamageEvent;
import dk.muj.derius.skill.Skill;
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e)
	{
		MPlayer p = MPlayerColl.get().get(e.getPlayer().getUniqueId().toString(), true);
		for(Skill s: Skill.getAllSkills())
			p.InstantiateSkill(s);
		if(p.getSpecialisationCooldownExpire() == 0)
			p.setSpecialisationChangeMillis(System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e)
	{	
		Player p = e.getPlayer();
		Action action = e.getAction();
		if(action != Action.RIGHT_CLICK_AIR)
			return;
		
		MPlayer mplayer = MPlayer.get(p);
		
		mplayer.setPreparedTool(e.getMaterial() == null ? Optional.empty() : Optional.of(e.getMaterial()));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onExpGain(PlayerAddExpEvent e)
	{
		Player p = e.getPlayer().getPlayer();
		String perm = Perm.EXP_MULTIPLIER.node;
		for(int i = 100; i > 0; i--)
		{
			if(p.hasPermission(perm + i))
			{
				long startExp = e.getExpAmount();
				float multiplier = i/10;
				e.setExpAmount((long) (startExp*multiplier));
				break;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerAttack(EntityDamageByEntityEvent event)
	{
		if( ! (event.getDamager() instanceof Player)) return;
		Player player = (Player) event.getDamager();
		Listener listener = Listener.getPlayerAttackKeyListener(player.getItemInHand().getType());
		if(listener == null)
			return;
		listener.onPlayerAttack(MPlayer.get(player), event);
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
