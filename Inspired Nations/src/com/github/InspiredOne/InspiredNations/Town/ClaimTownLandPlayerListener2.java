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
package com.github.InspiredOne.InspiredNations.Town;

import java.awt.Point;
import java.math.BigDecimal;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;
import com.github.InspiredOne.InspiredNations.Regions.Point3D;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;


public class ClaimTownLandPlayerListener2{
	
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	PlayerMoveEvent moveEvent;
	PlayerInteractEvent interactEvent;
	ItemStack item;
	
	public ClaimTownLandPlayerListener2(InspiredNations instance,PlayerMoveEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		moveEvent = event;
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
	}
	public ClaimTownLandPlayerListener2(InspiredNations instance,PlayerInteractEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		interactEvent = event;
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
	}

	public void onPlayerInteract() {
		if (PM.getAloudSelect()) {
			if (!PDI.getIsTownMayor()) return;
			if (!PM.townSelect()) return;
			if (!interactEvent.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
			if (!interactEvent.hasItem()) return;
			if (interactEvent.getItem().getTypeId() != item.getTypeId()) return;
			Country country = PDI.getCountryResides();
			polygonPrism prism = PM.getPolygon();
			Location spot = interactEvent.getClickedBlock().getLocation();
			Point3D spot1 = new Point3D(spot.getWorld().getName(), spot.getBlockX(), spot.getBlockY(), spot.getBlockZ());
			Point3D spot2;
			Point3D spot3;
			Point3D spot4;
			Point3D spot5;
			if (!country.isIn(spot)) return;
			prism.addVertex(new Point(spot.getBlockX(), spot.getBlockZ()));
			
			// Make the bedrock marker
			PM.addBlock(spot1, spot.getBlock());
			if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
				spot.getBlock().setTypeId(7);
			}
			
			spot.setY(spot.getY() + 1);
			spot2 = new Point3D(spot1.getWorld(), spot1.x, spot1.y + 1, spot1.z);
			if (player.getLocation().getBlockX() != spot.getBlockX() || player.getLocation().getBlockZ() != spot.getBlockZ()) {
				PM.addBlock(spot2, spot.getBlock());
				if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
					spot.getBlock().setTypeId(7);
				}
			}
			spot.setY(spot.getY() + 1);
			spot3 = new Point3D(spot2.getWorld(), spot2.x, spot2.y + 1, spot2.z);
			if (player.getLocation().getBlockX() != spot.getBlockX() || player.getLocation().getBlockZ() != spot.getBlockZ()) {
				PM.addBlock(spot3, spot.getBlock());
				if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
					spot.getBlock().setTypeId(7);
				}
			}
			spot.setY(spot.getY() + 1);
			spot4 = new Point3D(spot3.getWorld(), spot3.x, spot3.y + 1, spot3.z);
			PM.addBlock(spot4,  spot.getBlock());
			if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
				spot.getBlock().setTypeId(7);
			}
			spot.setY(spot.getY() + 1);
			spot5 = new Point3D(spot4.getWorld(), spot4.x, spot4.y + 1, spot4.z);
			PM.addBlock(spot5, spot.getBlock());
			if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
				spot.getBlock().setTypeId(7);
			}
			
			
			// Delays the clicks
			PM.setAloudSelect(false);
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
					public void run() {
						PM.setAloudSelect(true);
					}
			}, 10);
			

			player.sendRawMessage(generateMap(PDI.getTownMayored(), player));
		}
		interactEvent.setCancelled(true);
	}
	
	
	public void onPlayerMove() {
		if (!PDI.getIsTownMayor()) return;
		if (!PM.townSelect()) return;
		player.sendRawMessage(generateMap(PDI.getTownMayored(), player));
	}
	
	// Build the map for the PlayerListener
	public String generateMap(Town town, Player player) {
		
		String[] namesplit = item.getType().name().split("_");
		String name = "";
		for (int i = 0; i < namesplit.length; i++) {
			name = name.concat(namesplit[i] + " ");
		}
		
		int size = PM.getPolygon().Area();
		BigDecimal expenditures = cut(new BigDecimal(PM.getPolygon().Area() * town.getNationTax() / 100));
		
		int sizeLength = (int) ((size + "").length() * 1);
		int expendituresLength = (int) (expenditures.toString().length() * 1);
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Claim Land: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Square Meters. Cost: " + ChatColor.GOLD + expenditures + ChatColor.YELLOW + " " + town.getPluralMoney() + " per year. " + ChatColor.DARK_AQUA + repeat("-", 26 - sizeLength - expendituresLength - (int) (town.getPluralMoney().length()*1.4)) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back.";

		options = options.concat("Use a " + name.toLowerCase() + "to select the corners of your town. Type 'Finish' when you are done or 'Cancel' to abandon. ");
		
		Country country = PDI.getCountryResides();
		String map = "" + ChatColor.DARK_AQUA + repeat("-", 53);
		Location spot = player.getLocation();
		Point chunk = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
		Chunks area = country.getArea();

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
		
		map = map.concat(ChatColor.GREEN + " +" + ChatColor.YELLOW + " = Your Country." + ChatColor.RED + " +" + ChatColor.YELLOW + " = Others." + ChatColor.GRAY + " /" + ChatColor.YELLOW + " = Unclaimed Land. @ = You.     ");
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
