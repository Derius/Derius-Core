package dk.muj.derius.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;

public class ItemUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN
	// -------------------------------------------- //
	
	private ItemUtil() {}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	@SuppressWarnings("unused")
	private static Map<Material, Integer> maxDurability = ItemUtil.durabilityMap;
	
	// -------------------------------------------- //
	// DURABILITY
	// -------------------------------------------- //
	
	/**
	 * Applies the specified amount of damage to a tool/armor
	 * returns true if the item has been broken.
	 * @param {ItemStack} The item you want to modify
	 * @param {short} the amount of damage you want to apply to it
	 * @return {boolean} whether it breaks or not
	 */
	public static boolean applyDamage(ItemStack item, short damage)
	{
		/*
		 * Remember, durability in minecraft counts upwards till it 
		 * reaches the maximum. In this case, it destroys the itemstack.
		 */
		if (item == null || item.getType() == Material.AIR) return false;
		if (damage < 0)
		{
			damage = (short) -damage;
		}
		
		short newDurability = (short) (damage / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1) + item.getDurability());
		short maxDurability = ItemUtil.maxDurability(item);
		
		if (newDurability >= maxDurability)
		{
			item.setDurability(maxDurability);
			return true;
		}
		else
		{
			item.setDurability(newDurability);
			return false;
		}
	}
	
	/**
	 * Reduces an amount of damage for the tool/armor specified
	 * returns true if the item has been fully repaired by it.
	 * @param {ItemStack} The item you want to modify
	 * @param {short} the amount of damage you want to take from it
	 * @return {boolean} whether it is fully repaired or not
	 */
	public static boolean reduceDamage(ItemStack item, short reduce)
	{
		if (item == null || item.getType() == Material.AIR) return false;
		if (reduce < 0)
		{
			reduce = (short) -reduce;
		}
		
		short newDurability = (short) (item.getDurability() - reduce);
		
		if (newDurability <= 0)
		{
			item.setDurability((short) 0);
			return true;
		}
		else
		{
			item.setDurability(newDurability);
			return false;
		}
	}

	public static short maxDurability(ItemStack item)
	{
		Material material = item.getType();
		if ( ! ItemUtil.durabilityMap.containsKey(material))
		{
			Derius.get().log("An Error occured when trying to get the");
			Derius.get().log(Txt.parse("maxDurability of %s. Either, it has not been added,", material.toString()));
			Derius.get().log("or your code has an error.");
			
			return (short) 10;
		}
		
		int dur = ItemUtil.durabilityMap.get(material);
		return (short) dur;
	}
	
	// -------------------------------------------- //
	// LORE
	// -------------------------------------------- //
	
	/**
	 * Adds the specified lore to the item passed in.
	 * @param {ItemStack} the item you want the lore added to
	 * @param {List<String} the lore you want to add
	 */
	public static void addLore(ItemStack item, List<String> lore)
	{
		if (item == null) return;
		if (lore == null)
		{
			lore = new ArrayList<>(1);
		}
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.hasLore() ? meta.getLore() : lore;
		itemLore.addAll(lore);
		
		meta.setLore(lore);
	}
	
	/**
	 * Removes the specified lore to the item passed in.
	 * @param {ItemStack} the item you want the lore added to
	 * @param {List<String} the lore you want to remove
	 */
	public static void removeLore(ItemStack item, List<String> lore)
	{
		if (item == null) return;
		if (lore == null)
		{
			lore = new ArrayList<>(1);
		}
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.hasLore() ? meta.getLore() : lore;
		itemLore.removeAll(lore);
		
		meta.setLore(lore);
	}
	
	/**
	 * Uses the lore passed in and either adds or removes it,
	 * depending on the item.
	 * @param {ItemStack} the item you want it's lore changed
	 * @param {List<String} the lore you want to add/remove
	 */
	public static void changeLore(ItemStack item, List<String> lore)
	{
		if (item == null) return;
		if (lore == null) return;
		
		ItemMeta meta = item.getItemMeta();
		if (meta.hasLore())
		{
			List<String> itemLore = meta.getLore();
			boolean contains = itemLore.containsAll(lore);
			
			if ( ! contains)
			{
				ItemUtil.addLore(item, lore);
			}
			else
			{
				ItemUtil.removeLore(item, lore);
			}
		}
	}
	
	// -------------------------------------------- //
	// ENTCHANTMENTS
	// -------------------------------------------- //
	
	public static void addEntchantment(ItemStack item, Map<Enchantment, Integer> entchantments)
	{
		if (item == null) return;
		if (entchantments == null) return;
		Map<Enchantment, Integer> itemEntchantsments= item.getEnchantments();
		
		for (Enchantment ench : entchantments.keySet())
		{
			int level = entchantments.get(ench);
			if (itemEntchantsments.containsKey(ench))
			{
				level = level + itemEntchantsments.get(ench);
				item.removeEnchantment(ench);
			}
			item.addEnchantment(ench, level);
		}
	}
	
	public static void removeEntchantment(ItemStack item, Map<Enchantment, Integer> entchantments)
	{
		if (item == null) return;
		if (entchantments == null) return;
		Map<Enchantment, Integer> itemEntchantsments= item.getEnchantments();
		
		for (Enchantment ench : entchantments.keySet())
		{
			int level = itemEntchantsments.get(ench);
			if (itemEntchantsments.containsKey(ench))
			{
				level = level - entchantments.get(ench);
				item.removeEnchantment(ench);
			}
			item.addEnchantment(ench, level);
		}
	}
	
	// -------------------------------------------- //
	// MATERIALTOSHORT MAP
	// -------------------------------------------- //
	
	private static Map<Material, Integer> durabilityMap = MUtil.map(
			// Leather Items
			Material.LEATHER_HELMET,		 56,
			Material.LEATHER_CHESTPLATE,	 81,
			Material.LEATHER_LEGGINGS,		 76,
			Material.LEATHER_BOOTS,			 60,
			// Wood Items
			Material.WOOD_SWORD,			 60,
			Material.WOOD_AXE,				 60,
			Material.WOOD_SPADE,			 60,
			Material.WOOD_PICKAXE,			 60,
			Material.WOOD_HOE,				 60,
			// Gold Items
			Material.GOLD_HELMET,			 78,
			Material.GOLD_CHESTPLATE,		 113,
			Material.GOLD_LEGGINGS,			 106,
			Material.GOLD_BOOTS,			 92,
			Material.GOLD_SWORD,			 33,
			Material.GOLD_AXE,				 33,
			Material.GOLD_SPADE,			 33,
			Material.GOLD_PICKAXE,			 33,
			Material.GOLD_HOE,				 33,
			// Chainmail Items
			Material.CHAINMAIL_HELMET,		 166,
			Material.CHAINMAIL_CHESTPLATE,	 241,
			Material.CHAINMAIL_LEGGINGS,	 226,
			Material.CHAINMAIL_BOOTS,		 196,
			// Stone Items
			Material.STONE_SWORD,			 132,
			Material.STONE_AXE,				 132,
			Material.STONE_SPADE,			 132,
			Material.STONE_PICKAXE,			 132,
			Material.STONE_HOE,				 132,
			// Iron Items
			Material.IRON_HELMET,			 166,
			Material.IRON_CHESTPLATE,		 241,
			Material.IRON_LEGGINGS,			 226,
			Material.IRON_BOOTS,			 196,
			Material.IRON_SWORD,			 251,
			Material.IRON_AXE,				 251,
			Material.IRON_SPADE,			 251,
			Material.IRON_PICKAXE,			 251,
			Material.IRON_HOE,				 251,
			// Iron Items
			Material.DIAMOND_HELMET,		 364,
			Material.DIAMOND_CHESTPLATE,	 529,
			Material.DIAMOND_LEGGINGS,		 496,
			Material.DIAMOND_BOOTS,			 430,
			Material.DIAMOND_SWORD,			 1562,
			Material.DIAMOND_AXE,			 1562,
			Material.DIAMOND_SPADE,			 1562,
			Material.DIAMOND_PICKAXE,		 1562,
			Material.DIAMOND_HOE,			 1562,

			// Misc Items
			Material.CARROT_STICK,			 26,
			Material.SHEARS,				 239,
			Material.FLINT_AND_STEEL,		 65,
			Material.FISHING_ROD,			 65,
			Material.BOW,					 385
			
			);
	
}
