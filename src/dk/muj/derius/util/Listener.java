package dk.muj.derius.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dk.muj.derius.api.player.DPlayer;

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
		
		// Block break
		private static Map<Material, Listener> blockBreakKeys = new EnumMap<>(Material.class);
		protected static Map<Material, Listener> getBlockBreakKeys() { return blockBreakKeys; }

		// Player deal damage
		private static Map<Material, Listener> dealDamageKeys = new EnumMap<>(Material.class);
		protected static Map<Material, Listener> getDealDamageKeys() { return dealDamageKeys; }
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
	@Deprecated
	public static void registerBlockBreakKey(Listener listener, Material... materials)
	{
		Validate.notNull(listener, "listener mustn't be null");
		Validate.notNull(materials, "materials mustn't be null");
		
		for (Material material : materials)
		{
			Fields.getBlockBreakKeys().put(material, listener);
		}
	}
	
	/**
	 * Registers a collection of block types to listen for (when broken)
	 * @param {Collection<Material>} collection of block types to listen for
	 */
	@Deprecated
	public static void registerBlockBreakKeys(Listener listener, Collection<Material> materials)
	{
		Validate.notNull(listener, "listener mustn't be null");
		Validate.notNull(materials, "materials mustn't be null");
		
		for (Material material : materials)
		{
			Fields.getBlockBreakKeys().put(material, listener);
		}
	}
	
	/**
	 * Gets the listener that are called when
	 * a block with this material is broken
	 * @param {Material} block type key to get
	 * @return {Listener} The listener listening to this block
	 */
	@Deprecated
	public static Listener getBlockBreakListener(Material material)
	{
		return Fields.getBlockBreakKeys().get(material);
	}

	// -------------------------------------------- //
	// BLOCK BREAK KEYS
	// -------------------------------------------- //

	/**
	 * Registers a weapon type to listen for when player deals damage
	 * @param {Material} weapon type to listen for
	 */
	@Deprecated
	public static void registerPlayerAttackKey(Listener listener, Material... materials)
	{
		Validate.notNull(listener, "listener mustn't be null");
		Validate.notNull(materials, "materials mustn't be null");
		
		for (Material material : materials)
		{
			Fields.getDealDamageKeys().put(material, listener);
		}
	}
	
	/**
	 * Registers a collection of weapons to listen for player deals damage
	 * @param {Collection<Material>} collection of block types to listen for
	 */
	@Deprecated
	public static void registerPlayerAttackKeys(Listener listener, Collection<Material> materials)
	{
		Validate.notNull(listener, "listener mustn't be null");
		Validate.notNull(materials, "materials mustn't be null");
		
		for (Material material : materials)
		{
			Fields.getDealDamageKeys().put(material, listener);
		}
	}
	
	/**
	 * Gets the listener that are called when
	 * a player deals damage
	 * @param {Material} weapon key to get
	 * @return {Listener} The listener listening for this weapon
	 */
	@Deprecated
	public static Listener getPlayerAttackKeyListener(Material material)
	{
		return Fields.getDealDamageKeys().get(material);
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
	
	// -------------------------------------------- //
	// LISTENER METHODS
	// -------------------------------------------- //
	
	/**
	 * Called when a block is broken if the block is registered for that listener
	 * @param {DPlayer} player who broke the block
	 * @param {Block} the block that was broken
	 */
	@Deprecated
	default void onBlockBreak(DPlayer dplayer, BlockState block) {};
	
	/**
	 * Called when a player attacks an entity.
	 * @param {DPlayer} the player attacking
	 * @param {EntityDamageByEntityEvent} the event causing this
	 */
	@Deprecated
	default void onPlayerAttack(DPlayer attacker, EntityDamageByEntityEvent event) {};

}
