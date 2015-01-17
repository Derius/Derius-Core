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
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.req.ReqHasPerm;
import dk.muj.derius.req.sp.ReqHasOpenSlot;
import dk.muj.derius.req.sp.ReqIsntAutoAssigned;
import dk.muj.derius.req.sp.ReqIsntBlacklisted;
import dk.muj.derius.req.sp.ReqIsntSpecialised;
import dk.muj.derius.skill.Skill;

public class SkillEngine extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static SkillEngine i = new SkillEngine();
	public static SkillEngine get() { return i; }
	private SkillEngine() {}
	
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
		if ( ! MConf.get().worldSkillsEarn.containsKey(id))
		{
			MConf.get().worldSkillsEarn.put(id, new WorldExceptionSet());
		}
		
		String name = skill.getName();
		
		Permission learnPerm = new Permission(Perm.SKILL_LEARN.node + id, "learn the skill " + name, PermissionDefault.FALSE);
		Permission seePerm = new Permission(Perm.SKILL_SEE.node + id, "see the skill " + name, PermissionDefault.FALSE);
		Permission spPerm = new Permission(Perm.SKILL_SPECIALISE.node + id, "specialise in the skill " + name, PermissionDefault.FALSE);
		
		skill.addLearnRequirements(ReqHasPerm.get(learnPerm));
		skill.addSeeRequirements(ReqHasPerm.get(seePerm));
		
		skill.addSpecialiseRequirements(ReqHasPerm.get(spPerm));
		skill.addSpecialiseRequirements(ReqIsntAutoAssigned.get());
		skill.addSpecialiseRequirements(ReqIsntBlacklisted.get());
		skill.addSpecialiseRequirements(ReqIsntSpecialised.get());
		skill.addSpecialiseRequirements(ReqHasOpenSlot.get());
	}
}
