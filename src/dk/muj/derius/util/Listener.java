package dk.muj.derius.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

import dk.muj.derius.entity.MPlayer;

public interface Listener
{
	
	static class ListenerFields
	{
		// -------------------------------------------- //
		// FIELDS
		// -------------------------------------------- //
	
		private static Collection<Material> registeredInteractTools = new HashSet<>();
		private static Map<Material, Listener> blockBreakKeys = new EnumMap<>(Material.class);
		
		static Map<Material, Listener> getBlockBreakKeys() { return blockBreakKeys; }
		static Collection<Material> getRegisteredInteractTools() { return registeredInteractTools; }
	
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	/**
	 * Registers the listener
	 * currently unused
	 */
	public default void registerListener()
	{

	}
	
	// -------------------------------------------- //
	// BLOCK BREAK KEYS
	// -------------------------------------------- //

	/**
	 * Registers a block type to listen for (when broken)
	 * @param {Material} block type to listen for
	 */
	public default void registerBlockBreakKey(Material... materials)
	{
		for(Material material: materials)
			ListenerFields.getBlockBreakKeys().put(material,this);
	}
	
	/**
	 * Registers a collection of block types to listen for (when broken)
	 * @param {Collection<Material>} collection of block types to listen for
	 */
	public default void registerBlockBreakKeys(Collection<Material> materials)
	{
		for(Material material: materials)
			this.registerBlockBreakKey(material);
	}
	
	/**
	 * Gets the listener that are called when
	 * a block with this material is broken
	 * @param {Material} block type to get
	 * @return {Listener} The listener listening to this block
	 */
	public static Listener getListener(Material material)
	{
		return ListenerFields.getBlockBreakKeys().get(material);
	}
	
	// -------------------------------------------- //
	// INTERACT
	// -------------------------------------------- //
	
	
	/**
	 * Makes it possible for players to prepare these tools
	 * @param {Collection<Material>} materials to register
	 */
	public static void registerTools(Collection<Material> materials)
	{
		for(Material material : materials)
			ListenerFields.getRegisteredInteractTools().add(material);
	}
	
	/**
	 * Makes it possible for players to prepare the tool
	 * @param {Material} material to register
	 */
	public static void registerTool(Material material)
	{
		ListenerFields.getRegisteredInteractTools().add(material);
	}
	
	/**
	 * Tells whether or not a tool can be prepared
	 * @param {Material} material to check for
	 * @return {boolean} true if tool is registered
	 */
	public static boolean isRegistered(Material material)
	{
		return ListenerFields.getRegisteredInteractTools().contains(material);
	}
	
	/**
	 * Makes it not possible for players to prepare the tool if already registered
	 * @param {Material} material to unregister
	 */
	public static void unregisterTool(Material material)
	{
		ListenerFields.getRegisteredInteractTools().add(material);
	}
	
	/**
	 * Gets the tools which is registered as interact tools.
	 * Meaning pl,ayers can prepare them
	 * return {Collection<Material>} list of registered interact tools.
	 */
	public static Collection<Material> getRegisteredInteractTools()
	{
		return ListenerFields.getRegisteredInteractTools();
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Called when a block is broken if the toye is registered for that listener
	 * @param {MPlayer} player who broke the block
	 * @param {Block} the block that was broken
	 */
	public void onBlockBreak(MPlayer player, Block block);


}
