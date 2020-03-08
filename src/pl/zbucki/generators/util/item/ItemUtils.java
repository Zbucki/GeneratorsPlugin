package pl.zbucki.generators.util.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.zbucki.generators.util.Util;

public class ItemUtils {
	public static Map<String, Enchantment> ENCHANTMENTS;

	public static Enchantment getEnchantment(String g) {
		return ENCHANTMENTS.get(g);
	}

	public static List<ItemStack> deserialize(List<String> items) {
		List<ItemStack> im = new ArrayList<ItemStack>();
		if (items == null || items.isEmpty()) {
			return im;
		}
		for (String s : items) {
			im.add(deserialize(s));
		}
		return im;
	}

	public static ItemStack deserialize(String item) {
		if (item == null || item.isEmpty()) {
			return new ItemStack(Material.AIR);
		}
		String[] s = item.split(" ");
		Material id = Material.AIR;
		int subid = 0;
		String ia = s[0];
		if (ia.contains(":")) {
			String[] ss = ia.split(":");
			id = getMaterialFromString(ss[0]);
			subid = Integer.parseInt(ss[1]);
		} else {
			id = getMaterialFromString(ia);
		}
		int i = 1;
		if (s.length < 2) {
			i = 1;
		} else {
			i = Integer.parseInt(s[1]);
		}
		ItemStack is = new ItemBuilder(id, i, (short) subid).build();
		ItemMeta im = is.getItemMeta();
		for (int o = 1; o < s.length; ++o) {
			if (s[o].contains(":")) {
				String[] d = s[o].split(":");
				if (d[0].equalsIgnoreCase("name")) {
					String name = d[1];
					if (name != null) {
						if (!name.isEmpty()) {
							im.setDisplayName(Util.fixColor(name.replace("_", " ")));
						}
					}
				}
				if (d[0].equalsIgnoreCase("lore")) {
					if (d[1] != null) {
						if (!d[1].isEmpty()) {
							List<String> lore = new ArrayList<String>();
							for (String ss2 : Arrays.asList(d[1].split(","))) {
								lore.add(Util.fixColor(ss2.replace("_", " ")));
							}
							im.setLore(lore);
						}
					}
				}
				if (ENCHANTMENTS.containsKey(d[0].toLowerCase()) && d[1] != null) {
					if (Util.isInt(d[1])) {
						im.addEnchant((Enchantment) ENCHANTMENTS.get(d[0].toLowerCase()), Integer.parseInt(d[1]), true);
					}
				}
			}
		}
		is.setItemMeta(im);
		is.setAmount(i);
		return is;
	}

	public static Material getMaterialFromString(String mat) {
		Material m = Material.getMaterial(mat.toUpperCase());
		if (m != null) {
			return m;
		}
		return Material.AIR;
	}

	static {
		(ENCHANTMENTS = new HashMap<String, Enchantment>()).put("alldamage", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("alldmg", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("sharpness", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("sharp", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("dal", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("dar", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS.put("smite", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS.put("du", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS.put("digspeed", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("efficiency", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("minespeed", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("cutspeed", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("ds", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("eff", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("durability", Enchantment.DURABILITY);
		ENCHANTMENTS.put("dura", Enchantment.DURABILITY);
		ENCHANTMENTS.put("unbreaking", Enchantment.DURABILITY);
		ENCHANTMENTS.put("d", Enchantment.DURABILITY);
		ENCHANTMENTS.put("thorns", Enchantment.THORNS);
		ENCHANTMENTS.put("highcrit", Enchantment.THORNS);
		ENCHANTMENTS.put("thorn", Enchantment.THORNS);
		ENCHANTMENTS.put("highercrit", Enchantment.THORNS);
		ENCHANTMENTS.put("t", Enchantment.THORNS);
		ENCHANTMENTS.put("fireaspect", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("fire", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("meleefire", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("meleeflame", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("fa", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("knockback", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("kback", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("kb", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("k", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("looting", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("lbm", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("oxygen", Enchantment.OXYGEN);
		ENCHANTMENTS.put("respiration", Enchantment.OXYGEN);
		ENCHANTMENTS.put("breathing", Enchantment.OXYGEN);
		ENCHANTMENTS.put("breath", Enchantment.OXYGEN);
		ENCHANTMENTS.put("o", Enchantment.OXYGEN);
		ENCHANTMENTS.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("pe", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("fallprotection", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("fallprot", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("featherfall", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("featherfalling", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("pfa", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("fireprotection", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("flameprotection", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("fireprotect", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("flameprotect", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("fireprot", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("flameprot", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("pf", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS.put("projprot", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS.put("pp", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS.put("silktouch", Enchantment.SILK_TOUCH);
		ENCHANTMENTS.put("softtouch", Enchantment.SILK_TOUCH);
		ENCHANTMENTS.put("st", Enchantment.SILK_TOUCH);
		ENCHANTMENTS.put("waterworker", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("aquaaffinity", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("watermine", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("ww", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("firearrow", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("flame", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("flamearrow", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("af", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("arrowdamage", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("power", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("arrowpower", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("ad", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("punch", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("ak", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("infinitearrows", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("infarrows", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("infinity", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("infinite", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("unlimited", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("ai", Enchantment.ARROW_INFINITE);
	}
}
