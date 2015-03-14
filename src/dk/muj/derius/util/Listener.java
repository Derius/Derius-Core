package dk.muj.derius.util;

import java.util.Collection;

import org.bukkit.Material;

import dk.muj.derius.api.DeriusAPI;

@Deprecated
public interface Listener
{
	@Deprecated
	public static void registerTools(Collection<Material> materials)
	{
		DeriusAPI.registerPreparableTools(materials);
	}
	
	@Deprecated
	public static void registerTool(Material material)
	{
		DeriusAPI.registerPreparableTool(material);
	}
	
	@Deprecated
	public static boolean isRegistered(Material material)
	{
		return DeriusAPI.isRegisteredAsPreparable(material);
	}
	
	@Deprecated
	public static void unregisterTool(Material material)
	{
		DeriusAPI.unregisterPreparableTool(material);
	}
	
	@Deprecated
	public static Collection<Material> getRegisteredInteractTools()
	{
		return DeriusAPI.getPreparableTools();
	}

}
