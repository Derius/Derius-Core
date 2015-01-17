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

import dk.muj.derius.Derius;
import dk.muj.derius.entity.MPlayer;
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
	// EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e)
	{	
		// Listeners
		Listener listener = Listener.getBlockBreakListener(e.getBlock().getType());
		if(listener != null)
		{
			listener.onBlockBreak(MPlayer.get(e.getPlayer()), e.getBlock().getState());
		}
		
		BlockUtil.setBlockPlacedByPlayer(e.getBlock(), false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e)
	{	
		BlockUtil.setBlockPlacedByPlayer(e.getBlock(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onFade(BlockFadeEvent e)
	{	
		// Unset Block
		BlockUtil.setBlockPlacedByPlayer(e.getBlock(), false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBurn(BlockBurnEvent e)
	{
		// Unset Block
		BlockUtil.setBlockPlacedByPlayer(e.getBlock(), false);
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
	public void onPistonExtend(BlockPistonExtendEvent e)
	{
		// Placed by player?
		if ( ! BlockUtil.isBlockPlacedByPlayer(e.getBlock().getRelative(e.getDirection()))) return;
		
		// Set according to new position
		BlockUtil.setBlockPlacedByPlayer(e.getBlock().getRelative(e.getDirection()), false);
		BlockUtil.setBlockPlacedByPlayer(e.getBlock().getRelative(e.getDirection(), 2), true);
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPistonRetract(BlockPistonRetractEvent e)
	{
		// Placed by player?
		if ( ! BlockUtil.isBlockPlacedByPlayer(e.getBlock().getRelative(e.getDirection(), 2)));
			
		// Set according to new position
		BlockUtil.setBlockPlacedByPlayer(e.getBlock().getRelative(e.getDirection(), 2), false);
		BlockUtil.setBlockPlacedByPlayer(e.getBlock().getRelative(e.getDirection()), true);
		
	}
	
}
