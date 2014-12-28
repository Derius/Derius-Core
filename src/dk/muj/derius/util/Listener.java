package dk.muj.derius.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

import dk.muj.derius.entity.MPlayer;

public abstract class Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static Collection<Material> registeredInteractTools = new ArrayList<>();
	private static Map<Material, Listener> blockBreakKeys = new EnumMap<>(Material.class);
	
	// -------------------------------------------- //
	// STATIC METHODS
	// -------------------------------------------- //
	
	/**
	 * Gets the listener that are called when
	 * a block with this material is broken
	 * @param {Material} block type to get
	 * @return {Listener} The listener listening to this block
	 */
	public static Listener getListener(Material material)
	{
		return blockBreakKeys.get(material);
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	/**
	 * Registers the listener
	 * currently unused
	 */
	public void register()
	{

	}
	
	// -------------------------------------------- //
	// KEYS
	// -------------------------------------------- //

	/**
	 * Registers a block type to listen for (when broken)
	 * @param {Material} block type to listen for
	 */
	public void registerBlockBreakKey(Material... materials)
	{
		for(Material material: materials)
			blockBreakKeys.put(material,this);
	}
	
	/**
	 * Registers a collection of block types to listen for (when broken)
	 * @param {Collection<Material>} collection of block types to listen for
	 */
	public void registerBlockBreakKeys(Collection<Material> materials)
	{
		for(Material material: materials)
			this.registerBlockBreakKey(material);
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
			registeredInteractTools.add(material);
	}
	
	/**
	 * Makes it possible for players to prepare the tool
	 * @param {Material} material to register
	 */
	public static void registerTool(Material material)
	{
		registeredInteractTools.add(material);
	}
	
	/**
	 * Tells whether or not a tool can be prepared
	 * @param {Material} material to check for
	 * @return {boolean} true if tool is registered
	 */
	public static boolean isRegistered(Material material)
	{
		return registeredInteractTools.contains(material);
	}
	
	/**
	 * Makes it not possible for players to prepare the tool if already registered
	 * @param {Material} material to unregister
	 */
	public static void unregisterTool(Material material)
	{
		registeredInteractTools.add(material);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Called when a block is broken if the toye is registered for that listener
	 * @param {MPlayer} player who broke the block
	 * @param {Block} the block that was broken
	 */
	public abstract void onBlockBreak(MPlayer player, Block block);


}
