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
package com.github.InspiredOne.InspiredNations.Hud;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ClaimTownLand extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	Country country;
	Town town;
	TownMethods TMI;
	Vector<String> input = new Vector<String>();
	ItemStack item;
	int error;
	
	// Constructor
	public ClaimTownLand(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryResides();
		town = PDI.getTownMayored();
		TMI = new TownMethods(plugin, town);
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
		error = errortemp;
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
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		
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
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Chunks. Cost: " + ChatColor.GOLD + expenditures + ChatColor.YELLOW + " " + country.getPluralMoney() + " per year. " 
		+ ChatColor.DARK_AQUA + repeat("-", 34 - sizeLength - expendituresLength - (int) (country.getPluralMoney().length()*1.4)) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}

		if (!PM.townSelect()) {
			options = options.concat("To start claiming land, type 'begin'." + repeat(" ", 1));
			input.add("begin");
		}
		else {
			options = options.concat("To stop claiming land, type 'stop'. " + repeat(" ", 18));
			input.add("stop");
		}		
		String map = "" + ChatColor.DARK_AQUA + repeat("-", 53);
		Location spot = player.getLocation();
		Point chunk = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
		Chunks area = country.getArea();
		
		boolean owntown = false;
		boolean othertown = false;
		
		input.add("back");
		
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
		return space + main + options + map + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new ClaimTownLand(plugin, player, 1);
		
		// begin
		if (arg.equalsIgnoreCase("begin")) {
			PM.town(true);
			return new ClaimTownLand(plugin, player, 0);
		}
		
		// stop
		if (arg.equalsIgnoreCase("stop")) {
			PM.town(false);
			return new ClaimTownLand(plugin, player, 0);
		}
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			PM.preTown(false);
			PM.town(false);
			return new ManageTown(plugin, player, 0);
		}
		return new ClaimTownLand(plugin, player, 1);
	}
}
