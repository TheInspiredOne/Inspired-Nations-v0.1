package com.github.InspiredOne.InspiredNations.Town;

import java.awt.Point;
import java.math.BigDecimal;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;

public class ClaimTownLandPlayerListener {
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	TownMethods TMI;
	PlayerMoveEvent moveEvent;
	PlayerInteractEvent interactEvent;
	ItemStack item;
	
	public ClaimTownLandPlayerListener(InspiredNations instance,PlayerMoveEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		moveEvent = event;
		TMI = new TownMethods(plugin, PDI.getTownMayored());
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
	}
	
	public void onPlayerMove() {
		if (!PDI.getIsTownMayor()) return;
		if (!PM.preTownSelect()) return;
		Town town = PDI.getTownMayored();
		Country country = PDI.getCountryResides();
		Chunks area = town.getRegion();
		Location spot = player.getLocation();
		boolean aloud = false;
		Point tile = new Point(spot.getChunk().getX(), spot.getChunk().getZ());	
		// Select Town

		player.sendRawMessage(generateMap(town, player));
		if (!PM.townSelect()) return;
		if (area.isIn(spot)) return;
		
		for(Town towntest:country.getTowns()) {
			if(towntest.isIn(spot) && towntest != town) {
				if (towntest.getProtectionLevel() != 0) {
					return;
				}
			}
		}
		if (!country.isIn(spot)) {
			return;
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
		spot = player.getLocation();
		if (aloud) {
			area.addChunk(tile);
			town.setArea(area);
			
			for(Town towntest:country.getTowns()) {
				if(towntest.isIn(spot) && towntest != town) {
					if (towntest.getProtectionLevel() != 0) {
						towntest.getRegion().removeChunk(tile);
					}
				}
			}
			
			player.sendRawMessage(generateMap(town, player));
		}
	}
	
	// Build the map for the PlayerListener
	public String generateMap(Town town, Player player) {
		
		String[] namesplit = item.getType().name().split("_");
		String name = "";
		for (int i = 0; i < namesplit.length; i++) {
			name = name.concat(namesplit[i] + " ");
		}
		
		int size = town.getRegion().Chunks.size();
		BigDecimal expenditures = TMI.getTaxAmount();
		
		int sizeLength = (int) ((size + "").length() * 1);
		int expendituresLength = (int) (expenditures.toString().length() * 1);
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Claim Land: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Chunks. Cost: " + ChatColor.GOLD + expenditures + ChatColor.YELLOW + " " + town.getPluralMoney() + " per year. " + ChatColor.DARK_AQUA + repeat("-", 34 - sizeLength - expendituresLength - (int) (town.getPluralMoney().length()*1.4)) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back.";

		if (!PM.townSelect()) {
			options = options.concat("To start claiming land, type 'begin'." + repeat(" ", 1));
		}
		else {
			options = options.concat("To stop claiming land, type 'stop'. " + repeat(" ", 18));
		}
		
		Country country = PDI.getCountryResides();
		String map = "" + ChatColor.DARK_AQUA + repeat("-", 53);
		Location spot = player.getLocation();
		Point chunk = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
		Chunks area = country.getArea();

		boolean owntown = false;
		boolean othertown = false;
		
		for (int z = -8; z < 4; z++) {
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
						owntown = false;
						othertown = false;
						for (Town towntest: country.getTowns()) {
							if (towntest.isIn(chunk, spot.getWorld().getName())) {
								if (towntest.equals(PDI.getTownMayored())) {
									owntown = true;
								}
								else {
									othertown = true;
								}
							}
						}
						if (owntown) {
							map = map.concat(ChatColor.BLUE + "@");
						}
						else if (othertown) {
							map = map.concat(ChatColor.AQUA + "@");
						}
						else {
							map = map.concat(ChatColor.GREEN + "@");
						}
					}
					else {
						map = map.concat(ChatColor.GRAY + "@");
					}
				}
				if (z != 0 || x != 0) {
					if (area.isIn(test, spot.getWorld().getName())) {
						owntown = false;
						othertown = false;
						for (Town towntest: country.getTowns()) {
							if (towntest.isIn(test, spot.getWorld().getName())) {
								if (towntest.equals(PDI.getTownMayored())) {
									owntown = true;
								}
								else {
									othertown = true;
								}
							}
						}
						if (owntown) {
							map = map.concat(ChatColor.BLUE + "+");
						}
						else if (othertown) {
							map = map.concat(ChatColor.AQUA + "+");
						}
						else {
							map = map.concat(ChatColor.GREEN + "+");
						}
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
			map = map.substring(0, 1566).concat("`|`").concat(map.substring(1567));
		}
		if ((45 < spot.getYaw() && 135 >= spot.getYaw()) || (-315 < spot.getYaw() && -225 >= spot.getYaw())) {
			map = map.substring(0, 1404).concat("-").concat(map.substring(1405));
		} 
		if ((135 < spot.getYaw() && 225 >= spot.getYaw()) || (-225 < spot.getYaw() && -135 >= spot.getYaw())) {
			map = map.substring(0, 1248).concat(",|,").concat(map.substring(1249));

		}
		if ((225 < spot.getYaw() && 315 >= spot.getYaw()) || (-135 < spot.getYaw() && -45 >= spot.getYaw())) {
			map = map.substring(0, 1410).concat("-").concat(map.substring(1411));

		}
		
		map = map.concat(ChatColor.GREEN + "+" + ChatColor.YELLOW + "=Country." + ChatColor.RED + " +" + ChatColor.YELLOW + "=Other Countries."
				+ ChatColor.BLUE + " +" + ChatColor.YELLOW + "=Town." + ChatColor.AQUA + " +" + ChatColor.YELLOW + "=Other Towns. @=You.  ");
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
