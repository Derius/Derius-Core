package dk.muj.derius.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;

import dk.muj.derius.DeriusCore;

/**
 * Idea behind item manager: We want to give an item an encahnt and remove it when ability deactivates.
 * Logical solution: Would be to pass the itemStack from onActive to onDeactive in the active ability.
 * Issue: Bukkit changes the ItemStack and we can't trust the reference.
 * Solution: On disable we check for ALL items in a players inventory and make them not special.
 * if a player tries to circumvent this by making the item escape his/her inventory,
 * we also check for that and make the item normal again.
 */
public final class ItemManager implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ItemManager i = new ItemManager();
	public static ItemManager get() { return i; }
	private ItemManager() { }
	
	public static void setup()
	{
		Bukkit.getPluginManager().registerEvents(get(), DeriusCore.get());
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected static List<SpecialItemManager> managers = new ArrayList<>();
	public static List<SpecialItemManager> getManagers() { return new ArrayList<>(managers); }
	public static void addManager(SpecialItemManager manager) { managers.add(manager); }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public static boolean isSpecial(ItemStack is)
	{
		if (is == null) return false;
		for(SpecialItemManager manager : managers)
		{
			if (manager.isSpecial(is)) return true;
		}
		
		return false;
	}
	
	public static ItemStack toNormal(ItemStack is)
	{
		if (is == null) return null;
		for(SpecialItemManager manager : managers)
		{
			if ( ! manager.isSpecial(is)) continue;
			is = manager.toNormal(is);
		}
		
		return is;
	}
	
	public static void cleanInventory(Inventory inv)
	{
		if (inv == null) return;
		for(SpecialItemManager manager : managers)
		{
			manager.clearInventory(inv);
		}
	}
	
	// -------------------------------------------- //
	// ANTI CHEAT EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void handleDrop(PlayerDropItemEvent event)
	{
		toNormal(event.getItemDrop().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void handleMove(InventoryClickEvent event)
	{
		toNormal(event.getCurrentItem());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void handleMove(EventMassiveCorePlayerLeave event)
	{
		cleanInventory(event.getPlayer().getInventory());
	}
	
}
