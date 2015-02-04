package dk.muj.derius;

import java.util.List;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.cmd.CmdDerius;
import dk.muj.derius.engine.AbilityEngine;
import dk.muj.derius.engine.ChatEngine;
import dk.muj.derius.engine.MainEngine;
import dk.muj.derius.engine.SkillEngine;
import dk.muj.derius.entity.AbilityColl;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MConfColl;
import dk.muj.derius.entity.MLangColl;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.mixin.BlockMixin;
import dk.muj.derius.mixin.BlockMixinDefault;
import dk.muj.derius.mixin.MaxLevelMixin;
import dk.muj.derius.mixin.MaxLevelMixinDefault;
import dk.muj.derius.mixin.SpSlotMixin;
import dk.muj.derius.mixin.SpSlotMixinDefault;


public class Derius extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
    
    private static Derius i;
	public static Derius get() { return i; }
	public Derius() { i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands 
	// "outer" means it is accessed directly as a command. ex. "/example"
	private CmdDerius outerCmdDerius;
	public CmdDerius getOuterCmdDerius() { return this.outerCmdDerius; }
	
	// Mixins
	private SpSlotMixin spSlotMixin = SpSlotMixinDefault.get();
	public SpSlotMixin getSpSlotMixin () { return this.spSlotMixin; }
	public void setSpSlotMixin(SpSlotMixin val) { this.spSlotMixin = val; }
	
	private MaxLevelMixin maxLevelMixin = MaxLevelMixinDefault.get();
	public MaxLevelMixin getMaxLevelMixin() { return this.maxLevelMixin; }
	public void setMaxLevelMixin (MaxLevelMixin val) { this.maxLevelMixin = val; }
	
	private BlockMixin blockMixin = BlockMixinDefault.get();
	public BlockMixin getBlockMixin() { return this.blockMixin; }
	public void setBlockMixin (BlockMixin val) { this.blockMixin = val; }
	
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
	
}
