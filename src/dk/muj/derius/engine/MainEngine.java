package dk.muj.derius.engine;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.util.EventUtil;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Ability.AbilityType;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.mplayer.MPlayer;
import dk.muj.derius.entity.mplayer.MPlayerColl;
import dk.muj.derius.entity.skill.SkillColl;
import dk.muj.derius.events.AbilityRegisteredEvent;
import dk.muj.derius.events.PlayerDamageEvent;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.req.ReqAbilityCanBeUsedInArea;
import dk.muj.derius.req.ReqIsEnabled;
import dk.muj.derius.req.sp.ReqHasOpenSlot;
import dk.muj.derius.req.sp.ReqIsntAutoAssigned;
import dk.muj.derius.req.sp.ReqIsntBlacklisted;
import dk.muj.derius.req.sp.ReqIsntSpecialised;
import dk.muj.derius.req.sp.ReqSpCooldownIsExpired;
import dk.muj.derius.scoreboard.ScoreboardUtil;
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
		if (ability.getType() == AbilityType.ACTIVE)
		{
			skill.getAbilities().add(ability);
		}
		else if (ability.getType() == AbilityType.PASSIVE)
		{
			skill.getAbilities().add(ability);
		}
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
		MPlayer mplayer = MPlayerColl.get().get(event.getPlayer(), true);
		for (Skill skill : SkillColl.getAllSkills())
		{
			mplayer.instantiateSkill(skill);
		}
		
		if (mplayer.getSpecialisationCooldownExpire() == 0)
		{
			mplayer.setSpecialisationChangeMillis(System.currentTimeMillis());
		}
		
		if (mplayer.getBoardShowAtAll() && mplayer.getStaminaBoardStay())
		{
			ScoreboardUtil.updateStaminaScore(mplayer, 5);
		}
		return;
	}
	
	// -------------------------------------------- //
	// OTHER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void callListener(BlockBreakEvent event)
	{	
		Block block = event.getBlock();
		
		// Listeners
		Listener listener = Listener.getBlockBreakListener(block.getType());
		if (listener == null) return;
		
		// Check if player placed block
		if (DeriusCore.getBlockMixin().isBlockPlacedByPlayer(block)) return;
		
		listener.onBlockBreak(DeriusAPI.getDPlayer(event.getPlayer()), block.getState());
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)//, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event)
	{	
		Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR) return;
		
		DPlayer mplayer = DeriusAPI.getDPlayer(event.getPlayer());
		
		mplayer.setPreparedTool(Optional.ofNullable(event.getMaterial()));
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerAttack(EntityDamageByEntityEvent event)
	{
		if ( ! (event.getDamager() instanceof Player)) return;
		Player player = (Player) event.getDamager();
		Listener listener = Listener.getPlayerAttackKeyListener(player.getItemInHand().getType());
		if (listener == null) return;
		
		listener.onPlayerAttack(DeriusAPI.getDPlayer(player), event);
		
		return;
	}

	// -------------------------------------------- //
	// MUTIPLIER
	// -------------------------------------------- //
	
	
	// TODO: Break out into expansion
	/*@EventHandler(priority = EventPriority.LOWEST)
	public void muliplier(PlayerAddExpEvent event)
	{
		CommandSender sender = event.getDPlayer().getSender();
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
	}*/
	
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
