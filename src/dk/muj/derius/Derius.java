package dk.muj.derius;

import com.massivecraft.massivecore.MassivePlugin;

public class Derius extends MassivePlugin
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
    
    private static Derius i;
	public static Derius get() { return i; }
	public Derius() { i = this; }
	
	@Override
	public void onEnable()
	{
		this.preEnable();
		
		
		this.postEnable();
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
}
