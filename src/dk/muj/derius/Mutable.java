package dk.muj.derius;

public class Mutable<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private T content;
	public T get() { return content; }
	public void set(T content) { this.content = content; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Mutable(T startContent)
	{
		this.content = startContent;
	}
	
}
