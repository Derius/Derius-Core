package dk.muj.derius.mixin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Material;

import com.massivecraft.massivecore.ps.PS;

public class BlockMixinDefault implements BlockMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static BlockMixinDefault i = new BlockMixinDefault();
	public static BlockMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// Placed
	@Override
	public boolean isBlockPlacedByPlayer(PS ps)
	{
		return false;
	}

	// Listening
	
	private Set<Material> blocks = new CopyOnWriteArraySet<Material>();
	public Set<Material> getBlocksTypesToListenFor() { return this.blocks; }
	public void setBlocksTypesToListenFor(Collection<Material> blocks) { this.blocks.clear(); this.blocks.addAll(blocks); } 
	public void addBlockTypesToListenFor(Material... blocks) { this.blocks.addAll(Arrays.asList(blocks)); }
	public void addBlockTypesToListenFor(Collection<Material> blocks) { this.blocks.addAll(blocks); }

}
