package dk.muj.derius.util;

import com.massivecraft.massivecore.util.PermUtil;

public class DeriusPermUtil
{

	// -------------------------------------------- //
	// MIXIN
	// -------------------------------------------- //
	
	//Will be changed in le future.
	public static String getDeniedMessage(String perm)
	{
		return PermUtil.getDeniedMessage(perm);
	}
}
