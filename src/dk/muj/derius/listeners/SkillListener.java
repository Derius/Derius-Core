package dk.muj.derius.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.WorldException;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.events.PlayerAddExpEvent;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.skill.Skill;

public class SkillListener implements Listener
{
	MassivePlugin plugin;
	public SkillListener(MassivePlugin plugin)
	{
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSkillRegistered(SkillRegisteredEvent e)
	{
		Skill skill = e.getSkill();
		int id = skill.getId();
		for (MPlayer p: MPlayerColl.get().getAll())
			p.InstantiateSkill(skill);
		
		MConf.get().worldSkillsUse.put(id, new WorldException());
		MConf.get().worldSkillsEarn.put(id, new WorldException());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAbilityRegistered(AbilityRegisteredEvent e)
	{
		Ability ability = e.getAbility();
		MConf.get().worldAbilityUse.put(ability.getId(), new WorldException());
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
}
