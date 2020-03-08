package pl.zbucki.generators.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.data.CraftingData;
import pl.zbucki.generators.manager.CraftingManager;

@RequiredArgsConstructor
public class CraftingListener implements Listener {

	private final CraftingManager manager;
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		CraftingData data = manager.getCrafting(e.getRecipe().getResult());
		if (data == null) {
			return;
		}
		boolean isfull = true;
		for (int a = 1; a <= 9; a++) {
			if (e.getInventory().getItem(a) != null) {
				ItemStack it = data.getItems()[a - 1];
				if (e.getInventory().getItem(a).getType() != it.getType()) {
					isfull = false;
				}
				if (e.getInventory().getItem(a).getAmount() < it.getAmount()) {
					isfull = false;
				}
			}
		}
		e.setCancelled(true);
		if (!isfull) {
			return;
		}
		for (int a = 1; a <= 9; a++) {
			removeItem(e.getInventory(), a, data.getItems()[a - 1].getAmount());
		}
		e.getWhoClicked().getInventory().addItem(data.getResult());
		((Player) e.getWhoClicked()).updateInventory();
		e.getWhoClicked().closeInventory();

	}

	private void removeItem(Inventory inv, int slot, int amount) {
		if (inv.getItem(slot) == null) {
			return;
		}
		ItemStack item = inv.getItem(slot).clone();
		if (item == null || item.getType().equals(Material.AIR)) {
			return;
		}
		if (item.getAmount() <= amount) {
			inv.setItem(slot, new ItemStack(Material.AIR));
			return;
		}
		item.setAmount(item.getAmount() - amount);
		inv.setItem(slot, item);
	}

}
