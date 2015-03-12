package dk.muj.derius.engine;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

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
import dk.muj.derius.util.ScoreboardUtil;

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
		
		if (mplayer.getBoardShowAtAll() && mplayer.getStaminaBoardStay())
		{
			ScoreboardUtil.updateStaminaScore(mplayer, 5, mplayer.getStamina());
		}
		
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
	// STAMINA
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void blockSprintOnLowStamina(PlayerToggleSprintEvent event)
	{
		if ( ! event.isSprinting()) return;
		Player player = event.getPlayer();
		DPlayer dplayer = DeriusAPI.getDPlayer(player);
		event.setCancelled(dplayer.getStamina() < DeriusAPI.staminaSprintLimit(player));
	}
	
	// -------------------------------------------- //
	// CALL LISTENERS
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event)
	{	
		Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR) return;
		
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		
		dplayer.setPreparedTool(Optional.ofNullable(event.getMaterial()));
		
		return;
	}

}
