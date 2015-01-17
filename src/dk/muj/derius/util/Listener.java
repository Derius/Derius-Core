package dk.muj.derius.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dk.muj.derius.entity.MPlayer;

public interface Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	static class ListenerFields
	{
		// Interact tools
		private static Collection<Material> registeredInteractTools = new HashSet<>();
		static Collection<Material> getRegisteredInteractTools() { return registeredInteractTools; }
		
		// Block break
		private static Map<Material, Listener> blockBreakKeys = new EnumMap<>(Material.class);
		static Map<Material, Listener> getBlockBreakKeys() { return blockBreakKeys; }

		// Player deal damage
		private static Map<Material, Listener> dealDamageKeys = new EnumMap<>(Material.class);
		static Map<Material, Listener> getDealDamageKeys() { return dealDamageKeys; }
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
		this.registerBlockBreakKey(materials.toArray(new Material[materials.size()]));
	}
	
	/**
	 * Gets the listener that are called when
	 * a block with this material is broken
	 * @param {Material} block type key to get
	 * @return {Listener} The listener listening to this block
	 */
	public static Listener getBlockBreakListener(Material material)
	{
		return ListenerFields.getBlockBreakKeys().get(material);
	}

	// -------------------------------------------- //
	// BLOCK BREAK KEYS
	// -------------------------------------------- //

	/**
	 * Registers a weapon type to listen for when player deals damage
	 * @param {Material} weapon type to listen for
	 */
	public default void registerPlayerAttackKey(Material... materials)
	{
		for(Material material: materials)
			ListenerFields.getDealDamageKeys().put(material,this);
	}
	
	/**
	 * Registers a collection of weapons to listen for player deals damage
	 * @param {Collection<Material>} collection of block types to listen for
	 */
	public default void registerPlayerAttackKey(Collection<Material> materials)
	{
		this.registerPlayerAttackKey(materials.toArray(new Material[materials.size()]));
	}
	
	/**
	 * Gets the listener that are called when
	 * a player deals damage
	 * @param {Material} weapon key to get
	 * @return {Listener} The listener listening for this weapon
	 */
	public static Listener getPlayerAttackKeyListener(Material material)
	{
		return ListenerFields.getDealDamageKeys().get(material);
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
	// LISTENER METHODS
	// -------------------------------------------- //
	
	/**
	 * Called when a block is broken if the block is registered for that listener
	 * @param {MPlayer} player who broke the block
	 * @param {Block} the block that was broken
	 */
	default void onBlockBreak(MPlayer player, BlockState block) {};
	
	/**
	 * 
	 * @param attacker
	 * @param target
	 */
	default void onPlayerAttack(MPlayer attacker, EntityDamageByEntityEvent event) {};

}
