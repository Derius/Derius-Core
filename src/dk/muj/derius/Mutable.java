package dk.muj.derius;

public class Mutable<T>
{
	private T content;

	public T get() { return content; }
	public void set(T content) { this.content = content; }
	
	public Mutable(T startContent)
	{
		this.content = startContent;
	}
	
}
