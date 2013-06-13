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
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class PlaceShop1 extends StringPrompt {

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
	ItemStack itemheld;
	
	// Constructor
	public PlaceShop1(InspiredNations instance, Player playertemp, int errortemp, String business) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		plugin.logger.info("1a");
		if(!player.getItemInHand().equals(Material.AIR)) {
			itemheld = player.getItemInHand();
			PM.legalItem = true;
			PM.setItemType(itemheld);
		}
		else {
			PM.legalItem = false;
		}
		plugin.logger.info("2a");
		error = errortemp;
		name = business;
		for(GoodBusiness i: PDI.getGoodBusinessOwned()){
			if (i.getName().equalsIgnoreCase(business)) {
				good = i;
				break;
			}
		}
	}
	public PlaceShop1(InspiredNations instance, Player playertemp, int errortemp, String business, Vector<String> businesstemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		plugin.logger.info("1b");
		if(!player.getItemInHand().equals(Material.AIR)) {
			itemheld = player.getItemInHand();
			PM.legalItem = true;
			PM.setItemType(itemheld);
		}
		else {
			PM.legalItem = false;
		}
		plugin.logger.info("2b");
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
		


		if (error == 1) {
			errormsg = errormsg.concat("That chest is outside of the business.");
		}
		if (error == 2)	{
			errormsg = errormsg.concat("That Item is invalid. Put the item you would like to sell in your hand.");
		}
		if (error == 3) {
			errormsg = errormsg.concat("That chest is outside of the business and the Item is invalid. Put the item you would like to sell in your hand and interact with the chest you'd like to sell it from.");
		}
		if (error == 4) {
			errormsg = errormsg.concat("You can't sell zero items.");
		}
		if (error == 5) {
			errormsg = errormsg.concat("Only integers allowed.");
		}
		if (error == 6) {
			errormsg = errormsg.concat("Quantity cannot exceed 999999999 items.");
		}
		if (error == 7) {
			errormsg = errormsg.concat("The chest you selected was made into a shop before you could finish.");
		}
		if (error == 8) {
			errormsg = errormsg.concat("The chest you selected is already a shop.");
		}
		
		options = options.concat(ChatColor.YELLOW + "Place the item you would like to sell in the chest you would like to sell from. Then enter the quantity that you would like to sell per sale." + repeat(" ", 30));
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW);
		if (!PM.legalChest) {
			options = options.concat("Legal Chest:" + ChatColor.GOLD +" No" +repeat(" ", 57));
		}
		else {
			options = options.concat("Legal Chest:" + ChatColor.GOLD +" Yes" + repeat(" ", 57));
		}
		if (!PM.legalItem) {
			options = options.concat(ChatColor.YELLOW + "Item Type: " + ChatColor.GOLD + "Pending... " );
		}
		else {
			
			String[] namesplit = PM.getItemType().getType().name().split("_");
			String itemname = "";
			for (int i = 0; i < namesplit.length; i++) {
				itemname = itemname.concat(namesplit[i] + " ");
			}
			
			options = options.concat(ChatColor.YELLOW + "Item Type: " + ChatColor.GOLD + itemname + " ");
		}

		
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		// Back
		if (arg.equalsIgnoreCase("back")) {
			PM.businessName = "";
			PM.legalChest = false;
			PM.legalItem = false;
			PM.setPlaceItem(false);
			return new ManageBusiness2(plugin, player, 0, name);
		}
		if (PM.legalItem && !PM.legalChest) {
			return new PlaceShop1(plugin, player, 1, name);
		}
		else if(!PM.legalItem && PM.legalChest) {
			return new PlaceShop1(plugin, player, 2, name);
		}
		else if (!PM.legalItem && !PM.legalChest) {
			return new PlaceShop1(plugin, player, 3, name);
		}
		
		// Number
		

		
		
		try {
			int amount = new Integer(arg);
			if (amount < 0) {
				amount = amount*-1;
			}
			if (amount == 0) {
				return new PlaceShop1(plugin, player, 4, name);
			}
			if ((amount + "").length() > 9) {
				return new PlaceShop1(plugin, player, 6, name);
			}
			PM.setPlaceItem(false);
			PM.quantity = amount;
			return new PlaceShop2(plugin, player, 0, name);
		}
		catch (Exception ex) {
			return new PlaceShop1(plugin, player, 5, name);
		}
	}




}
