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
package com.github.InspiredOne.InspiredNations.Regions;

import java.math.BigDecimal;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;

public class ChestShopPlayerListener {
	InspiredNations plugin;
	HumanEntity player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	PlayerItemHeldEvent held;
	InventoryOpenEvent open;
	InventoryCloseEvent close;
	PlayerInteractEvent interact;
	BlockPlaceEvent place;
	SignChangeEvent sign;
	ItemStack item;
	boolean first = true;
	boolean legal;
	
	public ChestShopPlayerListener(InspiredNations instance, InventoryOpenEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		legal = PM.legalItem;
		open = event;
	}
	public ChestShopPlayerListener(InspiredNations instance, PlayerItemHeldEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		legal = PM.legalItem;
		held = event;
	}
	public ChestShopPlayerListener(InspiredNations instance, SignChangeEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		legal = PM.legalItem;
		sign = event;
	}
	
	public ChestShopPlayerListener(InspiredNations instance, InventoryCloseEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		legal = PM.legalItem;
		close = event;
	}
	
	public ChestShopPlayerListener(InspiredNations instance, PlayerInteractEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		legal = PM.legalItem;
		interact = event;
	}
	
	public ChestShopPlayerListener(InspiredNations instance, BlockPlaceEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		legal = PM.legalItem;
		place = event;
	}
	
	public void onOpenInventory() {
		if (!PM.getPlaceItem()) return;
		if (!open.getInventory().getType().equals(InventoryType.CHEST)) return;
		//PM.setItemType(player.getItemInHand());
		//plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(0));
			
	}
	
	public void onItemHeld() {
		if (!PM.getPlaceItem()) return;
		try {
		PM.setItemType(player.getInventory().getItem(held.getNewSlot()));
		legal = true;
		PM.legalItem = true;
		plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(0));
		}
		catch (Exception ex) {
			legal = false;
			PM.legalItem = false;
			plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(0));
		}
	}
	
	public void onCloseInventory() {
		if (!PM.getPlaceItem()) return;
		if (!close.getInventory().getType().equals(InventoryType.CHEST)) return;
		Inventory inventory = close.getInventory();
		PM.items = inventory;
		/*for (ItemStack thing: inventory.getContents()) {
			try{
				if (!thing.equals(null) && first) {
					item = thing;
					first = false;
					legal = true;
				}
				else if(!thing.getType().equals(item.getType()) && !thing.equals(null)) {
					legal = false;
					break;
				}
			}
			catch (Exception ex) {		
			}
		}*/
		if (legal && PM.legalChest) {
			//PM.setItemType(item);
			PM.items = inventory;
			PM.legalItem = true;
			plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(0));
		}
		else if(legal && !PM.legalChest) {
			PM.setItemType(item);
			PM.items = inventory;
			PM.legalItem = true;
			plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(1));
		}
		else if(!legal && PM.legalChest) {
			PM.legalItem = false;
			PM.items = null;
			PM.setItemType(null);
			plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(2));
		}
		else if(!legal && !PM.legalChest) {
			PM.legalItem = false;
			PM.items = null;
			PM.setItemType(null);
			plugin.getServer().getPlayer(player.getName()).sendRawMessage(generateMessage(3));
		}

		if (!legal) {
			PM.legalItem = false;
			PM.items = null;
			PM.setItemType(null);
		}
	}
	
	public void onInteractWithChest() {
		if (PM.getPlaceItem()){
			if (interact.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (interact.getClickedBlock().getTypeId()==54) {
					PM.alreadyChest = false;
					Location spot = interact.getClickedBlock().getLocation();
					GoodBusiness business = null;
					for (GoodBusiness temp:PDI.getGoodBusinessOwned()) {
						if (temp.getName().equalsIgnoreCase(PM.businessName)) {
							business = temp;
							break;
						}
					}
	
					if (business.isIn(spot)) {
						// Check in four directions for another chest
						PM.tempchests[0] = spot;
						Location test = new Location(spot.getWorld(), spot.getX(), spot.getY(), spot.getZ());
						boolean doubleChest = false;
						test.setX(test.getX() + 1);
						if (test.getBlock().getTypeId() == 54) {
							doubleChest = true;
							if (business.isIn(test)) {
								PM.tempchests[0] = spot.clone();
								PM.tempchests[1] = test.clone();
								PM.legalChest = true;
							}
							else {
								PM.legalChest = false;
							}
						}
						test.setX(test.getX() - 2);
						if (test.getBlock().getTypeId() == 54) {
							doubleChest = true;
							if (business.isIn(test)) {
								PM.tempchests[0] = spot.clone();
								PM.tempchests[1] = test.clone();
								PM.legalChest = true;
							}
							else {
								PM.legalChest = false;
							}
						}
						test.setX(test.getX() + 1);
						test.setZ(test.getZ() + 1);
						if (test.getBlock().getTypeId() == 54) {
							doubleChest = true;
							if (business.isIn(test)) {
								PM.tempchests[0] = spot.clone();
								PM.tempchests[1] = test.clone();
								PM.legalChest = true;
							}
							else {
								PM.legalChest = false;
							}
						}
						test.setZ(test.getZ() - 2);
						if (test.getBlock().getTypeId() == 54) {
							doubleChest = true;
							if (business.isIn(test)) {
								PM.tempchests[0] = spot.clone();
								PM.tempchests[1] = test.clone();
								PM.legalChest = true;
							}
							else {
								PM.legalChest = false;
							}
						}
						if (!doubleChest) {
							PM.tempchests[0] = spot.clone();
							PM.tempchests[1] = null;
							PM.legalChest = true;
						}

						PM.doublechest = doubleChest;
						for(ChestShop shop: business.getChestShop()) {

							if (shop.isIn(PM.tempchests[0])) {
								//Commented out because I changed my mind about each shop having an individual chest
								//PM.alreadyChest = true;
							}
						}
						
					}
					else {
						PM.legalChest = false;
					}
				}
			}
		}
	}
	
	public void onBlockPlace() {
		if (PM.placesign) {
			GoodBusiness business = null;
			for (GoodBusiness temp:PDI.getGoodBusinessOwned()) {
				if (temp.getName().equalsIgnoreCase(PM.businessName)) {
					business = temp;
					break;
				}
			}
			if (place.getBlockPlaced().getTypeId() != 63 && place.getBlockPlaced().getTypeId() != 68) return;
			if (!business.isIn(place.getBlockPlaced().getLocation())) {
				PM.outside = true;
				place.setCancelled(true);
				place.getPlayer().sendRawMessage(generateMessage2(3));
				return;
			}
			if (!business.isIn(place.getBlockAgainst().getLocation())) {
				PM.againstoutside = true;
				place.setCancelled(true);
				place.getPlayer().sendRawMessage(generateMessage2(4));
				return;
			}
			if (place.getBlockAgainst().getTypeId() == 12 || place.getBlockAgainst().getTypeId() == 13 || place.getBlockAgainst().getTypeId() == 63 || place.getBlockAgainst().getTypeId() == 68) {
				PM.onfallblock = true;
				place.setCancelled(true);
				place.getPlayer().sendRawMessage(generateMessage2(2));
				return;
			}
			PM.outside = false;
			PM.againstoutside = false;
			PM.onfallblock = false;
			PM.legalsign = true;
			place.getPlayer().sendRawMessage(generateMessage2(0));
			PM.tempchests[2] = place.getBlock().getLocation();
			PM.tempchests[3] = place.getBlockAgainst().getLocation();
		}
	}
	
	public void onSignChangeEvent() {
		if (PM.placesign) {
			GoodBusiness business = null;
			for (GoodBusiness temp:PDI.getGoodBusinessOwned()) {
				if (temp.getName().equalsIgnoreCase(PM.businessName)) {
					business = temp;
					break;
				}
			}
			if (!business.isIn(sign.getBlock().getLocation())) return;
			sign.setLine(0, PM.itemname);
			sign.setLine(1, "Price: " + cut(new BigDecimal(PM.cost)).toString());
			sign.setLine(2, "Qt: " + PM.quantity);
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
	
	// A method to generate the printout for PlaceShop1
	public String generateMessage(int error) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Place Shop: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		

		
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
	
	// A method to generate the printout for PlaceShop4
	public String generateMessage2(int error) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Place Shop: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		
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
		
		options = options.concat(ChatColor.YELLOW + "Place a sign where you would like players to buy from. Only the last valid sign you place will work. Type 'finish' when you are done. ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW);
		if (!PM.legalsign) {
			options = options.concat("Valid Sign Placed: " + ChatColor.GOLD + "No ");
		}
		else options = options.concat("Valid Sign Placed: " + ChatColor.GOLD + "Yes ");
		
		return space + main + options + end + errormsg;
	}
	
}
