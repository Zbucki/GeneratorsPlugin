package pl.zbucki.generators.util.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.zbucki.generators.util.Util;

public class ItemBuilder {
	
	private final Material mat;
	private int amount;
	private short data;
	private HashMap<Enchantment, Integer> enchants;
	private List<String> lore;
	private String name;

	public ItemBuilder(Material mat, int amount, String name) {
		this.data = (short) 0;
		this.lore = new ArrayList<String>();
		this.enchants = new HashMap<Enchantment, Integer>();
		this.mat = mat;
		this.amount = amount;
		this.name = Util.fixColor(name);
	}

	public ItemBuilder(Material mat, int amount, short data) {
		this.data = (short) 0;
		this.lore = new ArrayList<String>();
		this.enchants = new HashMap<Enchantment, Integer>();
		this.name = null;
		this.lore = new ArrayList<String>();
		this.enchants = new HashMap<Enchantment, Integer>();
		this.mat = mat;
		this.amount = amount;
		this.data = data;
	}

	public ItemBuilder(Material mat, int amount, String name, List<String> lore) {
		this.data = (short) 0;
		this.lore = new ArrayList<String>();
		this.enchants = new HashMap<Enchantment, Integer>();
		this.mat = mat;
		this.amount = amount;
		this.name = name;
		this.lore = lore;
	}

	public ItemBuilder(Material mat, int amount) {
		this(mat, amount, (short) 0);
	}

	public ItemBuilder addLore(String s) {
		lore.add(s);
		return this;
	}

	public HashMap<Enchantment, Integer> getEnchants() {
		return enchants;
	}

	public ItemBuilder(Material mat, short data) {
		this(mat, 1, data);
	}

	public ItemBuilder setData(short data) {
		this.data = data;
		return this;
	}

	public short getData() {
		return data;
	}

	public ItemBuilder addEnchant(Enchantment enchant, int i) {
		enchants.put(enchant, i);
		return this;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public ItemBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public Material getMat() {
		return mat;
	}

	public int getAmount() {
		return amount;
	}

	public ItemBuilder addEnchantment(Enchantment enchant, int level) {
		if (enchants.containsKey(enchant)) {
			enchants.remove(enchant);
		}
		enchants.put(enchant, level);
		return this;
	}

	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchants) {
		if (enchants.isEmpty()) {
			return this;
		}
		enchants.putAll(enchants);
		return this;
	}

	public ItemStack build() {
		ItemStack is = new ItemStack(mat, amount, data);
		ItemMeta im = is.getItemMeta();
		if (lore != null && !lore.isEmpty()) {
			im.setLore(Util.fixColor(lore));
		}
		if (name != null) {
			im.setDisplayName(Util.fixColor(name));
		}
		for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
			is.addUnsafeEnchantment(e.getKey(), e.getValue());
		}
		is.setItemMeta(im);
		return is;
	}

	public ItemBuilder setLore(List<String> lore) {
		if (lore.isEmpty()) {
			return this;
		}
		lore = Util.fixColor(lore);
		return this;
	}

	public ItemBuilder setName(String name) {
		if (name == null) {
			return this;
		}
		name = Util.fixColor(name);
		return this;
	}

	public void setEnchants(HashMap<Enchantment, Integer> enchants) {
		this.enchants = enchants;
	}
}
