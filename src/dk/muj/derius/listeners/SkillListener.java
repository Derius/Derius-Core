package dk.muj.derius.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.WorldException;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.skill.Skill;

public class SkillListener implements Listener
{
	MassivePlugin plugin;
	public SkillListener(MassivePlugin plugin)
	{
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRegistered(SkillRegisteredEvent e)
	{
		Skill skill = e.getSkill();
		String id = skill.getId();
		for (MPlayer p: MPlayerColl.get().getAll())
			p.InstantiateSkill(id);
		
		MConf.get().worldSkillsUse.put(id, new WorldException());
		MConf.get().worldSkillsEarn.put(id, new WorldException());
	}
}
