package dk.muj.derius.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatFormal;

import dk.muj.derius.entity.MChunk;
import dk.muj.derius.entity.MChunkColl;

public final class BlockUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN
	// -------------------------------------------- //
	
	private BlockUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static Set<Material> blockTypesToListenFor = new CopyOnWriteArraySet<Material>();
	public static Set<Material> getBlocksTypesToListenFor() { return blockTypesToListenFor; }
	public static void setBlocksTypesToListenFor(Set<Material> blocks) { blockTypesToListenFor = blocks; } 
	public static void addBlockTypesToListenFor(Material... blocks) { blockTypesToListenFor.addAll(Arrays.asList(blocks)); }
	public static void addBlockTypesToListenFor(Collection<Material> blocks) { blockTypesToListenFor.addAll(blocks); }
	
	// -------------------------------------------- //
	// MANGE PLACED BLOCKS
	// -------------------------------------------- //
	
	public static void setBlockPlacedByPlayer(Block block, boolean isPlaced)
	{	
		PS ps = PS.valueOf(block);
		ps = ps.with(ps.getBlock(true).with(ps.getChunk(true)));
		MChunk chunk = MChunkColl.get().get(ps.getChunk(true).toString(PSFormatFormal.get()), true);
		
		if (isPlaced)
		{
			// We wil always like to remove, but not always add
			if ( ! getBlocksTypesToListenFor().contains(block.getType())) return;
			chunk.addBlock(ps, ps.asBukkitChunk());
		}
		else
		{
			chunk.removeBlock(ps);
		}
	}
	
	public static boolean isBlockPlacedByPlayer(Block block)
	{
		PS ps = PS.valueOf(block);
		ps = ps.with(ps.getBlock(true).with(ps.getChunk(true)));
		MChunk chunk = MChunkColl.get().get(ps.getChunk(true).toString(PSFormatFormal.get()), true);
		
		return chunk.getBlocks().contains(ps.getBlock());
	}
	
	// -------------------------------------------- //
	// SURROUNDING BLOCKS
	// -------------------------------------------- //
	
	/**
	 * Gets all blocks relative to specified block. With all BlockFaces
	 * @param {Block} Block to get relatives from
	 * @return {Set<Block>} the blocks relative to the specified block
	 */
	public static Set<Block> getSurroundingBlocks(final Block source)
	{
		Set<Block> ret = new HashSet<Block>();
		
		for (BlockFace face : BlockFace.values())
		{
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
	/**
	 * Gets all blocks relative to specified block. With specified BlockFaces
	 * @param {Block} Block to get relatives from
	 * @param {Collection<BlockFace>} BlockFaces to get from
	 * @return {Set<Block>} the blocks relative to the specified block.
	 */
	public static Set<Block> getSurroundingBlocksWith(final Block source, Collection<BlockFace> faces)
	{
		Set<Block> ret = new HashSet<Block>();
		
		for (BlockFace face : BlockFace.values())
		{
			if ( ! faces.contains(face)) continue;
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
	/**
	 * Gets all blocks relative to specified block. Without specified BlockFaces
	 * @param {Block} Block to get relatives from
	 * @param {Collection<BlockFace>} BlockFaces not to get from
	 * @return {Set<Block>} the blocks relative to the specified block.
	 */
	public static Set<Block> getSurroundingBlocksWithout(final Block source, Collection<BlockFace> faces)
	{
		Set<Block> ret = new HashSet<Block>();
		
		for (BlockFace face : BlockFace.values())
		{
			if (faces.contains(face)) continue;
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
}
