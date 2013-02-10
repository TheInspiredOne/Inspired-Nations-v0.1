package com.github.InspiredOne.InspiredNations;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class StartStop {

	// Grabbing the instance of the plugin.
	InspiredNations plugin;
	SaveFiles SF;
	public StartStop(InspiredNations instance) {
		plugin = instance;
		SF = new SaveFiles(plugin);

	}
	
	public void Start() {
		PluginDescriptionFile pdf = plugin.getDescription();
		plugin.getLogger().info(pdf.getName() + " version " + pdf.getVersion() + " has been enabled.");
		
		// Handles Commands
		plugin.getCommand("hud").setExecutor(plugin.InspiredNationsCE);
		
		// Handles config.yml
		FileConfiguration config = plugin.getConfig(); 
		config.options().copyDefaults(true);
		if (!(new File(plugin.getDataFolder() + config.getName()).exists())) {
			plugin.saveDefaultConfig();
		}
		
		// Handles Stored Data
		SF.loadDataFile();
		plugin.playermodes = new HashMap<String, PlayerModes>();
		try {
			Player[] online = plugin.getServer().getOnlinePlayers();
			for (int i = 0; i < online.length; i++) {
				plugin.playermodes.put(online[i].getName().toLowerCase(), new PlayerModes(plugin));
			}
		}
		catch (Exception ex) {
			
		}
	}
	
	public void Stop() {
		try {
			Player[] online = plugin.getServer().getOnlinePlayers();
			for (int i = 0; i < online.length; i++) {
				plugin.playermodes.get(online[i].getName().toLowerCase()).setBlocksBack();
				if (online[i].isConversing()) {
					String name = online[i].getName().toLowerCase();
					online[i].abandonConversation(plugin.playerdata.get(name).getConversation());
				}
			}
		}
		catch (Exception ex) {	
		}
		PluginDescriptionFile pdf = plugin.getDescription();
		plugin.getLogger().info(pdf.getName() + " version " + pdf.getVersion() + " has been disabled.");
		SF.saveDataFile();
	}
}
