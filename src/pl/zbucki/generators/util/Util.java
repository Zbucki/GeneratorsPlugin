package pl.zbucki.generators.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class Util {

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static Location locationFromString(String msg) {
		if (msg == null || msg.equalsIgnoreCase("") || !msg.contains(",")) {
			World world2 = Bukkit.getWorlds().get(0);
			double n = 0.0;
			return new Location(world2, n, n, n);
		}
		String[] array = msg.split(",");
		World world = Bukkit.getWorld(array[0]);
		double x = Double.parseDouble(array[1]);
		double y = Double.parseDouble(array[2]);
		double z = Double.parseDouble(array[3]);
		return new Location(world, x, y, z);
	}

	public static String locationToString(Location loc) {
		if (loc == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(loc.getWorld().getName());
		sb.append("," + Double.toString(loc.getX()));
		sb.append("," + Double.toString(loc.getY()));
		sb.append("," + Double.toString(loc.getZ()));

		return sb.toString();
	}

	public static List<String> fixColor(List<String> list) {
		List<String> mm = new ArrayList<String>();
		for (String s : list) {
			mm.add(fixColor(s));
		}
		return mm;
	}

	public static String fixColor(String text) {
		return ChatColor.translateAlternateColorCodes('&', text.replace("%>", "»").replace("<%", "«"));
	}
}
