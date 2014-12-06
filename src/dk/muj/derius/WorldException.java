package dk.muj.derius;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

public class WorldException
{

	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	public boolean standard = true;
	
	
	// -------------------------------------------- //
	// EXCEPTIONS
	// -------------------------------------------- //
	
	public List<String> exceptions = new ArrayList<String>();
	
	public boolean EnabledInWorld(String worldName)
	{
		if(exceptions.contains(worldName))
			return !standard;
		return standard;
	}
	
	public boolean EnabledInWorld(World world)
	{
		return EnabledInWorld(world.getName());
	}
}
