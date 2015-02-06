package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.util.AbilityUtil;

public class ChatEngine extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ChatEngine i = new ChatEngine();
	public static ChatEngine get() { return i; }
	public ChatEngine() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusCore.get();
	}
	
	// -------------------------------------------- //
	// EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event)
	{
		DPlayer mplayer = DeriusAPI.getDPlayer(event.getPlayer());
		
		if ( ! mplayer.isChatListeningOk()) return;
		
		Ability ability = mplayer.getAbilityBySubString(event.getMessage().toLowerCase());
		if (ability == null) return;
		
		AbilityUtil.activateAbility(mplayer, ability, null, true);
		
		return;
	}
	
}
