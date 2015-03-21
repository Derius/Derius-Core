package dk.muj.derius;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

import dk.muj.derius.api.BlockBreakExpGain;
import dk.muj.derius.api.Derius;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ScheduledDeactivate;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.events.AbilityRegisteredEvent;
import dk.muj.derius.api.events.SkillRegisteredEvent;
import dk.muj.derius.api.inventory.SpecialItemManager;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.engine.EngineActivate;
import dk.muj.derius.engine.EngineMain;
import dk.muj.derius.engine.EngineScheduledDeactivate;
import dk.muj.derius.entity.AbilityColl;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.entity.mplayer.MPlayerColl;
import dk.muj.derius.inventory.ItemManager;

public class DeriusCore implements Derius
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusCore i;
	public static DeriusCore get() { return i; }
	private DeriusCore() { i = this; }
	
	// -------------------------------------------- //
	// INJECT
	// -------------------------------------------- //
	
	public static void inject()
	{
		Class<DeriusAPI> apiClass = DeriusAPI.class;
		Field coreField = ReflectionUtil.getField(apiClass, DeriusConst.API_DERIUS_FIELD);
		if (coreField != null) // Avoid useless NPE, since we already got one exception.
		{	
			if (ReflectionUtil.makeAccessible(coreField)); // Returns true on success.
			{
				ReflectionUtil.setField(coreField, null, DeriusCore.get());
			}
		}
	}
	
	// -------------------------------------------- //
	// DEBUG
	// -------------------------------------------- //
	
	@Override
	public void debug(int level, String format, Object... args)
	{
		if (MConf.get().debugLevel < level) return;
		
		String message = Txt.parse(format, args);
		DeriusPlugin.get().log(Level.INFO, message);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: DATABASE
	// -------------------------------------------- //
	
	@Override
	public Gson getGson(Plugin plugin)
	{
		if (plugin instanceof MassivePlugin)
		{
			return ((MassivePlugin) plugin).gson;
		}
		return DeriusPlugin.get().gson;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: DPLAYER
	// -------------------------------------------- //
	
	@Override
	public Collection<? extends DPlayer> getAllDPlayers()
	{
		return MPlayerColl.get().getAll();
	}
	
	@Override
	public DPlayer getDPlayer(Object senderObject)
	{
		return MPlayerColl.get().get(senderObject, false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: SKILL
	// -------------------------------------------- //
	
	@Override
	public Collection<? extends Skill> getAllSkills()
	{
		return SkillColl.getAllSkills();
	}
	
	@Override
	public Skill getSkill(String id)
	{
		return SkillColl.get().get(id);
	}
	
	@Override
	public void registerSkill(Skill skill)
	{
		SkillRegisteredEvent event = new SkillRegisteredEvent(skill);
		if ( ! event.runEvent()) return;
		
		DeriusAPI.debug(1000, "<i>Registering skill <h>%s<i>.", skill.getName());
		
		if ( skill.isRegistered()) throw new IllegalArgumentException("Skill is already registered.");
		
		this.ensureSkillIsLoaded(skill.getId());
		Skill old = SkillColl.get().get(skill.getId(), false);
		// If an old skill was in the system...
		if (old != null)
		{
			DeriusAPI.debug(1000, "<i>Loading old values for skill <h>%s<i>.", skill.getName());
			// ...it configurations we would like to keep.
			skill.load(old);
			// Now remove the old one from the system.
			SkillColl.get().removeAtLocal(skill.getId());
		}
		SkillColl.get().attach(skill, skill.getId());
		
		return;
	}
	
	// Sometimes skills aren't loaded properly on init.
	// This method should counter that.
	private void ensureSkillIsLoaded(String id)
	{
		SkillColl coll = SkillColl.get();
		// This might fail if the skill actually wasn't in the database...
		try
		{
			// First we load the value.
			Entry<JsonElement, Long> value = coll.getDb().load(coll, id);
			if (value.getValue() == null || value.getValue().longValue() == 0) return;
			// Then we load it into our system.
			coll.loadFromRemote(id, value);
		}
		// ...so we fail silently.
		catch(Throwable t) { t.printStackTrace(); } // We might fail silently, when releasing the plugin.
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: ABILITY
	// -------------------------------------------- //
	
	@Override
	public Collection<? extends Ability> getAllAbilities()
	{
		return AbilityColl.getAllAbilities();
	}
	
	@Override
	public Ability getAbility(String id)
	{
		return AbilityColl.get().get(id);
	}
	
	@Override
	public void registerAbility(Ability ability)
	{
		AbilityRegisteredEvent event = new AbilityRegisteredEvent(ability);
		if ( ! event.runEvent()) return;
		
		if (ability.isRegistered()) throw new IllegalArgumentException("Ability is already registered.");
		
		this.ensureAbilityIsLoaded(ability.getId());
		Ability old = AbilityColl.get().get(ability.getId(), false);
		if (old != null)
		{
			ability.load(old);
			AbilityColl.get().removeAtLocal(ability.getId());
		}
		
		AbilityColl.get().attach(ability, ability.getId());
		return;
	}
	
	private void ensureAbilityIsLoaded(String id)
	{
		AbilityColl coll = AbilityColl.get();
		// This might fail if the ability actually wasn't in the database...
		try
		{
			// First we load the value.
			Entry<JsonElement, Long> value = coll.getDb().load(coll, id);
			if (value.getValue() == null || value.getValue().longValue() == 0) return;
			// Then we load it into our system.
			coll.loadFromRemote(id, value);
		}
		catch(Throwable t) { t.printStackTrace(); } // We might fail silently, when releasing the plugin.
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: EXP
	// -------------------------------------------- //
	
	public void registerExpGain(BlockBreakExpGain gainer)
	{
		EngineActivate.registerExpGain(gainer);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: PREPARABLE TOOLS
	// -------------------------------------------- //
	
	@Override
	public Collection<Material> getPreparableTools()
	{
		return EngineMain.getPreparableTools();
	}
	
	@Override
	public void registerPreparableTools(Collection<Material> materials)
	{
		EngineMain.registerPreparableTools(materials);
	}
	
	@Override
	public void registerPreparableTool(Material material)
	{
		EngineMain.registerPreparableTool(material);
	}
	
	@Override
	public boolean isRegisteredAsPreparable(Material material)
	{
		return EngineMain.isRegisteredAsPreparable(material);
	}
	
	@Override
	public void unregisterPreparableTool(Material material)
	{
		EngineMain.unregisterPreparableTool(material);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS: SCHEDULED TELEPORT
	// -------------------------------------------- //
	
	@Override
	public boolean isScheduled(ScheduledDeactivate sd)
	{
		return EngineScheduledDeactivate.get().isScheduled(sd);
	}
	
	@Override
	public void schedule(ScheduledDeactivate sd)
	{
		EngineScheduledDeactivate.get().schedule(sd);
	}
	
	// -------------------------------------------- //
	// OTHER
	// -------------------------------------------- //
	
	/**
	 * Registers a special item manager, used to avoid cheating.
	 * @param {SpecialItemManager} manager to activate.
	 */
	public void registerSpecialItemManager(SpecialItemManager manager)
	{
		ItemManager.addManager(manager);
	}

}
