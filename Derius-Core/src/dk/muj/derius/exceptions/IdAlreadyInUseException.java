package dk.muj.derius.exceptions;

public class IdAlreadyInUseException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4878943872189602869L;

	  public IdAlreadyInUseException() { super(); }
	  public IdAlreadyInUseException(String message) { super(message); }
	  public IdAlreadyInUseException(String message, Throwable cause) { super(message, cause); }
	  public IdAlreadyInUseException(Throwable cause) { super(cause); }
}
