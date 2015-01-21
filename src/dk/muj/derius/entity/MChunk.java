package dk.muj.derius.entity;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;

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
		this.lastActive = that.lastActive;
		
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
	
	private long lastActive = System.currentTimeMillis();
	public long getLastActive() { return lastActive; }
	public void setLastActive(long lastActive) { this.lastActive = lastActive; }
	
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
	
	public boolean addBlock(final PS ps, Chunk c)
	{
		PS block = ps.getBlockCoords(true);
		PS chunk = ps.getChunkCoords(true).withWorld(ps.getWorld());
		if ( ! chunk.toString(PSFormatFormal.get()).equals(this.getId())) return false;
		this.blocks.add(block);
		
		this.lastActive = System.currentTimeMillis();
		
		return true;
	}
	
	public boolean removeBlock(final PS ps)
	{
		PS block = ps.getBlockCoords(true);
		PS chunk = ps.getChunkCoords(true);
		if ( ! chunk.toString(PSFormatFormal.get()).equals(this.getId())) return false;
		
		this.blocks.remove(block);
		
		this.lastActive = System.currentTimeMillis();
		
		return true;
	}
	
	public Set<PS> getBlocks()
	{
		return this.blocks;
	}

	public void clear()
	{
		this.blocks.clear();	
	}
	
}
