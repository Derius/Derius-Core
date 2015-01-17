package dk.muj.derius.util;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

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
	public static void addBlockTypesToListenFor(Material block) { blockTypesToListenFor.add(block); }
	
	// -------------------------------------------- //
	// MANGE BLOCKS
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
			chunk.addBlock(ps);
		}
		else
		{
			Bukkit.broadcastMessage("Util 3");
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
	
}
