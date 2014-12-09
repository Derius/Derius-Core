package dk.muj.derius.ability;

import org.bukkit.Material;

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
}
