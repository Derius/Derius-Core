package dk.muj.derius.entity;

import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.store.SenderColl;

import dk.muj.derius.Const;
import dk.muj.derius.DeriusCore;

public class MPlayerColl extends SenderColl<MPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MPlayerColl i = new MPlayerColl();
	public static MPlayerColl get() { return i; }
	private MPlayerColl()
	{
		super(Const.COLLECTION_MPLAYER, MPlayer.class, MStore.getDb(), DeriusCore.get());
	}

	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public MPlayer createNewInstance()
	{
		return new MPlayer();
	}
}
