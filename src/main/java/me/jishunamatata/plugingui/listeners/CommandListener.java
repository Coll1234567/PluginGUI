package me.jishunamatata.plugingui.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("/plugins") || event.getMessage().equalsIgnoreCase("/pl")) {
			event.setMessage("/plugingui:plugins");
		}
	}

}
