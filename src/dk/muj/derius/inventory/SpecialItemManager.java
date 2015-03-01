package dk.muj.derius.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface SpecialItemManager
{
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	public default void register()
	{
		ItemManager.addManager(this);
	}
	
	// -------------------------------------------- //
	// CHANGE
	// -------------------------------------------- //
	
	/**
	 * Takes in an ItemStack and makes it "special".
	 * it will then return the same ItemStack (which is mutable).
	 * @param {ItemStack} itemstack to make special
	 * @return {ItemStack} the special itemstack
	 */
	public abstract ItemStack toSpecial(ItemStack itemStack);
	
	/**
	 * Takes in an "special" ItemStack and makes it "normal".
	 * it will then return the same ItemStack (which is mutable).
	 * @param {ItemStack} itemstack to make normal
	 * @return {ItemStack} the normal itemstack
	 */
	public abstract ItemStack toNormal(ItemStack itemStack);
	
	// -------------------------------------------- //
	// CHECKS
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not this item is made special by this specialiser.
	 * @param {ItemStack} itemStack to check for
	 * @return {boolean} true if item is specialised by this
	 */
	public abstract boolean isSpecial(ItemStack itemStack);
	
	/**
	 * Tells whether or not this item can be made special by this specialiser.
	 * @param {ItemStack} itemStack to check for
	 * @return {boolean} true if item can be specialised.
	 */
	public abstract boolean matches(ItemStack itemStack);
	
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	/**
	 * Clears a whole inventory from all special items by this specialiser.
	 * @param {Inventory} inventory to clean.
	 */
	public default void clearInventory(Inventory inv)
	{
		for (ItemStack is : inv.getContents())
		{
			if ( ! this.matches(is)) continue;
			if ( ! this.isSpecial(is)) continue;
		
			this.toNormal(is);
		}
	
		return;
	}
	
}
