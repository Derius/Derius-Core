package dk.muj.derius.engine;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.Perm;
import dk.muj.derius.entity.MChunk;
import dk.muj.derius.entity.MChunkColl;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.util.BlockUtil;
import dk.muj.derius.util.Listener;

public class BlockEngine extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static BlockEngine i = new BlockEngine();
	public static BlockEngine get() { return i; }
	public BlockEngine() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return Derius.get();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		for (MPlayer mplayer : MPlayerColl.get().getAll(p -> Perm.NOTIFY_CHUNK.has(p.getSender(), false)))
		{
			mplayer.msg(MLang.get().prefix + " <i>Chunk cleanup is about to happen lag might occur");
		}
		cleanWorlds();
		
		return;
	}
	
	@Override
	public Long getPeriod()
	{
		//Every 24 hours we perform a cleanup
		//It is also done on startup
		//				1Sec	1min	1hour	24hours
		return (long) (	20 *	60 *	60 *	24);
	}
	
	// -------------------------------------------- //
	// WORLDS
	// -------------------------------------------- //
	
	public void cleanWorlds()
	{
		for (MChunk mchunk : MChunkColl.get().getAll())
		{
			if (mchunk.getLastActive() < System.currentTimeMillis() - Txt.millisPerDay * 20)
			{
				mchunk.clear();
			}
		}
		
		return;
	}
	
	
	// -------------------------------------------- //
	// BLOCK EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{	
		// Listeners
		Listener listener = Listener.getBlockBreakListener(event.getBlock().getType());
		if (listener != null)
		{
			listener.onBlockBreak(MPlayer.get(event.getPlayer()), event.getBlock().getState());
		}
		
		BlockUtil.setBlockPlacedByPlayer(event.getBlock(), false);
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{	
		BlockUtil.setBlockPlacedByPlayer(event.getBlock(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onFade(BlockFadeEvent event)
	{	
		// Unset Block
		BlockUtil.setBlockPlacedByPlayer(event.getBlock(), false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBurn(BlockBurnEvent event)
	{
		// Unset Block
		BlockUtil.setBlockPlacedByPlayer(event.getBlock(), false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExplode(EntityExplodeEvent event)
	{
		for (Block block : event.blockList())
		{
			BlockUtil.setBlockPlacedByPlayer(block, false);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPistonExtend(BlockPistonExtendEvent event)
	{
		// Placed by player?
		if ( ! BlockUtil.isBlockPlacedByPlayer(event.getBlock().getRelative(event.getDirection()))) return;
		
		// Set according to new position
		BlockUtil.setBlockPlacedByPlayer(event.getBlock().getRelative(event.getDirection()), false);
		BlockUtil.setBlockPlacedByPlayer(event.getBlock().getRelative(event.getDirection(), 2), true);
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPistonRetract(BlockPistonRetractEvent event)
	{
		// Placed by player?
		if ( ! BlockUtil.isBlockPlacedByPlayer(event.getBlock().getRelative(event.getDirection(), 2)));
			
		// Set according to new position
		BlockUtil.setBlockPlacedByPlayer(event.getBlock().getRelative(event.getDirection(), 2), false);
		BlockUtil.setBlockPlacedByPlayer(event.getBlock().getRelative(event.getDirection()), true);
	}
	
}
