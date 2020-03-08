package pl.zbucki.generators.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.zbucki.generators.GeneratorsPlugin;

public class YamlUtil {

	public static File getFile(String name) {
		File file = new File(GeneratorsPlugin.getInst().getDataFolder(), name + ".yml");
		if (!file.exists()) {
			if (GeneratorsPlugin.getInst().getResource(name + ".yml") != null) {
				GeneratorsPlugin.getInst().saveResource(name + ".yml", true);
				return file;
			}
			try {
				file.createNewFile();
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void setValue(Class<?> clazz, Object instance, String fieldName, Object value) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException exx) {
			exx.printStackTrace();
		}
	}

	public static YamlConfiguration getYaml(File file) {
		return YamlConfiguration.loadConfiguration((File) file);
	}

	public static YamlConfiguration getYaml(String name) {
		return YamlConfiguration.loadConfiguration((File) YamlUtil.getFile(name));
	}

	public static boolean saveYaml(YamlConfiguration yaml, File file) {
		try {
			yaml.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
