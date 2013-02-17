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
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class PlaceShop3 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	Vector<String> businessnames = new Vector<String>();
	GoodBusiness good;
	String name;
	
	// Constructor
	public PlaceShop3(InspiredNations instance, Player playertemp, int errortemp, String business) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = business;
		for(GoodBusiness i: PDI.getGoodBusinessOwned()){
			if (i.getName().equalsIgnoreCase(business)) {
				good = i;
				break;
			}
		}
	}
	public PlaceShop3(InspiredNations instance, Player playertemp, int errortemp, String business, Vector<String> businesstemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;

		for(GoodBusiness i: PDI.getGoodBusinessOwned()){
			if (i.getName().equalsIgnoreCase(business)) {
				good = i;
			}
		}
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
		String main = ChatColor.BOLD + "Place Shop: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		
		String[] namesplit = PM.getItemType().getType().name().split("_");
		String itemname = "";
		for (int i = 0; i < namesplit.length; i++) {
			itemname = itemname.concat(namesplit[i] + " ");
		}
		
		if (error == 1) {
			errormsg = errormsg.concat("Price cannot exceed 99999.99.");
		}
		if (error == 2) {
			errormsg = errormsg.concat("Only numbers are allowed as the argument. Do not use letters.");
		}
		
		options = options.concat(ChatColor.YELLOW + "Specify the price of your items. ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW);
		options = options.concat("Recomended for " + PM.quantity + " " + PM.itemname + ": " );
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		// Back
		if (arg.equalsIgnoreCase("back")) {
			return new PlaceShop2(plugin, player, 0, name);
		}
		try {
			double price = new Double(arg);
			if (price < 0) {
				price = price * -1.0;
			}
			String stringprice = cut(new BigDecimal(price)).toString();
			if (stringprice.length() > 15) {
				return new PlaceShop3(plugin, player, 1, name);
			}
			PM.cost = price;
			PM.placesign = true;
			return new PlaceShop4(plugin, player, 0, name);
		}
		catch (Exception ex) {
			return new PlaceShop3(plugin, player, 2, name);
		}
	}



}
