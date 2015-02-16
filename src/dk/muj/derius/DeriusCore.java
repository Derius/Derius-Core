package dk.muj.derius;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Derius;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.cmd.CmdDerius;
import dk.muj.derius.engine.AbilityEngine;
import dk.muj.derius.engine.ChatEngine;
import dk.muj.derius.engine.MainEngine;
import dk.muj.derius.engine.SkillEngine;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MConfColl;
import dk.muj.derius.entity.MLangColl;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.entity.ability.AbilityAdapter;
import dk.muj.derius.entity.ability.AbilityColl;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.entity.skill.DeriusSkill;
import dk.muj.derius.entity.skill.SkillAdapter;
import dk.muj.derius.entity.skill.SkillColl;
import dk.muj.derius.mixin.BlockMixin;
import dk.muj.derius.mixin.BlockMixinDefault;
import dk.muj.derius.mixin.MaxLevelMixin;
import dk.muj.derius.mixin.MaxLevelMixinDefault;
import dk.muj.derius.mixin.SpSlotMixin;
import dk.muj.derius.mixin.SpSlotMixinDefault;


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
	
	// Mixins
	private static SpSlotMixin spSlotMixin = SpSlotMixinDefault.get();
	public static SpSlotMixin getSpSlotMixin () { return spSlotMixin; }
	public static void setSpSlotMixin(SpSlotMixin val) { spSlotMixin = val; }
	
	private static MaxLevelMixin maxLevelMixin = MaxLevelMixinDefault.get();
	public static MaxLevelMixin getMaxLevelMixin() { return maxLevelMixin; }
	public static void setMaxLevelMixin (MaxLevelMixin val) { maxLevelMixin = val; }
	
	private static BlockMixin blockMixin = BlockMixinDefault.get();
	public static BlockMixin getBlockMixin() { return blockMixin; }
	public static void setBlockMixin (BlockMixin val) { blockMixin = val; }
	
	// Engines
	private List<Engine> engines = MUtil.list(
		AbilityEngine	.get(),
		MainEngine		.get(),
		ChatEngine		.get(),
		SkillEngine		.get());

	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
		
		// Initializing Databases
		MConfColl	.get().init();
		MLangColl	.get().init();
		MPlayerColl	.get().init();	
		SkillColl	.get().init();
		AbilityColl	.get().init();
		
		// Engine activation
		for (Engine engine : engines)
		{
			engine.activate();
		}
		
		// Command registration
		this.outerCmdDerius = new CmdDerius() { @Override public List<String> getAliases() { return MConf.get().outerAliasesDerius; } };
		this.outerCmdDerius.register(this);
		
		MPlayerColl.get().get(IdUtil.getConsole(), true);
		
		this.setApiField();
		
		this.postEnable();
	}
	

	@Override
	public void onDisable()
	{
		super.onDisable();
		
		// Engine deactivation
		for (Engine engine : engines)
		{
			engine.deactivate();
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE: MASSIVEPLUGIN
	// -------------------------------------------- //
	
	@Override
	public GsonBuilder getGsonBuilder()
	{
		return super.getGsonBuilder()
				.registerTypeAdapter(DeriusSkill.class, SkillAdapter.get())
				.registerTypeAdapter(DeriusAbility.class, AbilityAdapter.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DERIUS
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
	// INIT API
	// -------------------------------------------- //
	
	private void setApiField()
	{
		Class<DeriusAPI> apiClass = DeriusAPI.class;
		Field coreField = ReflectionUtil.getField(apiClass, "core");
		ReflectionUtil.makeAccessible(coreField);
		ReflectionUtil.setField(coreField, null, this);
	}
}
