package dk.muj.derius.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.events.AbilityRegisteredEvent;
import dk.muj.derius.api.events.SkillRegisteredEvent;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.req.ReqAbilityCanBeUsedInArea;
import dk.muj.derius.api.req.ReqHasOpenSlot;
import dk.muj.derius.api.req.ReqIsEnabled;
import dk.muj.derius.api.req.ReqIsntAutoAssigned;
import dk.muj.derius.api.req.ReqIsntBlacklisted;
import dk.muj.derius.api.req.ReqIsntSpecialised;
import dk.muj.derius.api.req.ReqSpCooldownIsExpired;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.entity.mplayer.MPlayer;
import dk.muj.derius.entity.mplayer.MPlayerColl;

public class EngineMain extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMain i = new EngineMain();
	public static EngineMain get() { return i; }
	public EngineMain() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusCore.get();
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final static Collection<Material> preparableTools = new HashSet<>();
	public static Collection<Material> getPreparableTools() { return preparableTools; }
	public static void registerPreparableTools(Collection<Material> materials) { preparableTools.addAll(materials);}
	public static void registerPreparableTool(Material material) { preparableTools.add(material); }
	public static boolean isRegisteredAsPreparable(Material material) { return getPreparableTools().contains(material); }
	public static void unregisterPreparableTool(Material material) { preparableTools.remove(material); }
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void setupSkill(SkillRegisteredEvent event)
	{
		Skill skill = event.getSkill();
		for (MPlayer mplayer : MPlayerColl.get().getAll())
		{
			mplayer.instantiateSkill(skill);
		}
		
		// Requirements
		skill.addLearnRequirements(ReqIsEnabled.get());
		skill.addSeeRequirements(ReqIsEnabled.get());
		skill.addSpecialiseRequirements(ReqIsEnabled.get());
		
		
		skill.addSpecialiseRequirements(ReqIsntAutoAssigned.get());
		skill.addSpecialiseRequirements(ReqIsntBlacklisted.get());
		skill.addSpecialiseRequirements(ReqIsntSpecialised.get());
		skill.addSpecialiseRequirements(ReqHasOpenSlot.get());
		skill.addSpecialiseRequirements(ReqSpCooldownIsExpired.get());
		
		return;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void setupAbility(AbilityRegisteredEvent event)
	{
		Ability ability = event.getAbility();
		Skill skill = ability.getSkill();
		skill.getAbilities().add(ability);
		// Requirements
		ability.addActivateRequirements(ReqAbilityCanBeUsedInArea.get());
		
		ability.addActivateRequirements(ReqIsEnabled.get());
		ability.addSeeRequirements(ReqIsEnabled.get());
		
		return;
	}
	
	// -------------------------------------------- //
	// INSTANTIATE PLAYER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW)
	public void instantiatePlayerFields(PlayerJoinEvent event)
	{
		instantiatePlayerFields(event.getPlayer().getUniqueId().toString());
	}
	
	public static void instantiatePlayerFields(String playerId)
	{
		MPlayer mplayer = MPlayerColl.get().get(playerId, true);
		
		for (Skill skill : DeriusAPI.getAllSkills())
		{
			mplayer.instantiateSkill(skill);
		}
		
		if (mplayer.getSpecialisationCooldownExpire() == 0)
		{
			mplayer.setSpecialisationChangeMillis(System.currentTimeMillis());
		}
		
		if (mplayer.isPlayer()) mplayer.getScoreboard();
		
		mplayer.setStamina(0.0);
		return;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void deinstantiatePlayerFields(EventMassiveCorePlayerLeave event)
	{
		deinstantiatePlayerFields(event.getPlayer().getUniqueId().toString());
	}
	
	public static void deinstantiatePlayerFields(String playerId)
	{
		MPlayer mplayer = MPlayerColl.get().get(playerId, false);

		if (mplayer == null) return;
		
		mplayer.setScoreboard(null);
		return;
	}
	
	// -------------------------------------------- //
	// CHAT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChat(AsyncPlayerChatEvent event)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(event.getPlayer());
		
		if ( ! mplayer.isChatListeningOk()) return;
		
		Ability ability = mplayer.getAbilityBySubString(event.getMessage().toLowerCase());
		if (ability == null) return;
		
		AbilityUtil.activateAbility(mplayer, ability, null, VerboseLevel.NORMAL);
		
		return;
	}

	// -------------------------------------------- //
	// CALL LISTENERS
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void setPreparedTool(PlayerInteractEvent event)
	{	
		if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
		
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		
		dplayer.setPreparedTool(Optional.ofNullable(event.getMaterial()));
		
		return;
	}

}
