package me.jishunamatata.plugingui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishunamatata.plugingui.commands.PluginCommand;
import me.jishunamatata.plugingui.commands.PluginGUICommand;
import me.jishunamatata.plugingui.gui.CustomInventoryManager;
import me.jishunamatata.plugingui.listeners.CommandListener;

public class PluginGUI extends JavaPlugin {
	
	private ConfigManager configManager;

	@Override
	public void onEnable() {
		configManager = new ConfigManager(this);
		
		Bukkit.getPluginManager().registerEvents(new CustomInventoryManager(), this);
		Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
		
		getCommand("plugins").setExecutor(new PluginCommand(this));
		getCommand("plugingui").setExecutor(new PluginGUICommand(configManager));
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

}
