package pl.zbucki.generators.data;

import org.bukkit.inventory.ItemStack;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CraftingData {

	private final ItemStack result;
	private final ItemStack[] items;

}
