package com.github.InspiredOne.InspiredNations.Country;

import java.awt.Point;
import java.math.BigDecimal;
import java.util.Iterator;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class UnclaimCountryLandPlayerListener{
	
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	CountryMethods countryMethods;
	
	public UnclaimCountryLandPlayerListener(InspiredNations instance, PlayerMoveEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		countryMethods = new CountryMethods(plugin, PDI.getCountryRuled());
	}

	public void onPlayerMove() {
		if (!PDI.getIsCountryRuler()) return;
		Country country = PDI.getCountryRuled();
		Chunks area = country.getArea();
		Location spot = player.getLocation();
		boolean aloud = false;
		Point tile = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
	
		// Deselect Country
		if (!player.isConversing()) {
			PM.predecountry(false);
			PM.decountry(false);
			return;
		}
		if (!PM.preDeselectCountry()) return;
		player.sendRawMessage(generateMap(country, player));
		if (!PM.countryDeselect()) return;
		if ((!area.isIn(spot) || !chunkAdjacent(area, spot, tile)) && area.Chunks.size() != 0) return;
		else aloud = true;
		if (aloud) {
			area.removeChunk(tile);
			country.setArea(area);
			plugin.chunks.remove(tile);

			
			// Check towns to see if any of them got cut out
			for (Iterator<Town> i = country.getTowns().iterator(); i.hasNext();) {
				Town town = i.next();
				if (town.getRegion().isIn(tile, spot.getWorld().getName())) {
					town.getRegion().removeChunk(tile);
					town.removeCutOutRegions();
				}
			}
			
			for(Player playertarget:plugin.getServer().getOnlinePlayers()) {
				PlayerMethods PM = new PlayerMethods(plugin, playertarget);
				PM.resetLocationBooleans();
			}
			player.sendRawMessage(generateMap(country, player));
		}
	}

	// Build the map for the PlayerListener
	public String generateMap(Country country, Player player) {
	int sizeLength = (int) ((country.size() + "").length()*1.4);
	BigDecimal expenditures = cut(countryMethods.getTaxAmount());
	int expendituresLength = (int) (("" + expenditures).length() * 1.4);

	int maxClaimableChunks = countryMethods.getMaxClaimableChunks();
	int maxChunksLength = (int) ((maxClaimableChunks + "").length() * 1.4);

	
	String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
	String main = ChatColor.BOLD + "Unclaim Land: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
	String options = "";
	String end = ChatColor.GOLD + "" + country.size() + " / " + maxClaimableChunks + ChatColor.YELLOW + " Chunks Claimed.  Cost: " + ChatColor.GOLD + expenditures + ChatColor.YELLOW + " " + country.getPluralMoney() + " per year. " + ChatColor.DARK_AQUA + repeat("-", 25 - sizeLength - expendituresLength - maxChunksLength - (int) (country.getPluralMoney().length()*1.4)) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back.";
	
	String map = "" + ChatColor.DARK_AQUA + repeat("-", 53);
	Location spot = player.getLocation();
	Point chunk = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
	Chunks area = country.getArea();
	String playername = player.getName().toLowerCase();
	PlayerModes PM = plugin.playermodes.get(playername);
	

	for (int z = -9; z < 4; z++) {
		for (int x = -26; x < 27; x++) {
			Point test = new Point(chunk.x + x, chunk.y + z);
			if (z == 0 && x == 0) {
				if (plugin.chunks.containsKey(chunk) && !area.isIn(chunk, spot.getWorld().getName())) {
					if (plugin.countrydata.get(plugin.chunks.get(chunk)).getArea().getWorld().equals(spot.getWorld().getName())) {
						map = map.concat(ChatColor.RED + "@");
					}
					else {
						map = map.concat(ChatColor.GRAY + "@");
					}
				}
				else if (area.isIn(chunk, spot.getWorld().getName())) {
					map = map.concat(ChatColor.GREEN + "@");
				}
				else {
					map = map.concat(ChatColor.GRAY + "@");
				}
			}
			if (z != 0 || x != 0) {
				if (area.isIn(test, spot.getWorld().getName())) {
					map = map.concat(ChatColor.GREEN + "+");
				}
				else if (!area.isIn(test, spot.getWorld().getName()) && plugin.chunks.containsKey(test)) {
					if (plugin.countrydata.get(plugin.chunks.get(chunk)).getArea().getWorld().equals(spot.getWorld().getName())) {
						map = map.concat(ChatColor.RED + "+");
					}
					else {
						map = map.concat(ChatColor.GRAY + "/");
					}
				}
				else {
					map = map.concat(ChatColor.GRAY + "/");
				}
			}
		}
	}
	
	// Direction Icon
	if ((-45 < spot.getYaw() && 45 >= spot.getYaw()) || (315 < spot.getYaw() && 360 >= spot.getYaw())
			|| (-360 < spot.getYaw() && -315 >= spot.getYaw())) {
		map = map.substring(0, 1725).concat("`|`").concat(map.substring(1726));
	}
	if ((45 < spot.getYaw() && 135 >= spot.getYaw()) || (-315 < spot.getYaw() && -225 >= spot.getYaw())) {
		map = map.substring(0, 1563).concat("-").concat(map.substring(1564));
	} 
	if ((135 < spot.getYaw() && 225 >= spot.getYaw()) || (-225 < spot.getYaw() && -135 >= spot.getYaw())) {
		map = map.substring(0, 1407).concat(",|,").concat(map.substring(1408));

	}
	if ((225 < spot.getYaw() && 315 >= spot.getYaw()) || (-135 < spot.getYaw() && -45 >= spot.getYaw())) {
		map = map.substring(0, 1569).concat("-").concat(map.substring(1570));

	}
	
	map = map.concat(ChatColor.GREEN + " +" + ChatColor.YELLOW + " = Your Land." + ChatColor.RED + " +" + ChatColor.YELLOW + " = Other Land." + ChatColor.GRAY + " /" + ChatColor.YELLOW + " = Unclaimed Land. @ = You.    ");
	
	if (!PM.countryDeselect()) {
		options = options.concat("To start unclaiming land, type 'begin'." + repeat(" ", 1));
	}
	else {
		options = options.concat("To stop unclaiming land, type 'stop'. " + repeat(" ", 17));
	}
	return space + main + options + map + end;
	}
	
	// A method to determine if a chunk is aloud to be removed
	public boolean chunkAdjacent(Chunks area, Location spot, Point tile) {
		boolean aloud = true;
		int in = 0;
		if (area.Area() < 3) return aloud;
		
		Point tile1 = new Point(tile.x, tile.y + 1);
		Point tile1L = new Point(tile.x - 1, tile.y + 1);
		Point tile1R = new Point(tile.x + 1, tile.y + 1);
		Point tile2 = new Point(tile.x + 1, tile.y);
		Point tile2T = new Point(tile.x + 1, tile.y + 1);
		Point tile2B = new Point(tile.x + 1, tile.y - 1);
		Point tile3 = new Point(tile.x, tile.y - 1);
		Point tile3L = new Point(tile.x - 1, tile.y - 1);
		Point tile3R = new Point(tile.x + 1, tile.y - 1);
		Point tile4 = new Point(tile.x - 1, tile.y);
		Point tile4T = new Point(tile.x - 1, tile.y + 1);
		Point tile4B = new Point(tile.x - 1, tile.y - 1);
		if (area.isIn(spot)) {
			if (area.isIn(tile1, spot.getWorld().getName())) {
				if((!area.isIn(tile1R, spot.getWorld().getName()) || !area.isIn(tile2, spot.getWorld().getName())) &&
						(!area.isIn(tile1L, spot.getWorld().getName()) || !area.isIn(tile4, spot.getWorld().getName()))) aloud = false;
				in++;
			}
			if (area.isIn(tile2, spot.getWorld().getName())) {
				if ((!area.isIn(tile2B, spot.getWorld().getName()) || !area.isIn(tile3, spot.getWorld().getName())) && (!area.isIn(tile2T, spot.getWorld().getName())
						|| !area.isIn(tile1, spot.getWorld().getName()))) aloud = false;
				in++;
			}
			if (area.isIn(tile3, spot.getWorld().getName())) {
				if ((!area.isIn(tile3L, spot.getWorld().getName()) || !area.isIn(tile4, spot.getWorld().getName())) && (!area.isIn(tile3R, spot.getWorld().getName())
						|| !area.isIn(tile2, spot.getWorld().getName()))) aloud = false;
				in++;
			}
			if (area.isIn(tile4, spot.getWorld().getName())) {
				if ((!area.isIn(tile4B, spot.getWorld().getName()) || !area.isIn(tile3, spot.getWorld().getName())) && (!area.isIn(tile4T, spot.getWorld().getName())
						|| !area.isIn(tile1, spot.getWorld().getName()))) aloud = false;
				in++;
			}
		}
		if (in == 1) aloud = true;
		return aloud;
	}
	
	// A method to simply repeat a string
	public String repeat(String entry, int multiple) {
		String temp = "";
		for (int i = 0; i < multiple; i++) {
			temp = temp.concat(entry);
		}
		return temp;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public double cut(double x) {
		int y;
		y = (int) (x*100);
		return y/100.0;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
	}
}
