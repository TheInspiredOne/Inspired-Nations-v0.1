package com.github.InspiredOne.InspiredNations.Town;

import java.awt.Point;
import java.math.BigDecimal;
import java.util.Iterator;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Country.CountryMethods;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;

public class UnclaimTownLandPlayerListener {
	
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	TownMethods TMI;
	Town town;
	
	public UnclaimTownLandPlayerListener(InspiredNations instance, PlayerMoveEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		TMI = new TownMethods(plugin, PDI.getTownMayored());
		town = PDI.getTownMayored();
	}
	
	public void onPlayerMove() {
		if (!PDI.getIsTownMayor()) return;
		Chunks area = town.getRegion();
		Location spot = player.getLocation();
		boolean aloud = false;
		Point tile = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
	
		// Deselect Town
		if (!player.isConversing()) {
			PM.predetown(false);
			PM.detown(false);
			return;
		}
		if (!PM.preDeselectTown()) return;
		player.sendRawMessage(generateMap(town, player));
		if (!PM.townDeselect()) return;
		if ((!area.isIn(spot) || !chunkAdjacent(area, spot, tile)) && area.Chunks.size() != 0) return;
		else aloud = true;
		if (aloud) {
			area.removeChunk(tile);
			town.setArea(area);
			town.removeCutOutRegions();
			player.sendRawMessage(generateMap(town, player));
		}
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
	
	// Build the map for the PlayerListener
	public String generateMap(Town town, Player player) {

		int size = town.getRegion().Chunks.size();
		BigDecimal expenditures = TMI.getTaxAmount();
		
		int sizeLength = (int) ((size + "").length() * 1);
		int expendituresLength = (int) (expenditures.toString().length() * 1);
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Unclaim Land: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Chunks. Cost: " + ChatColor.GOLD + expenditures + ChatColor.YELLOW + " " + town.getPluralMoney() + " per year. " + ChatColor.DARK_AQUA + repeat("-", 34 - sizeLength - expendituresLength - (int) (town.getPluralMoney().length()*1.4)) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back.";

		if (!PM.townDeselect()) {
			options = options.concat("To start unclaiming land, type 'begin'." + repeat(" ", 1));
		}
		else {
			options = options.concat("To stop unclaiming land, type 'stop'. " + repeat(" ", 18));
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
