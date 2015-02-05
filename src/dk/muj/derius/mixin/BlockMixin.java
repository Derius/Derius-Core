package dk.muj.derius.mixin;

import java.util.Collection;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.massivecraft.massivecore.ps.PS;

public interface BlockMixin
{
	// Placed by player
	
	// Abstract
	public boolean isBlockPlacedByPlayer(PS ps);
	
	// Default
	public default boolean isBlockPlacedByPlayer(Block block)
	{
		return this.isBlockPlacedByPlayer(PS.valueOf(block));
	}
	
	public default boolean isBlockPlacedByPlayer(Location loc)
	{
		return this.isBlockPlacedByPlayer(PS.valueOf(loc));
	}
	
	
	// Listening
	
	public Set<Material> getBlocksTypesToListenFor();
	public void setBlocksTypesToListenFor(Collection<Material> blocks);
	public void addBlockTypesToListenFor(Material... blocks);
	public void addBlockTypesToListenFor(Collection<Material> blocks);
	
}
