package pl.zbucki.generators;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import pl.zbucki.generators.listeners.BlockListener;
import pl.zbucki.generators.listeners.CraftingListener;
import pl.zbucki.generators.listeners.ExplodeListener;
import pl.zbucki.generators.manager.CraftingManager;
import pl.zbucki.generators.manager.FarmerManager;
import pl.zbucki.generators.manager.GeneratorManager;
import pl.zbucki.generators.storage.Store;
import pl.zbucki.generators.storage.modes.StoreFlat;
import pl.zbucki.generators.storage.modes.StoreMongoDB;
import pl.zbucki.generators.util.YamlUtil;
import pl.zbucki.generators.util.config.Settings;
import pl.zbucki.generators.util.runnable.GeneratorRepairRunnable;

public class GeneratorsPlugin extends JavaPlugin {

	@Getter
	private static GeneratorsPlugin inst;
	@Getter
	private Store store;
	@Getter
	private Settings settings;
	
	private GeneratorManager generatorManager;
	private FarmerManager farmerManager;
	private CraftingManager craftingManager;

	@Override
	public void onEnable() {
		inst = this;
		settings = new Settings();
		registerManagers();
		registerListeners();
		if (!registerDatabase()) {
			getLogger().warning("Nie mozna polaczyc sie z baza danych!");
		}
	}

	@Override
	public void onDisable() {
		generatorManager.getToRepair().forEach(generator -> generatorManager.repairGenerator(generator));
		Bukkit.getScheduler().cancelAllTasks();
		store.disconnect();
	}

	private void registerManagers() {
		generatorManager = new GeneratorManager();
		farmerManager = new FarmerManager();
		craftingManager = new CraftingManager();
		new GeneratorRepairRunnable(generatorManager).start();
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new CraftingListener(craftingManager), this);
		getServer().getPluginManager().registerEvents(new BlockListener(generatorManager, farmerManager), this);
		getServer().getPluginManager().registerEvents(new ExplodeListener(generatorManager), this);
	}

	private boolean registerDatabase() {
		boolean flat = settings.isFlat();
		if (flat) {
			store = new StoreFlat(generatorManager, YamlUtil.getYaml("placedgenerators"));
		} else {
			store = new StoreMongoDB(this, generatorManager);
		}
		generatorManager.getDataExecutor().execute(() -> store.connect());
		getLogger().info("Zaladowano " + store.loadedData() + " polozonych generatorow!");
		return store.isConnected();
	}
}
