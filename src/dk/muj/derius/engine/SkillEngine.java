package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.collections.WorldExceptionSet;

import dk.muj.derius.Derius;
import dk.muj.derius.Perm;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.skill.Skill;

public class SkillEngine extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static SkillEngine i = new SkillEngine();
	public static SkillEngine get() { return i; }
	public SkillEngine() {}
	
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
	public void onSkillRegistered(SkillRegisteredEvent e)
	{
		Skill skill = e.getSkill();
		int id = skill.getId();
		for (MPlayer p: MPlayerColl.get().getAll())
		{
			p.InstantiateSkill(skill);
		}
		if(!MConf.get().worldSkillsEarn.containsKey(id))
		{
			MConf.get().worldSkillsEarn.put(id, new WorldExceptionSet());
		}
		skill.addLearnRequirements(ReqHasPerm.get(Perm.SKILL_LEARN.node + skill.getId()));
		skill.addSeeRequirements(ReqHasPerm.get(Perm.SKILL_SEE.node + skill.getId()));
	}
}
