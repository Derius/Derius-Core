package dk.muj.derius.mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

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
	
	@Override
	public Set<Material> getBlocksTypesToListenFor()
	{
		return Collections.emptySet();
	}

	@Override
	public void setBlocksTypesToListenFor(Collection<Material> blocks)
	{
		// None per default
	}

	@Override
	public void addBlockTypesToListenFor(Material... blocks)
	{
		// None per default
	}

	@Override
	public void addBlockTypesToListenFor(Collection<Material> blocks)
	{
		// None per default
	}

	@Override
	public boolean isListendFor(Material material)
	{
		return false;
	}

}
