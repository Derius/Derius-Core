package dk.muj.derius.ability;

import org.bukkit.Material;

import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;

public class AbilityUtil
{
	/**
	 * Returns whether or not the passed material is an interactKey.
	 * @param {Ability} The Ability we want to check for.
	 * @param {Material} The Material we want to check for.
	 * @return {boolean} whether it is an interactKey or not.
	 */
	public static boolean isInteractKey(Ability ability, Material material)
	{
		for (Material interactKey : ability.getInteractKeys())
		{
			if (interactKey == material)
				return true;
		}
		return false;
	}
	
	
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
