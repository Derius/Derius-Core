package dk.muj.derius.mixin;

import dk.muj.derius.api.DPlayer;

public class SpSlotMixinDefault implements SpSlotMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SpSlotMixinDefault i = new SpSlotMixinDefault();
	public static SpSlotMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getMaxSlots(DPlayer mplayer)
	{
		final String perm = "derius.spslots.";
		
		for (int i = 50; i > 0; i++)
		{
			if ( ! mplayer.getSender().hasPermission(perm + i)) continue;
			return i;
		}
		
		return 0;
	}

}
