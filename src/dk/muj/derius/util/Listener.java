package dk.muj.derius.util;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;

public interface Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	static final class Fields
	{
		// Interact tools
		private static Collection<Material> registeredInteractTools = new HashSet<>();
		protected static Collection<Material> getRegisteredInteractTools() { return registeredInteractTools; }
	}

	// -------------------------------------------- //
	// INTERACT TOOLS
	// -------------------------------------------- //
	
	
	/**
	 * Makes it possible for players to prepare these tools
	 * @param {Collection<Material>} materials to register
	 */
	public static void registerTools(Collection<Material> materials)
	{
		Validate.notNull(materials, "materials mustn't be null");
		
		for (Material material : materials)
		{
			Fields.getRegisteredInteractTools().add(material);
		}
	}
	
	/**
	 * Makes it possible for players to prepare the tool
	 * @param {Material} material to register
	 */
	public static void registerTool(Material material)
	{
		Validate.notNull(material, "material mustn't be null");
		
		Fields.getRegisteredInteractTools().add(material);
	}
	
	/**
	 * Tells whether or not a tool can be prepared
	 * @param {Material} material to check for
	 * @return {boolean} true if tool is registered
	 */
	public static boolean isRegistered(Material material)
	{
		return Fields.getRegisteredInteractTools().contains(material);
	}
	
	/**
	 * Makes it not possible for players to prepare the tool if already registered
	 * @param {Material} material to unregister
	 */
	public static void unregisterTool(Material material)
	{
		Fields.getRegisteredInteractTools().add(material);
	}
	
	/**
	 * Gets the tools which is registered as interact tools.
	 * Meaning pl,ayers can prepare them
	 * return {Collection<Material>} list of registered interact tools.
	 */
	public static Collection<Material> getRegisteredInteractTools()
	{
		return Fields.getRegisteredInteractTools();
	}

}
