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
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManagePark1 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public ManagePark1(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
	}
	public ManagePark1(InspiredNations instance, Player playertemp, int errortemp, Vector<String> parktemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		names = parktemp;
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
	
	// A method to find a park given an incomplete string;
	public Vector<String> find(String name) {
		Vector<String> list = new Vector<String>();
		Vector<Park> park = town.getParks();

		for (Park i: park) {
			String nametest = i.getName();
			if (nametest.toLowerCase().contains(name.toLowerCase())) {
				list.add(nametest);
				if (nametest.equalsIgnoreCase(name)) {
					list.clear();
					list.add(nametest);
					return list;
				}
			}
		}
		return list;
	}
	
	// A method to take a Vector<String> and return all the elements as a formated String.
	public String format(Vector<String> words) {
		String result = "";
		if (words.size() == 0) return "";
		for (String i : words) {
			result = result.concat(i + ", ");
		}
		return result.substring(0, result.length() - 2);
	}
	

	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Park: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(names) + ".");
		}
		options = options.concat(ChatColor.YELLOW + "Select the park you would like to manage." + repeat(" ", 20) + ChatColor.DARK_AQUA + repeat("-", 53));
		options = options.concat(ChatColor.GREEN + "");
		for (Park i: town.getParks()) {
			options = options.concat(i.getName() + ", ");
		}
		options = options.substring(0, options.length()-2).concat(" ");
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		// Back
		if (arg.equalsIgnoreCase("back")) {
			return new TownGovernmentRegions(plugin, player, 0);
		}
		
		// Name of Park
		if (find(arg).size() == 1) {
			arg = find(arg).get(0);
			return new ManagePark2(plugin, player, 0, arg);
		}
		else if(find(arg).isEmpty()) {
			return new ManagePark1(plugin, player, 1);
		}
		else return new ManagePark1(plugin, player, 2, find(arg));
	}
}
