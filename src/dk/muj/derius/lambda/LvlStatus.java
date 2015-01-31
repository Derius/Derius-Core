package dk.muj.derius.lambda;

import java.util.Optional;

import javax.annotation.concurrent.Immutable;


/**
 * This class is used for passing data about a players progress
 * in a certain skill around between methods and classes.
 * 
 * It has 3 variables.
 * The players level
 * The players leftover exp (after calculating level)
 * The amount of exp required to reach next level (ignoring leftover exp)
 * 
 * This is an interface so other developers can do fancy stuff if they wish
 * it is however designed to be immutable.
 * 
 * To instantiate the default implementation use LvlStatusDefault.java
 * 
 * The getters & setters should be obvious.
 */
@Immutable
public interface LvlStatus
{
	// -------------------------------------------- //
	// Getters & Setters
	// -------------------------------------------- //
	
	public int getLvl ();
	public LvlStatus setLvl (int level);
	

	public Optional<Integer> getExp ();
	public LvlStatus setExp (Optional<Integer>  exp);
	

	public Optional<Integer>  getExpToNextLvl ();
	public LvlStatus setExpToNextLvl (Optional<Integer>  expToNextLvl);
}
