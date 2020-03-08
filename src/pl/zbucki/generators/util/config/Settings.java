package pl.zbucki.generators.util.config;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.zbucki.generators.storage.StoreMode;
import pl.zbucki.generators.util.YamlUtil;

public class Settings {

	public String MONGODB_HOST, MONGODB_DATABASE, MONGODB_USERNAME, MONGODB_PASSWORD;
	public int MONGODB_PORT;
	private static StoreMode MODE;

	public Settings() {
		YamlConfiguration yaml = YamlUtil.getYaml("settings");
		MONGODB_HOST = yaml.getString("DATABASE.HOST");
		MONGODB_DATABASE = yaml.getString("DATABASE.DATABASE");
		MONGODB_USERNAME = yaml.getString("DATABASE.USERNAME");
		MONGODB_PASSWORD = yaml.getString("DATABASE.PASSWORD");
		MONGODB_PORT = yaml.getInt("DATABASE.PORT");
		MODE = StoreMode.parseMode(yaml.getString("DATABASE.MODE"));
	}
	
	public boolean isFlat() {
		return MODE == StoreMode.FLAT;
	}

}
