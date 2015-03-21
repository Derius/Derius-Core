package dk.muj.derius.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.ScheduledDeactivate;

public class EngineScheduledDeactivate extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineScheduledDeactivate i = new EngineScheduledDeactivate();
	public static EngineScheduledDeactivate get() { return i; }
	private EngineScheduledDeactivate() {}
	
	// -------------------------------------------- //
	// SCHEDULED DEATIVATES
	// -------------------------------------------- //
	
	protected Map<String, ScheduledDeactivate> playerIdToScheduledDeactivate = new ConcurrentHashMap<>();
	
	public boolean isScheduled(ScheduledDeactivate sd)
	{
		return this.playerIdToScheduledDeactivate.containsValue(sd);
	}
	
	public void schedule(ScheduledDeactivate sd)
	{
		ScheduledDeactivate old = this.playerIdToScheduledDeactivate.get(sd.getPlayerId());
		if (old != null) throw new IllegalStateException("Played already has a scheduled deactivate");
		
		this.playerIdToScheduledDeactivate.put(sd.getPlayerId(), sd);
		
		sd.setDueMillis(System.currentTimeMillis() + sd.getDelayMillis());
	}
	
	public boolean unschedule(ScheduledDeactivate sd)
	{
		ScheduledDeactivate old = this.playerIdToScheduledDeactivate.get(sd.getPlayerId());
		if (old == null) return false;
		if (old != sd) return false;
		
		return this.playerIdToScheduledDeactivate.remove(sd.getPlayerId()) != null;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusPlugin.get();
	}
	
	@Override
	public Long getPeriod()
	{
		return 1L;
	}
	
	@Override
	public void run()
	{
		long now = System.currentTimeMillis();
		for (ScheduledDeactivate sd : playerIdToScheduledDeactivate.values())
		{
			if (sd.isDue(now))
			{
				sd.run();
				this.unschedule(sd);
			}
		}
	}
	
}
