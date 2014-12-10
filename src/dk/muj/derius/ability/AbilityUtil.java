package dk.muj.derius.ability;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;

public class AbilityUtil
{		
	/**
	 * Returns a colorcode from the MConf for the Txt.parse method,
	 * based on the players ability to activate said ability or not
	 * @param {Ability} The ability we want to check for.
	 * @param {MPlayer} The MPlayer we want to check for.
	 * @return {String} The colorcode for the txt.parse method.
	 */
	public static String CanUseAbilitySkillColor (Ability a, MPlayer mplayer)
	{
		if (a.CanPlayerActivateAbility(mplayer))
		{
			return MConf.get().canPlayerUseAbilityColorYes;
		}
		else
		{
			return MConf.get().canPlayerUseAbilityColorNo;
		}
	}
}