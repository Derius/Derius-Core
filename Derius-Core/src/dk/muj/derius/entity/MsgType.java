package dk.muj.derius.entity;

public enum MsgType
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	/**
	 * The messages gets sent through chat
	 */
	CHAT(),
	/**
	 * The messages get shown through a title (on screen)
	 */
	TITLE(),
	/**
	 * The messages get shown through a Scoreboard
	 */
	SCOREBOARD(),	
	
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	MsgType()
	{
		
	}
}
