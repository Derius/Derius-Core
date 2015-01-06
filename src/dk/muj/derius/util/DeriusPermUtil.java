package dk.muj.derius.util;

import com.massivecraft.massivecore.util.PermUtil;

import dk.muj.derius.Perm;

public class DeriusPermUtil
{

	// -------------------------------------------- //
	// MIXIN
	// -------------------------------------------- //
	
	//Will be changed in le future.
	public static String getDeniedMessage(String perm)
	{
		if (perm.startsWith(Perm.ABILITY_USE.node))
		{	//       6      14  18
			// derius.ability.use.
			int index = perm.lastIndexOf(".");
			String id = perm.substring(index);
			String.
		}
		return PermUtil.getDeniedMessage(perm);
	}
}
