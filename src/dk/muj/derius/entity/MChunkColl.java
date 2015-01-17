package dk.muj.derius.entity;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.Const;
import dk.muj.derius.Derius;

public class MChunkColl extends Coll<MChunk>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MChunkColl i = new MChunkColl();
	public static MChunkColl get() { return i; }
	private MChunkColl()
	{
		super(Const.COLLECTION_CHUNKS, MChunk.class, MStore.getDb(), Derius.get());
	}

}
