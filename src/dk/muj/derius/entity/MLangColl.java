package dk.muj.derius.entity;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.DeriusConst;
import dk.muj.derius.DeriusCore;

public final class MLangColl extends Coll<MLang>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MLangColl i = new MLangColl();
	public static MLangColl get() { return i; }
	private MLangColl()
	{
		super(DeriusConst.COLLECTION_MLANG, MLang.class, MStore.getDb(), DeriusCore.get());
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
	// OVERRIDE
	// -------------------------------------------- //
	
	// This initializes the saving task of the class.
	@Override
	public void init()
	{
		super.init();
		MLang.i = this.get("MessageSettings", true);
	}

}
