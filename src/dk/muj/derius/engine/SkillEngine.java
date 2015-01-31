package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.Derius;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.req.sp.ReqHasOpenSlot;
import dk.muj.derius.req.sp.ReqIsntAutoAssigned;
import dk.muj.derius.req.sp.ReqIsntBlacklisted;
import dk.muj.derius.req.sp.ReqIsntSpecialised;

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
	public void onSkillRegistered(SkillRegisteredEvent event)
	{
		Skill skill = event.getSkill();
		for (MPlayer mplayer : MPlayerColl.get().getAll())
		{
			mplayer.instantiateSkill(skill);
		}
		
		skill.addSpecialiseRequirements(ReqIsntAutoAssigned.get());
		skill.addSpecialiseRequirements(ReqIsntBlacklisted.get());
		skill.addSpecialiseRequirements(ReqIsntSpecialised.get());
		skill.addSpecialiseRequirements(ReqHasOpenSlot.get());
		
		return;
	}
	
}
