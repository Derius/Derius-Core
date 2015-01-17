package dk.muj.derius.entity;

import java.util.HashSet;
import java.util.Set;

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
		PS block = ps.getBlockCoords(true);
		PS chunk = ps.getChunkCoords(true).withWorld(ps.getWorld());
		if ( ! chunk.toString(PSFormatFormal.get()).equals(this.getId())) return false;
		this.blocks.add(block);
		
		return true;
	}
	
	public boolean removeBlock(final PS ps)
	{
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
