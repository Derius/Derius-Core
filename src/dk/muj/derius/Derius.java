package dk.muj.derius;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.cmd.CmdDerius;
import dk.muj.derius.engine.AbilityEngine;
import dk.muj.derius.engine.BlockEngine;
import dk.muj.derius.engine.ChatEngine;
import dk.muj.derius.engine.MainEngine;
import dk.muj.derius.engine.SkillEngine;
import dk.muj.derius.entity.MChunkColl;
import dk.muj.derius.entity.MConfColl;
import dk.muj.derius.entity.MLangColl;
import dk.muj.derius.entity.MPlayerColl;


public class Derius extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
    
    private static Derius i;
	public static Derius get() { return i; }
	public Derius() { i = this; }
	
	// -------------------------------------------- //
	// COMMMANDS
	// -------------------------------------------- //
	
	// Commands 
	// "outer" means it is accessed directly as a command. ex. "/example"
	private CmdDerius outerCmdDerius;
	public CmdDerius getOuterCmdDerius() { return this.outerCmdDerius; }

	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		this.preEnable();
		// Initializing Database
		MConfColl.get().init();
		MPlayerColl.get().init();
		MChunkColl.get().init();
		MLangColl.get().init();
		
		//ENGINE
		AbilityEngine.get().activate();
		MainEngine.get().activate();
		ChatEngine.get().activate();
		SkillEngine.get().activate();
		BlockEngine.get().activate();
		
		// Command registration
		this.outerCmdDerius = new CmdDerius();
		this.outerCmdDerius.register(this); // "this" passes "Derius" to Massivecore, that can be usefull for other plugins
		
		this.postEnable();
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
	
}
