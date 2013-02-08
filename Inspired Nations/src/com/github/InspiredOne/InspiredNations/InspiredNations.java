package com.github.InspiredOne.InspiredNations;

import java.awt.Point;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.InspiredOne.InspiredNations.Country.Country;

public class InspiredNations extends JavaPlugin{

	//
	public Logger logger = Logger.getLogger("Minecraft");
	private StartStop SS = new StartStop(this);
	public HashMap<String, PlayerData> playerdata;
	public HashMap<String, Country> countrydata;
	public HashMap<String, PlayerModes> playermodes;
	public HashMap<Point, String> chunks;
	public InspiredNationsCommandExecutor InspiredNationsCE = new InspiredNationsCommandExecutor(this);
	public InspiredNationsPlayerListener InspiredNationsPL = new InspiredNationsPlayerListener(this);
	
	
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(InspiredNationsPL, this);
		SS.Start();
	}
	
	public void onDisable() {
		SS.Stop();
	}
}
