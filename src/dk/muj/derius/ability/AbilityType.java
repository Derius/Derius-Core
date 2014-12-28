package dk.muj.derius.ability;

public enum AbilityType
{

	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	/**
	 * Active skills last over a duration of time
	 */
	ACTIVE(),
	/**
	 * Passive abilities are activated once & don't last over time
	 */
	PASSIVE(),
	
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	AbilityType()
	{
		
	}
}
