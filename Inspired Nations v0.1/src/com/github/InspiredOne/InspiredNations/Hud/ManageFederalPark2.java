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
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Park.Park;

public class ManageFederalPark2 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	String name;
	Park park;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public ManageFederalPark2(InspiredNations instance, Player playertemp, int errortemp, String parkname) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = parkname;
		for (Park test: country.getParks()) {
			if (test.getName().equalsIgnoreCase(parkname)) {
				park = test;
				break;
			}
		}
	}
	
	public ManageFederalPark2(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp, String parkname) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = parkname;
		for (Park test: country.getParks()) {
			if (test.getName().equalsIgnoreCase(parkname)) {
				park = test;
				break;
			}
		}
		names = namestemp;
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
	
	// A method to find a person given an incomplete string;
	public Vector<String> find(String name) {
		Set<String> players = plugin.playerdata.keySet();
		OfflinePlayer[] playernames = plugin.getServer().getOfflinePlayers();
		Vector<String> list = new Vector<String>();
		for (Iterator<String> i = players.iterator(); i.hasNext();) {

			String nametest = i.next();
			for (OfflinePlayer person: playernames) {
				if (nametest.equalsIgnoreCase(person.getName())) {
					nametest = person.getName();
				}
			}
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !park.getBuilders().contains(nametest)) {
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
	
	// A generalized find function where you give it the vector you want it to search in.
	public Vector<String> find(String name, Vector<String> test) {
		Vector<String> list = new Vector<String>();
		for (String nametest:test) {
			if (nametest.toLowerCase().contains(name.toLowerCase())) {
				list.add(nametest);
			}
			if (nametest.equalsIgnoreCase(name)) {
				list.clear();
				list.add(nametest);
				return list;
			}
		}
		return list;
	}
	
	
	// A method to take a Vector<String> and return all the elements as a formated String.
	public String format(Vector<String> words) {
		String result = "";
		if (words.size()==0) {
			return "";
		}
		for (String i : words) {
			result = result.concat(i + ", ");
		}
		return result.substring(0, result.length() - 2);
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Park: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Not enough arguments.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a builder.");
		}
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "That park name is already being used in this country.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player does not exist on this server or is already a builder.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(names) + ". ");
		}
		options = options.concat(ChatColor.YELLOW + " " + ChatColor.BOLD + park.getName() + ChatColor.RESET + repeat(" ", 70 - (int)(park.getName().length()*1.4)) + ChatColor.RESET + ChatColor.YELLOW);
		options = options.concat("Builders: " + ChatColor.GOLD + format(park.getBuilders()) + " ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);

		options = options.concat("Add Builder <player>" + repeat(" ", 53));
		input.add("add");
		if (park.getBuilders().size() > 0) {
			options = options.concat("Remove Builder <player>" + repeat( " ", 50));
			input.add("remove");
		}
		options = options.concat("Rename <name>" + repeat(" ", 60));
		options = options.concat("Protection Level" + repeat(" ", 55));
		options = options.concat("Unclaim ");
		input.add("unclaim");
		input.add("rename");
		input.add("protection");
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String tempname = "";
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new ManageFederalPark2(plugin, player, 1, name);

		
		// back
		if (arg.equalsIgnoreCase("back")) {
			if (country.getParks().size() == 1) {
				return new CountryGovernmentRegions(plugin, player, 0);
			}
			else return new ManageFederalPark1(plugin ,player, 0);
		}
		// Unclaim
		if (args[0].equalsIgnoreCase("unclaim")) {
			PDI.getCountryRuled().removePark(park);
			for(Player playertarget:plugin.getServer().getOnlinePlayers()) {
				PlayerMethods PM = new PlayerMethods(plugin, playertarget);
				PM.resetLocationBooleans();
			}
			if (PDI.getCountryRuled().getParks().size() >= 1) {
				for(String name: country.getCoRulers()) {
					if(plugin.getServer().getPlayerExact(name).isConversing() && !name.equalsIgnoreCase(player.getName())) {
						plugin.playerdata.get(name).getConversation().abandon();
					}
				}
				if (plugin.getServer().getPlayerExact(country.getRuler()).isConversing() && !country.getRuler().equalsIgnoreCase(player.getName())) {
					plugin.playerdata.get(country.getRuler()).getConversation().abandon();
				}
				return new ManageFederalPark1(plugin, player, 0);
			}
			else {
				for(String name: country.getCoRulers()) {
					if(plugin.getServer().getPlayerExact(name).isConversing() && !name.equalsIgnoreCase(player.getName())) {
						plugin.playerdata.get(name).getConversation().abandon();
					}
				}
				if (plugin.getServer().getPlayerExact(country.getRuler()).isConversing() && !country.getRuler().equalsIgnoreCase(player.getName())) {
					plugin.playerdata.get(country.getRuler()).getConversation().abandon();
				}
				return new CountryGovernmentRegions(plugin, player, 0);
			}
			
		}
		
		if (args.length < 2) return new ManageFederalPark2(plugin, player, 2, name);
		
		// Add Builder
		if (args[0].equalsIgnoreCase("add")){
			if (find(args[2]).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2]).size() == 0) return new ManageFederalPark2(plugin, player, 6, name);
			else return new ManageFederalPark2(plugin, player, 9, find(args[2]), name);
			
			park.addBuilder(args[2]);
			return new ManageFederalPark2(plugin, player, 0, name);
		}
		
		// Protection Level
		if (args[0].equalsIgnoreCase("protection")) {
			return new GeneralProtectionLevel(plugin, player, 0, "federalpark", park);
		}
		
		// Remove Builder
		if (args[0].equalsIgnoreCase("remove")) {
			if (find(args[2], park.getBuilders()).size() == 1) args[2] = find(args[2], park.getBuilders()).get(0);
			else if (find(args[2], park.getBuilders()).size()== 0) return new ManageFederalPark2(plugin, player, 3, name);
			else return new ManageFederalPark2(plugin, player, 9, find(args[2], park.getBuilders()), name);
			
			park.removeBuilder(args[2]);
			return new ManageFederalPark2(plugin, player, 0, name);
		}
		

		// Rename
		if (args[0].equalsIgnoreCase("rename")) {
			for(int i = 0; i < args.length - 1; i++) {
				tempname = tempname.concat(args[i+1] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			for (Park parktest: country.getParks()) {
				if (parktest.getName().equalsIgnoreCase(tempname) && !parktest.equals(park)) {
					return new ManageFederalPark2(plugin, player, 5, name);
				}
			}
			park.setName(tempname);
			return new ManageFederalPark2(plugin, player, 0, tempname);
		}
		return new ManageFederalPark2(plugin ,player, 0, name);
	}
}
