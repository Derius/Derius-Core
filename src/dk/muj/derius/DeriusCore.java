package dk.muj.derius;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

import dk.muj.derius.adapter.AbilityAdapter;
import dk.muj.derius.adapter.SkillAdapter;
import dk.muj.derius.api.Derius;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ScheduledDeactivate;
import dk.muj.derius.api.ability.Ability;
import dk.muj.derius.api.events.AbilityRegisteredEvent;
import dk.muj.derius.api.events.SkillRegisteredEvent;
import dk.muj.derius.api.inventory.SpecialItemManager;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.cmd.CmdDerius;
import dk.muj.derius.engine.EngineActivate;
import dk.muj.derius.engine.EngineMain;
import dk.muj.derius.engine.EngineMsg;
import dk.muj.derius.engine.EngineScheduledDeactivate;
import dk.muj.derius.entity.AbilityColl;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MConfColl;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MLangColl;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.entity.mplayer.MPlayer;
import dk.muj.derius.entity.mplayer.MPlayerAdapter;
import dk.muj.derius.entity.mplayer.MPlayerColl;
import dk.muj.derius.inventory.ItemManager;
import dk.muj.derius.mixin.BlockMixinDefault;
import dk.muj.derius.mixin.MaxLevelMixinDefault;
import dk.muj.derius.mixin.StaminaMixinDefault;
import dk.muj.derius.task.TaskPlayerStaminaUpdate;

public class DeriusCore extends MassivePlugin implements Derius
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusCore i;
	public static DeriusCore get() { return i; }
	public DeriusCore() { i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands 
	// "outer" means it is accessed directly as a command. ex. "/example"
	private CmdDerius outerCmdDerius;
	public CmdDerius getOuterCmdDerius() { return this.outerCmdDerius; }
	
	// Engines
	private List<Engine> engines = MUtil.list(
		EngineActivate.get(),
		EngineMain.get(),
		EngineScheduledDeactivate.get(),
		EngineMsg.get());
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
		
		// We must first init the collections...
		MConfColl.get().init();
		MLangColl.get().init();
		MPlayerColl.get().init();
		SkillColl.get().init();
		AbilityColl.get().init();
		
		// ... because some of them are used when instantiating the api fields.
		this.setApiFields();
		
		// Engine activation
		engines.forEach(Engine::activate);

		ItemManager.setup();
		
		// Command registration
		this.outerCmdDerius = new CmdDerius() { public List<String> getAliases() { return MConf.get().outerAliasesDerius; } };
		this.outerCmdDerius.register(this);
		
		EngineMain.instantiatePlayerFields(IdUtil.CONSOLE_ID);
		
		for (Player player : MUtil.getOnlinePlayers())
		{
			EngineMain.instantiatePlayerFields(player.getUniqueId().toString());
		}
		
		// ModulaRepeatTask
		TaskPlayerStaminaUpdate.get().activate();
		
		this.postEnable();
	}
	

	@Override
	public void onDisable()
	{
		super.onDisable();
		
		// Engine deactivation
		engines.forEach(Engine::deactivate);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVEPLUGIN
	// -------------------------------------------- //
	
	@Override
	public GsonBuilder getGsonBuilder()
	{
		return super.getGsonBuilder()
				.registerTypeAdapter(Skill.class, SkillAdapter.get())
				.registerTypeAdapter(Ability.class, AbilityAdapter.get())
				.registerTypeAdapter(MPlayer.class, MPlayerAdapter.get());
	}
	
	// -------------------------------------------- //
	// DEBUG
	// -------------------------------------------- //
	
	public void debug(int level, String format, Object... args)
	{
		if (MConf.get().debugLevel < level) return;
		
		String message = Txt.parse(format, args);
		get().log(Level.INFO, message);
	}
	
	// -------------------------------------------- //
	// INIT API
	// -------------------------------------------- //
	
	private void setApiFields()
	{
		// Mixins
		this.initMixins();
		
		// DLang
		DeriusAPI.setDLang(MLang.get());
		
		// The "core" field
		Class<DeriusAPI> apiClass = DeriusAPI.class;
		Field coreField = ReflectionUtil.getField(apiClass, DeriusConst.API_DERIUS_FIELD);
		if (coreField != null) // Avoid useless NPE
		{	
			ReflectionUtil.makeAccessible(coreField);
			ReflectionUtil.setField(coreField, null, this);
		}
	}
	
	private void initMixins()
	{
		DeriusAPI.setBlockMixin(BlockMixinDefault.get());
		DeriusAPI.setMaxLevelMixin(MaxLevelMixinDefault.get());
		DeriusAPI.setStaminaMixin(StaminaMixinDefault.get());
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
		return gson;
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
		
		if ( ! skill.isRegistered())
		{
			Skill old = SkillColl.get().get(skill.getId(), false);
			if (old != null)
			{
				skill.load(old);
				SkillColl.get().removeAtLocal(skill.getId());
			}
			SkillColl.get().attach(skill, skill.getId());
		}
		
		return;
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
		
		if (ability.isRegistered()) return;
		
		Ability old = AbilityColl.get().get(ability.getId(), false);
		if (old != null)
		{
			ability.load(old);
			AbilityColl.get().removeAtLocal(ability.getId());
		}
		
		AbilityColl.get().attach(ability, ability.getId());
		return;
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
