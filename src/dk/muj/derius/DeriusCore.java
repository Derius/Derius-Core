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

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Derius;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Skill;
import dk.muj.derius.cmd.CmdDerius;
import dk.muj.derius.engine.MainEngine;
import dk.muj.derius.engine.MsgEngine;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MConfColl;
import dk.muj.derius.entity.MLangColl;
import dk.muj.derius.entity.ability.AbilityAdapter;
import dk.muj.derius.entity.ability.AbilityColl;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.entity.mplayer.MPlayer;
import dk.muj.derius.entity.mplayer.MPlayerAdapter;
import dk.muj.derius.entity.mplayer.MPlayerColl;
import dk.muj.derius.entity.skill.DeriusSkill;
import dk.muj.derius.entity.skill.SkillAdapter;
import dk.muj.derius.entity.skill.SkillColl;
import dk.muj.derius.mixin.BlockMixinDefault;
import dk.muj.derius.mixin.MaxLevelMixinDefault;
import dk.muj.derius.mixin.StaminaMixinDefault;
import dk.muj.derius.scoreboard.TaskPlayerStaminaUpdate;


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
		MainEngine.get(),
		MsgEngine.get());
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
		
		MConfColl.get().init();
		MLangColl.get().init();
		MPlayerColl.get().init();
		SkillColl.get().init();
		AbilityColl.get().init();
		
		// Engine activation
		for (Engine engine : engines)
		{
			engine.activate();
		}
		
		// Command registration
		this.outerCmdDerius = new CmdDerius() { @Override public List<String> getAliases() { return MConf.get().outerAliasesDerius; } };
		this.outerCmdDerius.register(this);
		
		MPlayerColl.get().get(IdUtil.getConsole(), true);
		
		// ModulaRepeatTask
		TaskPlayerStaminaUpdate.get().activate();
		
		this.setApiFields();
		
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
				.registerTypeAdapter(DeriusAbility.class, AbilityAdapter.get())
				.registerTypeAdapter(MPlayer.class, MPlayerAdapter.get());
	}
	
	// -------------------------------------------- //
	// INIT API
	// -------------------------------------------- //
	
	private void setApiFields()
	{
		// Mixins
		this.initMixins();
		
		// The "core" field
		Class<DeriusAPI> apiClass = DeriusAPI.class;
		Field coreField = ReflectionUtil.getField(apiClass, Const.API_DERIUS_FIELD);
		if (coreField == null) return; // Avoid useless NPE
		ReflectionUtil.makeAccessible(coreField);
		ReflectionUtil.setField(coreField, null, this);
	}
	
	private void initMixins()
	{
		DeriusAPI.setBlockMixin(BlockMixinDefault.get());
		DeriusAPI.setMaxLevelMixin(MaxLevelMixinDefault.get());
		DeriusAPI.setStaminaMixin(StaminaMixinDefault.get());
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
	public Collection<? extends Ability> getAllAbilities()
	{
		return AbilityColl.getAllAbilities();
	}
	
	@Override
	public Ability getAbility(String id)
	{
		return AbilityColl.get().get(id);
	}
	
}
