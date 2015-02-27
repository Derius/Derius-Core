package dk.muj.derius.entity.mplayer;

import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.util.IdUtil;

import dk.muj.derius.DeriusConst;
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
		super(DeriusConst.COLLECTION_MPLAYER, MPlayer.class, MStore.getDb(), DeriusCore.get());
	}
	
	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public MPlayer createNewInstance()
	{
		return new MPlayer();
	}
	
	@Override
	public String fixId(Object oid)
	{
		return IdUtil.getId(oid);
	}
}
