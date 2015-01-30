package dk.muj.derius.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MPlayer;
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
		return Derius.get();
	}
	
	// -------------------------------------------- //
	// EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event)
	{
		MPlayer mplayer = MPlayer.get(event.getPlayer());
		
		if ( ! mplayer.isChatListeningOk()) return;
		
		Ability ability = mplayer.getAbilityBySubString(event.getMessage().toLowerCase());
		if (ability == null) return;
		
		AbilityUtil.activateAbility(mplayer, ability, null, true);
	}
	
}
