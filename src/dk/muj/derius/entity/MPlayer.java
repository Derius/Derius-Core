package dk.muj.derius.entity;

import com.massivecraft.massivecore.store.SenderEntity;

public class MPlayer extends SenderEntity<MPlayer>
{

	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static MPlayer get(Object oid)
	{
		return MPlayerColl.get().get(oid, false);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	 
}