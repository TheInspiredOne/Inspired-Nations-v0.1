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

import java.math.BigDecimal;
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ReclaimHouse1 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	Country country;
	Town town;
	TownMethods TMI;
	Vector<String> input = new Vector<String>();
	int error;
	House house;
	
	// Constructor
	public ReclaimHouse1(InspiredNations instance, Player playertemp, int errortemp, House housetemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryResides();
		town = PDI.getTownResides();
		error = errortemp;
		house = housetemp;
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
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Reclaim House: Pick your selection type." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		input.add("back");
		
		options = options.concat("What kind of selection would you like to make?" + repeat(" ", 22) + "A " + ChatColor.GOLD + "cuboid" + ChatColor.YELLOW + " is a perfect rectangular prism requiring you" +
				" to select two opposite corners of the 'box'." + repeat(" ", 29) + "A " + ChatColor.GOLD + "polygon" + ChatColor.YELLOW + " is a shape with as many corners as you want. The highest and lowest corners" +
				" dictate where the top and bottom of your selection is. ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat("Cuboid" + repeat(" ", 67));
		options = options.concat("Polygon" + repeat(" ", 60));
		input.add("cuboid");
		input.add("polygon");

		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new ReclaimHouse1(plugin, player, 1, house);
		PM.clear();
		// Cuboid
		if (arg.equalsIgnoreCase("cuboid")) {
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.selectCuboid(true);
			PM.selectPolygon(false);
		}
		
		// Polygon
		if (arg.equalsIgnoreCase("polygon")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.selectPolygon(true);
			PM.selectCuboid(false);
		}
		// Back
		if (arg.equalsIgnoreCase("back")) {
			PM.house(false);
			PM.reSelectHouse = false;
			return new ManageHouse2(plugin, player, 0, house.getName());
		}
		return new ReclaimHouse2(plugin,player,0, house);
	}

}
