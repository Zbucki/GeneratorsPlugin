package pl.zbucki.generators.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import pl.zbucki.generators.GeneratorsPlugin;
import pl.zbucki.generators.data.GeneratorData;
import pl.zbucki.generators.data.PlacedGeneratorData;
import pl.zbucki.generators.util.YamlUtil;
import pl.zbucki.generators.util.item.ItemBuilder;
import pl.zbucki.generators.util.item.ItemUtils;

public class GeneratorManager {

	private final GeneratorsPlugin plugin;
	@Getter
	private final ExecutorService dataExecutor = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	@Getter
	private final Map<Location, PlacedGeneratorData> placedGenerators = new ConcurrentHashMap<>();
	@Getter
	private final List<PlacedGeneratorData> toRepair = new ArrayList<>();
	private final Map<String, GeneratorData> generators = new ConcurrentHashMap<>();

	public GeneratorManager() {
		this.plugin = GeneratorsPlugin.getInst();
		loadGenerators();
	}

	public PlacedGeneratorData getPlacedGenerator(Location loc) {
		return placedGenerators.get(loc);
	}

	public GeneratorData getGeneratorData(String id) {
		return generators.get(id);
	}

	public void addGenerator(Location loc, GeneratorData type, boolean save) {
		PlacedGeneratorData data = new PlacedGeneratorData(this, loc, type);
		placedGenerators.put(loc, data);
	}

	public void addToRepair(PlacedGeneratorData data) {
		toRepair.add(data);
	}

	public void repairGenerator(PlacedGeneratorData data) {
		data.getLocation().getBlock().setType(Material.STONE);
		data.setLastDestroy(0);
		toRepair.remove(data);
	}
	
	public void removeGenerator(Location loc) {
		placedGenerators.remove(loc);
	}

	public GeneratorData getGeneratorType(ItemStack item) {
		if (item == null) {
			return null;
		}
		if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return null;
		}
		String name = item.getItemMeta().getDisplayName();
		return generators.values().stream().filter(f -> f.getGenerator().getType().equals(item.getType()))
				.filter(f -> f.getGenerator().getItemMeta().getDisplayName().equals(name)).findFirst().orElse(null);
	}

	public void loadGenerators() {
		YamlConfiguration yml = YamlUtil.getYaml("generators");
		for (String k : yml.getConfigurationSection("GENERATORS").getKeys(false)) {

			Material type = Material.getMaterial(yml.getString("GENERATORS." + k + ".TYPE"));

			String name = yml.getString("GENERATORS." + k + ".NAME");
			long regen_time = yml.getLong("GENERATORS." + k + ".REGEN_TIME");

			List<String> lore = yml.getStringList("GENERATORS." + k + ".LORE");
			List<String> ench = yml.getStringList("GENERATORS." + k + ".ENCHANTS");

			Material mat = ItemUtils.getMaterialFromString(yml.getString("GENERATORS." + k + ".DESTROY_ITEM"));
			ItemBuilder item = new ItemBuilder(type, 1).setName(name).setLore(lore);

			if (!ench.isEmpty()) {
				ench.forEach(e -> {
					item.addEnchantment(Enchantment.getByName(e.split(";")[0]), Integer.parseInt(e.split(";")[1]));
				});
			}
			GeneratorData generator = new GeneratorData(k, item.build(), mat, regen_time);
			generators.put(k, generator);
		}
		plugin.getLogger().info("Zaladowano " + generators.size() + " generatorow");
	}

}
