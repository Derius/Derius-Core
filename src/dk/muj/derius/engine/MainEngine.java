package dk.muj.derius.engine;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.Derius;
import dk.muj.derius.WorldException;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.SkillRegisteredEvent;
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
		for(Skill s: Skill.GetAllSkills())
			p.InstantiateSkill(s);
		if(p.getSpecialisationCooldownExpire() == 0)
			p.setSpecialisationChangeMillis(System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSkillRegistered(SkillRegisteredEvent e)
	{
		Skill skill = e.getSkill();
		int id = skill.getId();
		for (MPlayer p: MPlayerColl.get().getAll())
			p.InstantiateSkill(skill);
		if(!MConf.get().worldSkillsEarn.containsKey(id))
			MConf.get().worldSkillsEarn.put(id, new WorldException());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onExpGain(PlayerAddExpEvent e)
	{
		Player p = e.getPlayer().getPlayer();
		String perm = "derius.multiplier.";
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
	public void onBlockBreak(BlockBreakEvent e)
	{	
		Listener listener = Listener.getListener(e.getBlock().getType());
		if(listener == null)
			return;
		listener.onBlockBreak(MPlayer.get(e.getPlayer().getUniqueId().toString()), e.getBlock());
		
	}


}
