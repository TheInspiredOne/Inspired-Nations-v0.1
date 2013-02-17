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


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Country.Country;

public class NewCountry3 extends StringPrompt {
	
	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	String playername;
	Country country;
	int error;
	
	// Constructor
	public NewCountry3(InspiredNations instance, Player playertemp, Country countrytemp, int errortemp) {
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
	// A method to cut off decimals greater than the hundredth place;
	public double cut(double x) {
		int y;
		y = (int) (x*100);
		return y/100.0;
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		int moneyLength =(int) (PDI.getPluralMoney().length() * 1.4);
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "New Country Economics: Read the instructions." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = "";
		
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "Only numbers are aloud as the argument. Do not use letters.");
		}
		options = options.concat("How much should a single diamond be worth in " + country.getPluralMoney() + "?" + repeat(" ", 15 - moneyLength) + " ");
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		try {
			if (arg.equalsIgnoreCase("back")) {
				country.setPluralMoney("");
				country.setSingularMoney("");
				return new NewCountry2(plugin, player, country, 0);
			}
			BigDecimal diamond = new BigDecimal(arg);
			country.setMoneyMultiplyer(diamond.divide(new BigDecimal(500), 25, BigDecimal.ROUND_DOWN));
			PDI.setMoneyMultiplyer(country.getMoneyMultiplyer());
			return new ManageCountry(plugin, player, 0);
		}
		catch (Exception ex) {
			return new NewCountry3(plugin, player, country, 1);
		}
	}

}
