package pl.zbucki.generators.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import pl.zbucki.generators.GeneratorsPlugin;
import pl.zbucki.generators.data.FarmerData;
import pl.zbucki.generators.util.YamlUtil;
import pl.zbucki.generators.util.item.ItemBuilder;
import pl.zbucki.generators.util.item.ItemUtils;

public class FarmerManager {

	private static final List<FarmerData> farmers = new ArrayList<>();

	public FarmerManager() {
		loadFarmers();
	}

	public FarmerData getFarmerData(ItemStack item) {
		if (item == null) {
			return null;
		}
		if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return null;
		}
		return farmers.stream().filter(far -> far.getItem().getType().equals(item.getType()))
				.filter(far -> far.getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))
				.findFirst().orElse(null);
	}

	private void loadFarmers() {
		YamlConfiguration yml = YamlUtil.getYaml("farmers");
		for (String k : yml.getConfigurationSection("FARMERS").getKeys(false)) {

			Material type = Material.getMaterial(yml.getString("FARMERS." + k + ".ITEM.TYPE"));
			Material block = Material.getMaterial(yml.getString("FARMERS." + k + ".BLOCK_TO_REPLACE"));

			String name = yml.getString("FARMERS." + k + ".ITEM.NAME");

			List<String> lore = yml.getStringList("FARMERS." + k + ".ITEM.LORE");
			List<String> ench = yml.getStringList("FARMERS." + k + ".ITEM.ENCHANTS");

			int sizeBlocks = yml.getInt("FARMERS." + k + ".AT_ONCE_SIZE");
			long time = yml.getLong("FARMERS." + k + ".TIME");

			ItemBuilder item = new ItemBuilder(type, 1).setName(name).setLore(lore);
			if (!ench.isEmpty()) {
				ench.forEach(e -> {
					item.addEnchantment(ItemUtils.getEnchantment(e.split(";")[0]), Integer.parseInt(e.split(";")[1]));
				});
			}
			FarmerData data = new FarmerData(item.build(), block, time, sizeBlocks);
			farmers.add(data);
		}
		GeneratorsPlugin.getInst().getLogger().info("Zaladowano " + farmers.size() + " farmery");

	}

}
