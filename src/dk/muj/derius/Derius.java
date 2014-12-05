package dk.muj.derius;

import com.massivecraft.massivecore.MassivePlugin;

public class Derius extends MassivePlugin
{

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
