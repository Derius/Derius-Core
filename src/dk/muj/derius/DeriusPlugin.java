package dk.muj.derius;

import java.util.List;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

import dk.muj.derius.adapter.AbilityAdapter;
import dk.muj.derius.adapter.SkillAdapter;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.ability.Ability;
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

public final class DeriusPlugin extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusPlugin i;
	public static DeriusPlugin get() { return i; }
	public DeriusPlugin() { i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands 
	// "outer" means it is accessed directly as a command. ex. "/example"
	private CmdDerius outerCmdDerius;
	public CmdDerius getOuterCmdDerius() { return this.outerCmdDerius; }
	
	// Engines
	private List<Engine> engines = MUtil.list
		(
			EngineActivate.get(),
			EngineMain.get(),
			EngineScheduledDeactivate.get(),
			EngineMsg.get()
		);
	
	// Colls
	private List<Coll<?>> colls = MUtil.list
		(
			MConfColl.get(),
			MLangColl.get(),
			MPlayerColl.get(),
			SkillColl.get(),
			AbilityColl.get()
		);
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
		
		// We must first init the collections...
		colls.forEach(Coll::init);
		
		// ... because some of them are used when instantiating the api fields.
		this.setApiFields();
		
		// Engine activation
		engines.forEach(Engine::activate);
		TaskPlayerStaminaUpdate.get().activate();
		ItemManager.setup();
		
		// Command registration
		this.registerCommand();
		
		// Ensure player fields are instantiated.
		this.instantiatePlayerFields();
		
		this.postEnable();
	}
	
	private void registerCommand()
	{
		this.outerCmdDerius = new CmdDerius() { public List<String> getAliases() { return MConf.get().outerAliasesDerius; } };
		this.outerCmdDerius.register(this);
	}
	
	private void instantiatePlayerFields()
	{
		EngineMain.instantiatePlayerFields(IdUtil.CONSOLE_ID);
		MUtil.getOnlinePlayers().forEach(EngineMain::instantiatePlayerFields);
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
	// INIT API
	// -------------------------------------------- //
	
	private void setApiFields()
	{
		// Mixins
		this.injectMixin();
		
		// DLang
		DeriusAPI.setDLang(MLang.get());
		
		// The "core" field
		DeriusCore.inject();
	}
	
	private void injectMixin()
	{
		DeriusAPI.setBlockMixin(BlockMixinDefault.get());
		DeriusAPI.setMaxLevelMixin(MaxLevelMixinDefault.get());
		DeriusAPI.setStaminaMixin(StaminaMixinDefault.get());
	}

}
