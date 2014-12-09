package dk.muj.derius;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.cmd.CmdDerius;
import dk.muj.derius.entity.MConfColl;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.integration.FactionIntegration;
import dk.muj.derius.listeners.PlayerListener;
import dk.muj.derius.listeners.SkillListener;


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
	// LISTENERS
	// -------------------------------------------- //
	
	private SkillListener skillListener;
	public SkillListener getSkillListener () { return skillListener; }
	
	private PlayerListener playerListener;
	public PlayerListener getPlayerListener () { return playerListener; }
	
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
		
		//Listeners
		super.getServer().getPluginManager().registerEvents(this, this);
		this.skillListener = new SkillListener(this);
		this.playerListener = new PlayerListener(this);
		
		FactionIntegration.EstablishIntegration();
		
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e)
	{
		MPlayerColl.get().get(e.getPlayer().getUniqueId().toString(), true);
	}
}
