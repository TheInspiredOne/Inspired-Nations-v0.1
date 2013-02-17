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

import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.Country.Country;

public class JoinNewNation extends StringPrompt {

	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerMethods PMI;
	Vector<String> input = new Vector<String>();
	Vector<Country> newCountry = new Vector<Country>();
	int error;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public JoinNewNation(InspiredNations instance, Player playertemp, Vector<Country> newCountries, int errortemp) {
		plugin = instance;
		player = playertemp;
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		newCountry = newCountries;
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
	}
	public JoinNewNation(InspiredNations instance, Player playertemp, Vector<Country> newCountries, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		newCountry = newCountries;
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
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
	
	// A method to find a country given an incomplete string;
	public Vector<String> findCountry(String name) {
		
		Vector<String> list = new Vector<String>();
		for (Country country: newCountry) {
			String nametest = country.getName();
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName())) {
				if (PDI.getIsCountryResident()) {
					if (!PDI.getCountryResides().getName().equalsIgnoreCase(nametest));
					list.add(nametest);
				}
				else list.add(nametest);

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
		for (String i : words) {
			result = result.concat(i + ", ");
		}
		return result.substring(0, result.length() - 2);
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Join New Nation: Type the nation from the list you would like to join." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That country is not an option.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(names) + ". ");
		}
		input.add("back");
		for (int i = 0; i < newCountry.size(); i++) {
			int length = (int) (newCountry.get(i).getName().length() * 1.4);
			if (PDI.getIsCountryResident()) {
				if (!PDI.getCountryResides().getName().equals(newCountry.get(i).getName())) {
					options = options.concat(newCountry.get(i).getName() + repeat(" ", 79 - length));
					input.add(newCountry.get(i).getName().toLowerCase());
				}
			}
			else {
				options = options.concat(newCountry.get(i).getName() + repeat(" ", 79 - length));
				input.add(newCountry.get(i).getName().toLowerCase());
			}
		}
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		
		Vector<String> countrynames = findCountry(arg);
		// Back
		if (arg.equalsIgnoreCase("back")) return new ManageCitizenship(plugin, player, 0);
		
		if (countrynames.size() == 1) {
			arg = countrynames.get(0);
			boolean sucess = PMI.transferCountry(plugin.countrydata.get(arg.toLowerCase()));
			if (!sucess) return new JoinNewNation(plugin, player, newCountry, 0);
			else if (sucess) return new ManageCitizenship(plugin, player, 0);
		}
		else if (countrynames.size() == 0) return new JoinNewNation(plugin, player, newCountry, 2);
		else return new JoinNewNation(plugin, player, newCountry, 3, countrynames);
		
		


		return null;
	}
}
