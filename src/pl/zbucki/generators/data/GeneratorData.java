package pl.zbucki.generators.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeneratorData {

	private final String id;
	private final ItemStack generator;
	private final Material destroyItem;
	private final long time;

}
