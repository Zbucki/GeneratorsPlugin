package pl.zbucki.generators.data;

import org.bukkit.Location;
import org.bukkit.Material;

import lombok.Data;
import pl.zbucki.generators.manager.GeneratorManager;

@Data
public class PlacedGeneratorData {

	private final GeneratorManager manager;
	private final Location location;
	private final GeneratorData generator;
	private long lastDestroy;

	public PlacedGeneratorData(GeneratorManager manager, Location location, GeneratorData generator) {
		this.manager = manager;
		this.location = location;
		this.generator = generator;
		location.getBlock().setType(Material.STONE);
	}

	public void destroy() {
		setLastDestroy(System.currentTimeMillis() + getGenerator().getTime());
		manager.addToRepair(this);
	}

	public void drop() {
		location.getBlock().setType(Material.AIR);
		location.getWorld().dropItemNaturally(location, generator.getGenerator());
		manager.removeGenerator(location);
	}

}
