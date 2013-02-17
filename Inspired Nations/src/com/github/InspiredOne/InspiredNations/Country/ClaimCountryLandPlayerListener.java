/*******************************************************************************
 * Copyright (c) 2013 InspiredOne.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     InspiredOne - initial API and implementation
 ******************************************************************************/
package com.github.InspiredOne.InspiredNations.Country;

import java.awt.Point;
import java.math.BigDecimal;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;

public class ClaimCountryLandPlayerListener{
	
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	CountryMethods countryMethods;
	
	public ClaimCountryLandPlayerListener(InspiredNations instance, PlayerMoveEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		countryMethods = new CountryMethods(plugin, PDI.getCountryRuled());
	}

	public void onPlayerMove() {
		if (!PDI.getIsCountryRuler()) return;
		if (!PM.preCountrySelect()) return;
		Country country = PDI.getCountryRuled();
		Chunks area = country.getArea();
		Location spot = player.getLocation();
		boolean aloud = false;
		Point tile = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
		double costperchunk;
		if (plugin.getConfig().getDouble("base_cost_per_chunk") == 0) {
			costperchunk = .0001;
		}
		else {
			costperchunk = plugin.getConfig().getDouble("base_cost_per_chunk");
		}
		int maxClaimableChunks = country.getMaxLoan().subtract(country.getLoanAmount()).add(country.getMoney().add(country.getRevenue())).divide((new BigDecimal(costperchunk).multiply(country.getMoneyMultiplyer())), 0, BigDecimal.ROUND_DOWN).toBigInteger().intValue();
		
		// Select Country

		player.sendRawMessage(generateMap(country, player));
		if (!PM.countrySelect()) return;
		if (area.Chunks.size() > maxClaimableChunks) return;
		if (area.isIn(spot)) return;
		if (!area.isIn(spot) && plugin.chunks.containsKey(tile)){
			if (plugin.countrydata.get(plugin.chunks.get(tile)).getProtectionLevel() != 0) {
				return;	
			}
		}
		spot.setX(spot.getX() + 16);
		if (area.isIn(spot)) aloud = true;
		spot.setX(spot.getX() - 32);
		if (area.isIn(spot)) aloud = true;
		spot.setX(spot.getX() + 16);
		spot.setZ(spot.getZ() + 16);
		if (area.isIn(spot)) aloud = true;
		spot.setZ(spot.getZ() - 32);
		if (area.isIn(spot)) aloud = true;
		spot.setZ(spot.getZ() + 16);
		if (area.Area() == 0) {
			aloud = true;
			area.setWorld(spot.getWorld().getName());
		}
		
		if (aloud) {
			area.addChunk(tile);
			if (!area.isIn(spot) && plugin.chunks.containsKey(tile)){
				plugin.countrydata.get(plugin.chunks.get(tile)).getArea().removeChunk(tile);
			}
			country.setArea(area);
			plugin.chunks.put(tile, country.getName().toLowerCase());
			player.sendRawMessage(generateMap(country, player));
			for(Player playertarget:plugin.getServer().getOnlinePlayers()) {
				PlayerMethods PM = new PlayerMethods(plugin, playertarget);
				PM.resetLocationBooleans();
			}
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
	String main = ChatColor.BOLD + "Claim Land: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
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
					if (plugin.countrydata.get(plugin.chunks.get(test)).getArea().getWorld().equals(spot.getWorld().getName())) {
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
	
	if (!PM.countrySelect()) {
		options = options.concat("To start claiming land, type 'begin'." + repeat(" ", 1));
	}
	else {
		options = options.concat("To stop claiming land, type 'stop'. " + repeat(" ", 18));
	}
	return space + main + options + map + end;
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
