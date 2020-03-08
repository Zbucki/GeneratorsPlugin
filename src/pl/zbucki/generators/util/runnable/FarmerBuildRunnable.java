package pl.zbucki.generators.util.runnable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import pl.zbucki.generators.data.FarmerData;

public class FarmerBuildRunnable extends BukkitRunnable {

	private final Location startLocation;
	private final FarmerData data;

	public FarmerBuildRunnable(FarmerData data, Location startLocation) {
		this.data = data;
		this.startLocation = startLocation;
	}

	@Override
	public void run() {
		for (int i = 0; i < data.getAtOnceSize(); i++) {
			Location loc = startLocation.subtract(0.0D, 1, 0.0D);
			if (loc.getBlock().getType() == Material.BEDROCK) {
				this.cancel();
				return;
			}
			loc.getBlock().setType(data.getBlock());
		}
	}

}
