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
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Regions.ChestShop;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class PlaceShop4 extends StringPrompt {
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
	public PlaceShop4(InspiredNations instance, Player playertemp, int errortemp, String business) {
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
	public PlaceShop4(InspiredNations instance, Player playertemp, int errortemp, String business, Vector<String> businesstemp) {
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
		input.add("finish");
		
		String[] namesplit = PM.getItemType().getType().name().split("_");
		String itemname = "";
		for (int i = 0; i < namesplit.length; i++) {
			itemname = itemname.concat(namesplit[i] + " ");
		}
		
		if (error == 1) {
			errormsg = errormsg.concat("That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat("You cannot place your sign on sand, gravel, or other signs.");
		}
		if (error == 3) {
			errormsg = errormsg.concat("You must put your sign inside your business.");
		}
		if (error == 4) {
			errormsg = errormsg.concat("The block that you put your sign on must be inside your business.");
		}
		if (error == 5) {
			errormsg = errormsg.concat("You must first place a valid sign.");
		}
		
		options = options.concat(ChatColor.YELLOW + "Place a sign where you would like players to buy from. Only the last valid sign you place will work. Type 'finish' when you are done. ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW);
		if (!PM.legalsign) {
			options = options.concat("Valid Sign Placed: " + ChatColor.GOLD + "No ");
		}
		else options = options.concat("Valid Sign Placed: " + ChatColor.GOLD + "Yes ");

		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new PlaceShop4(plugin, player, 1, name);
		// Back
		if (arg.equalsIgnoreCase("back")) {
			PM.placesign = false;
			PM.legalsign = false;
			PM.onfallblock = false;
			PM.outside = false;
			PM.againstoutside = false;
			return new PlaceShop3(plugin, player, 0, name);
		}
		if (!PM.legalsign) {
			if (PM.onfallblock) {
				return new PlaceShop4(plugin, player, 2, name);
			}
			if (PM.outside) {
				return new PlaceShop4(plugin, player, 3, name);
			}
			if (PM.againstoutside) {
				return new PlaceShop4(plugin, player, 4, name);
			}
			return new PlaceShop4(plugin, player, 5, name);
		}
		
		// Finish
		if (arg.equalsIgnoreCase("finish")) {
			Inventory inventory = PM.items;
			Inventory doubleinvent = null;
			ItemStack item = PM.getItemType();
			for (ItemStack thing: inventory.getContents()) {
				try{
					if(!thing.getType().equals(item.getType()) && !thing.equals(null)) {
						inventory.remove(thing);
						player.getWorld().dropItem(player.getLocation(), thing);

					}
				}
				catch (Exception ex) {		
				}
			}
			if (PM.doublechest){
				doubleinvent = ((DoubleChestInventory) ((InventoryHolder) PM.tempchests[0].getBlock().getState()).getInventory()).getLeftSide();
			}
			else {
				inventory = ((Chest) PM.tempchests[0].getBlock().getState()).getInventory();
			}
			if (PM.doublechest) {
				for(HumanEntity person: doubleinvent.getViewers()) {
					person.closeInventory();
					person.getOpenInventory().close();
				}
			}
			else {
				for(HumanEntity person: inventory.getViewers()) {
					person.closeInventory();
					person.getOpenInventory().close();
				}
			}
			for (ChestShop shop: good.getChestShop()) {
				if (shop.isIn(PM.tempchests[0])) {
					PM.placesign = false;
					PM.legalsign = false;
					PM.onfallblock = false;
					PM.outside = false;
					PM.againstoutside = false;
					PM.setPlaceItem(true);
					PM.businessName = name;
					return new PlaceShop1(plugin, player, 7, good.getName());
				}
			}
			PM.placesign = false;
			PM.legalsign = false;
			PM.onfallblock = false;
			PM.outside = false;
			PM.againstoutside = false;
			ChestShop shop = new ChestShop(PM.items, PM.getItemType(), PM.cost,PM.quantity, PM.tempchests, PM.doublechest);
			good.addChestShop(shop);
			return new ManageBusiness2(plugin, player, 0, name);
		}
		return null;
	}



}
