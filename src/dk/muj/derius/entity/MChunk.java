package dk.muj.derius.entity;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatFormal;
import com.massivecraft.massivecore.store.Entity;

public class MChunk extends Entity<MChunk>
{
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public MChunk load(MChunk that)
	{
		this.blocks = that.blocks;
		
		return this;
	}
	
	@Override
	public boolean isDefault()
	{
		if (this.blocks == null) return true;
		return false;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Set<PS> blocks;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MChunk()
	{
		blocks = new HashSet<PS>();
	}
	
	// -------------------------------------------- //
	// BLOCK GETTERS & SETTERS
	// -------------------------------------------- //
	
	public boolean addBlock(final PS ps)
	{
		Bukkit.broadcastMessage("Entity 1");
		PS block = ps.getBlockCoords(true);
		PS chunk = ps.getChunkCoords(true).withWorld(ps.getWorld());
		Bukkit.broadcastMessage(chunk.toString(PSFormatFormal.get()));
		Bukkit.broadcastMessage(this.getId());
		if ( ! chunk.toString(PSFormatFormal.get()).equals(this.getId())) return false;
		Bukkit.broadcastMessage("Entity 1.1");
		this.blocks.add(block);
		
		return true;
	}
	
	public boolean removeBlock(final PS ps)
	{
		Bukkit.broadcastMessage("Entity 2");
		PS block = ps.getBlockCoords(true);
		PS chunk = ps.getChunkCoords(true);
		if ( ! chunk.toString(PSFormatFormal.get()).equals(this.getId())) return false;
		
		this.blocks.remove(block);
		
		return true;
	}
	
	public Set<PS> getBlocks()
	{
		return this.blocks;
	}
	
}
