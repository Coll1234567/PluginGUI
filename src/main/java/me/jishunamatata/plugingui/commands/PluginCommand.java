package me.jishunamatata.plugingui.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import me.jishunamatata.plugingui.ItemBuilder;
import me.jishunamatata.plugingui.PluginGUI;
import me.jishunamatata.plugingui.gui.CustomInventory;
import me.jishunamatata.plugingui.gui.CustomInventoryManager;
import me.jishunamatata.plugingui.utils.Utils;

public class PluginCommand implements CommandExecutor {
	private final PluginGUI plugin;
	private final NamespacedKey indexkey;
	private final List<Plugin> plugins;

	public PluginCommand(PluginGUI plugin) {
		this.plugin = plugin;
		this.indexkey = new NamespacedKey(plugin, "index");
		this.plugins = Arrays.asList(Bukkit.getPluginManager().getPlugins());
		this.plugins.sort((pluginA, pluginB) ->pluginA.getName().compareTo(pluginB.getName()));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(getMessage("not-player"));
			return true;
		}
		
		if (!sender.hasPermission("plugingui.plugins")) {
			sender.sendMessage(getMessage("no-permission"));
			return true;
		}

		showPluginGUI((HumanEntity) sender, 0);
		return true;
	}

	private String getMessage(String key) {
		return plugin.getConfigManager().getMessage(key);
	}

	private void showPluginGUI(HumanEntity player, int start) {
		CustomInventory inventory = new CustomInventory(null, 54, getMessage("inventory-name"));
		inventory.addClickConsumer(event -> event.setCancelled(true));

		for (int i = 0; i < Math.min(45, this.plugins.size() - start); i++) {
			Plugin plugin = this.plugins.get(start + i);
			PluginDescriptionFile desc = plugin.getDescription();

			boolean enabled = plugin.isEnabled();
			String name = desc.getName();
			String version = desc.getVersion();
			String website = desc.getWebsite();
			List<String> authors = desc.getAuthors();
			String description = desc.getDescription();

			ItemBuilder builder = new ItemBuilder(enabled
					? Material.getMaterial(this.plugin.getConfigManager().getConfig().getString("enabled-plugin-item"))
					: Material
							.getMaterial(this.plugin.getConfigManager().getConfig().getString("disabled-plugin-item")));

			builder.withName(enabled ? getMessage("plugins.name").replace("%name%", name)
					: getMessage("plugins.disabled-name").replace("%name%", name));
			builder.addLore(getMessage("plugins.version").replace("%version%", version));

			if (authors != null && !authors.isEmpty()) {
				builder.addLore(Utils.splitString(
						getMessage("plugins.authors").replace("%authors%", String.join(", ", authors)), 45, true));
			}

			if (website != null) {
				builder.addLore(getMessage("plugins.website").replace("%website%", website));
			}

			if (description != null) {
				builder.addLore(Utils
						.splitString(getMessage("plugins.description").replace("%description%", description), 45, true));
			}

			inventory.setItem(i, builder.build());
		}

		ItemStack filler = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).withName(" ").build();

		for (int i = 46; i < 53; i++) {
			inventory.setItem(i, filler);
		}

		if (start > 0) {
			inventory.addButton(45, new ItemBuilder(Material.ARROW).withName(getMessage("previous-page"))
					.withPersistantData(indexkey, start - 45).build(), this::gotoPage);
		} else {
			inventory.setItem(45, filler);
		}

		if (start + 45 < this.plugins.size()) {
			inventory.addButton(53, new ItemBuilder(Material.ARROW).withName(getMessage("next-page"))
					.withPersistantData(indexkey, start + 45).build(), this::gotoPage);
		} else {
			inventory.setItem(53, filler);
		}

		CustomInventoryManager.openGui(player, inventory);
	}

	private void gotoPage(InventoryClickEvent event) {
		PersistentDataContainer container = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
		showPluginGUI(event.getWhoClicked(), container.getOrDefault(indexkey, PersistentDataType.INTEGER, 0));
	}

}
