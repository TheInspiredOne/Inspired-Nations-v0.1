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

import java.util.Iterator;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Country.Country;

public class NewCountry2 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	String playername;
	Country country;
	int error;
	
	// Constructor
	public NewCountry2(InspiredNations instance, Player playertemp, Country countrytemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		playername = player.getName().toLowerCase();
		PM = plugin.playermodes.get(playername);
		country = countrytemp;
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

	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "New Country Economics: Read the instructions." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = "";
		
		options = options.concat("Type the name for your money." + repeat(" ", 39));
		options = options.concat("Put the singular form first and then the plural form. For example 'coin coins'." + repeat(" ", 45));
		// build the error message
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That money name is already taken. Please try something else.");
		}
		else if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "You need to enter both the sigular and plural form of your money separted by a space.");
		}
		return space + main + options + end + errormsg;
	}
		
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		boolean same = false;
		if (arg.equalsIgnoreCase("back")) {
			PDI.setIsCountryRuler(false);
			PDI.setCountryRuler(null);
			plugin.countrydata.remove(country.getName().toLowerCase());
			return new NewCountry(plugin, player, 0);
		}
		if (args.length != 2) return new NewCountry2(plugin, player, country, 2);

		else {
			for (Iterator<String> i = plugin.countrydata.keySet().iterator(); i.hasNext();) {
				Country temp = plugin.countrydata.get(i.next());
					if (temp.getPluralMoney().equalsIgnoreCase(args[1]) || temp.getSingularMoney().equalsIgnoreCase(args[0]) || temp.getPluralMoney().equalsIgnoreCase(args[0]) || temp.getSingularMoney().equalsIgnoreCase(args[1])) {
						same = true;
					}
			}
			if (same) return new NewCountry2(plugin, player, country, 1);
			country.setSingularMoney(args[0]);
			country.setPluralMoney(args[1]);
			PDI.setSingularMoney(args[0]);
			PDI.setPluralMoney(args[1]);
			return new NewCountry3(plugin, player, country, 0);
		}
	}
}
