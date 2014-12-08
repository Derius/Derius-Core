package dk.muj.derius.skill;

public enum SpecialisationStatus
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	/**
	 * This is used if everyone is automatically specialised in said skill
	 */
	AUTO_ASSIGNED(),
	/**
	 * This is used if nobody can be specialised in said skill
	 */
	BLACK_LISTED(),
	/**
	 * This is used if that player had the skill specialised & still have it now
	 */
	HAD(),
	/**
	 * This is used if the player didn't have it before, but has it now
	 */
	HAS_NOW(),
	/**
	 * This is used if that player didn't have the skill specialised & still don't have it
	 */
	DIDNT_HAVE(),
	/**
	 * This is used if the player had it before, but don't have it now
	 */
	DONT_HAVE_NOW(),
	/**
	 * This is used if the player could not get the skill specialised.
	 * Because of the max skill specialised limit.
	 */
	TOO_MANY(),
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	SpecialisationStatus()
	{
		
	}
}