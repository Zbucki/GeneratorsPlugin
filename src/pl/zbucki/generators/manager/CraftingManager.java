package pl.zbucki.generators.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import pl.zbucki.generators.GeneratorsPlugin;
import pl.zbucki.generators.data.CraftingData;
import pl.zbucki.generators.util.YamlUtil;
import pl.zbucki.generators.util.item.ItemBuilder;
import pl.zbucki.generators.util.item.ItemUtils;

public class CraftingManager {

	private final GeneratorsPlugin plugin;
	private final Map<ItemStack, CraftingData> craftings = new ConcurrentHashMap<>();

	public CraftingManager() {
		this.plugin = GeneratorsPlugin.getInst();
		loadCraftings();

	}

	public CraftingData getCrafting(ItemStack result) {
		return craftings.get(result);
	}

	private void register() {
		craftings.forEach((result, items) -> {
			ShapedRecipe rec = new ShapedRecipe(result);
			char[] recipe = { '0', '1', '2', '3', '4', '5', '6', '7', '8' };
			for (int i = 0; i < 9; i++) {
				if (items.getItems()[i].getType() == Material.AIR) {
					recipe[i] = ' ';
				}
			}
			rec.shape(String.valueOf(recipe[0]) + String.valueOf(recipe[1]) + String.valueOf(recipe[2]),
					String.valueOf(recipe[3]) + String.valueOf(recipe[4]) + String.valueOf(recipe[5]),
					String.valueOf(recipe[6]) + String.valueOf(recipe[7]) + String.valueOf(recipe[8]));
			for (int i = 0; i < 9; i++) {
				if (recipe[i] == ' ') {
					continue;
				}
				rec.setIngredient((i + "").charAt(0), items.getItems()[i].getData());
			}
			plugin.getServer().addRecipe(rec);
		});
	}

	private void loadCraftings() {
		YamlConfiguration yml = YamlUtil.getYaml("craftings");
		for (String k : yml.getConfigurationSection("CRAFTINGS").getKeys(false)) {
			ItemStack[] items = new ItemStack[9];
			Material type = Material.getMaterial(yml.getString("CRAFTINGS." + k + ".RESULT.TYPE"));
			short data = (short) yml.getInt("CRAFTINGS." + k + ".RESULT.DATA");
			int amount = yml.getInt("CRAFTINGS." + k + ".RESULT.AMOUNT");
			String name = yml.getString("CRAFTINGS." + k + ".RESULT.NAME");
			List<String> lore = yml.getStringList("CRAFTINGS." + k + ".RESULT.LORE");
			Map<Enchantment, Integer> ench = getEnchList(yml, "CRAFTINGS." + k + ".RESULT.ENCHANTS");
			ItemStack result = new ItemBuilder(type, amount, data).setLore(lore).setName(name).addEnchantments(ench)
					.build();
			int i = 0;
			for (ItemStack item : ItemUtils.deserialize(yml.getStringList("CRAFTINGS." + k + ".ITEMS"))) {
				items[i] = item;
				i++;
			}
			CraftingData cData = new CraftingData(result, items);
			craftings.put(result, cData);
		}
		plugin.getLogger().info("Zaladowano " + craftings.size() + " nowych craftingow");
		register();
	}

	private Map<Enchantment, Integer> getEnchList(YamlConfiguration yml, String path) {
		Map<Enchantment, Integer> s = new HashMap<>();
		if (yml.isSet(path)) {
			yml.getStringList(path).forEach(str -> {
				s.put(Enchantment.getByName(str.split(";")[0]), Integer.parseInt(str.split(";")[1]));
			});
		}
		return s;
	}

}
