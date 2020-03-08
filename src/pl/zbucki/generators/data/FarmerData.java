package pl.zbucki.generators.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.GeneratorsPlugin;
import pl.zbucki.generators.util.runnable.FarmerBuildRunnable;

@Data
@RequiredArgsConstructor
public class FarmerData {

	private final ItemStack item;
	private final Material block;
	private final long time;
	private final int atOnceSize;

	public void build(Location loc) {
		new FarmerBuildRunnable(this, loc).runTaskTimer(GeneratorsPlugin.getInst(),
				time * 20, time * 20);
	}

}
