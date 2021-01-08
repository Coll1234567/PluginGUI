package me.jishunamatata.plugingui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;

public class ConfigManager {
	private final PluginGUI plugin;
	private YamlConfiguration config;

	public ConfigManager(PluginGUI plugin) {
		this.plugin = plugin;
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}

		loadConfig();
	}

	public void loadConfig() {
		File file = copyResource(this.plugin, "config.yml");
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	public void reloadConfig() {
		loadConfig();
	}

	// saveDefaultConfig doesn't copy comments, this will
	private File copyResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		File resourceFile = new File(folder, resource);
		if (!resourceFile.exists()) {
			try {
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResource(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}

			} catch (Exception e) {
				plugin.getLogger().severe("Error copying file " + resource);
			}
		}
		return resourceFile;
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public String getMessage(String key) {
		return ChatColor.translateAlternateColorCodes('&',
				getConfig().getString("messages." + key, "Missing message: messages." + key));
	}
}
