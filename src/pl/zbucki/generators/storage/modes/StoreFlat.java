package pl.zbucki.generators.storage.modes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.manager.GeneratorManager;
import pl.zbucki.generators.storage.Store;
import pl.zbucki.generators.storage.StoreMode;
import pl.zbucki.generators.util.Util;
import pl.zbucki.generators.util.YamlUtil;

@RequiredArgsConstructor
public class StoreFlat implements Store {

	private final GeneratorManager manager;
	private final YamlConfiguration config;
	private int loadedData;

	@Override
	public void connect() {
		manager.getDataExecutor().execute(() -> {
			config.getStringList("GENERATORS").forEach(generator -> {
				String[] split = generator.split(":");
				Location location = Util.locationFromString(split[0]);
				String generatorId = split[1];
				manager.addGenerator(location, manager.getGeneratorData(generatorId), false);
				loadedData++;
			});
		});
	}

	@Override
	public void disconnect() {
		manager.getDataExecutor().execute(() -> {
			List<String> parsed = new ArrayList<>();
			manager.getPlacedGenerators().values().forEach(generator -> parsed
					.add(Util.locationToString(generator.getLocation()) + ":" + generator.getGenerator().getId()));
			config.set("GENERATORS", parsed);
			YamlUtil.saveYaml(config, YamlUtil.getFile("placedgenerators"));
		});
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public StoreMode getMode() {
		return StoreMode.FLAT;
	}

	@Override
	public int loadedData() {
		return loadedData;
	}
}
