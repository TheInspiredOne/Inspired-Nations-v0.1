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
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Country.CountryMethods;

public class CountryProtectionLevel extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Vector<String> input = new Vector<String>();
	int error;
	Vector<String> suggestions;
	CountryMethods countryMethods;
	
	// Constructor
	public CountryProtectionLevel(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = plugin.countrydata.get(PDI.getCountryRuled().getName().toLowerCase());
		error = errortemp;
		countryMethods = new CountryMethods(plugin, country);
	}
	public CountryProtectionLevel(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		suggestions = namestemp;
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
		String main = ChatColor.BOLD + "Protection Levels: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		input.add("back");
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Only numbers are aloud as the argument. Do not use letters.");
		}
		
		options = options.concat(ChatColor.YELLOW + "Current Protection Level: " + ChatColor.GOLD + country.getProtectionLevel() + ChatColor.YELLOW + repeat(" ", 44));
		options = options.concat(ChatColor.YELLOW + "New Protection Level: " + ChatColor.GOLD + country.getFutureProtectionLevel() + ChatColor.GRAY + " Applied after next tax cycle." + repeat(" ", 10));
		options = options.concat(ChatColor.YELLOW + "Current Military Funding: " + ChatColor.GOLD + cut(countryMethods.getTaxAmount()).toString() + repeat(" ", (int) (47 - Math.round((cut(countryMethods.getTaxAmount()).toString().length() * 1.4)))));
		options = options.concat(ChatColor.YELLOW + "New Military Funding: " + ChatColor.GOLD + cut(countryMethods.getTaxAmount(country.getFutureProtectionLevel())).toString() + 
				ChatColor.GRAY + " Applied after next tax cycle. " + repeat(" ", (int) (15 - Math.round((cut(countryMethods.getTaxAmount(country.getFutureProtectionLevel())).toString().length() * 1.4)))));
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.GOLD + "Level 0: " + ChatColor.YELLOW + "(No protection) Any entity can claim or build on your country's land." + repeat(" ", 60));
		options = options.concat(ChatColor.GOLD + "Level 1: " + ChatColor.YELLOW + "(Claim Protection) Country land is protected from being claimed." + repeat(" ", 60));
		options = options.concat(ChatColor.GOLD + "Level 2: " + ChatColor.YELLOW + "(Immigration Control) Players need permission to join." + repeat(" ", 1));
		options = options.concat(ChatColor.GOLD + "Level 3: " + ChatColor.YELLOW + "(Block and Interactable Protection) Only country residents can build and interact in country." + repeat(" ", 20));
		options = options.concat(ChatColor.GOLD + "Level 4: " + ChatColor.YELLOW + "(Player Protection) Players are protected from attacks while within country boundary." + repeat(" ", 5));
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		
		options = options.concat("Set <level number>" + repeat(" ", 55));
		input.add("set");
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new CountryProtectionLevel(plugin, player, 1);
		
		// back
		if (arg.equalsIgnoreCase("back")) return new ManageCountry(plugin, player, 0);
		
		// set
		if (args[0].equalsIgnoreCase("set")) {
			try {
				int level = java.lang.Integer.parseInt(args[1]);
				country.setFutureProtectionLevel(Math.abs(level));
				return new CountryProtectionLevel(plugin, player, 0);
			}
			catch (Exception ex) {
				return new CountryProtectionLevel(plugin, player, 2);
			}
		}
		
		return new CountryProtectionLevel(plugin, player, 1);
	}

}
