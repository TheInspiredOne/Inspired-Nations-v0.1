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
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class GeneralProtectionLevel extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	String from = "";
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	PlayerMethods PMeth;
	int error;
	Vector<String> suggestions = new Vector<String>();
	Park park;
	House house;
	GoodBusiness good;
	ServiceBusiness service;
	
	// Constructor
	public GeneralProtectionLevel(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, Park parktemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		from = fromtemp;
		park = parktemp;
	}
	
	// Constructor
	public GeneralProtectionLevel(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, House housetemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		from = fromtemp;
		house = housetemp;
		PMeth = new PlayerMethods(plugin, player);
	}
	
	// Constructor
	public GeneralProtectionLevel(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, GoodBusiness business) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		from = fromtemp;
		PMeth = new PlayerMethods(plugin, player);
		good = business;
	}
	
	// Constructor
	public GeneralProtectionLevel(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, ServiceBusiness business) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		from = fromtemp;
		PMeth = new PlayerMethods(plugin, player);
		service = business;
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
		
		if (from.equals("federalpark")) {
			options = options.concat(ChatColor.YELLOW + "Current Protection Level: " + ChatColor.GOLD + park.getProtectionLevel() + ChatColor.YELLOW + repeat(" ", 44));
			options = options.concat(ChatColor.YELLOW + "New Protection Level: " + ChatColor.GOLD + park.getFutureProtectionLevel() + ChatColor.GRAY + " Applied after next tax cycle." + repeat(" ", 10));
			options = options.concat(ChatColor.YELLOW + "Current Protection Cost: " + ChatColor.GOLD + cut(park.getTaxAmount()).toString() + repeat(" ", (int) (47 - Math.round((cut(park.getTaxAmount()).toString().length() * 1.4)))));
			options = options.concat(ChatColor.YELLOW + "New Protection Cost: " + ChatColor.GOLD + cut(park.getTaxAmount(park.getFutureProtectionLevel())).toString() + 
					ChatColor.GRAY + " Applied after next tax cycle. " + repeat(" ", (int) (15 - Math.round((cut(park.getTaxAmount(park.getFutureProtectionLevel())).toString().length() * 1.4)))));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.GOLD + "Level 0: " + ChatColor.YELLOW + "(No protection) Anybody that can build in your country can build in your park." + repeat(" ", 40));
			options = options.concat(ChatColor.GOLD + "Level 1: " + ChatColor.YELLOW + "(Complete protection) Only country rulers and designated builders can build in the park." + repeat(" ", 11));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Set <level number>" + repeat(" ", 55));
		}
		else if(from.equals("park")) {
			options = options.concat(ChatColor.YELLOW + "Current Protection Level: " + ChatColor.GOLD + park.getProtectionLevel() + ChatColor.YELLOW + repeat(" ", 44));
			options = options.concat(ChatColor.YELLOW + "New Protection Level: " + ChatColor.GOLD + park.getFutureProtectionLevel() + ChatColor.GRAY + " Applied after next tax cycle." + repeat(" ", 10));
			options = options.concat(ChatColor.YELLOW + "Current Protection Cost: " + ChatColor.GOLD + cut(park.getTaxAmount()).toString() + repeat(" ", (int) (47 - Math.round((cut(park.getTaxAmount()).toString().length() * 1.4)))));
			options = options.concat(ChatColor.YELLOW + "New Protection Cost: " + ChatColor.GOLD + cut(park.getTaxAmount(park.getFutureProtectionLevel())).toString() + 
					ChatColor.GRAY + " Applied after next tax cycle. " + repeat(" ", (int) (15 - Math.round((cut(park.getTaxAmount(park.getFutureProtectionLevel())).toString().length() * 1.4)))));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.GOLD + "Level 0: " + ChatColor.YELLOW + "(No protection) Anybody that can build in your town can build in your park." + repeat(" ", 50));
			options = options.concat(ChatColor.GOLD + "Level 1: " + ChatColor.YELLOW + "(Complete protection) Only country rulers, town rulers and designated builders can build in the park." + repeat(" ", 11));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Set <level number>" + repeat(" ", 55));
		}
		else if(from.equals("house")) {
			options = options.concat(ChatColor.YELLOW + "Current Protection Level: " + ChatColor.GOLD + house.getProtectionLevel() + ChatColor.YELLOW + repeat(" ", 44));
			options = options.concat(ChatColor.YELLOW + "New Protection Level: " + ChatColor.GOLD + house.getFutureProtectionLevel() + ChatColor.GRAY + " Applied after next tax cycle." + repeat(" ", 10));
			options = options.concat(ChatColor.YELLOW + "Current Protection Cost: " + ChatColor.GOLD + cut(PMeth.houseTax(house)).toString() + repeat(" ", (int) (47 - Math.round((cut(PMeth.houseTax(house)).toString().length() * 1.4)))));
			options = options.concat(ChatColor.YELLOW + "New Protection Cost: " + ChatColor.GOLD + cut(PMeth.houseTax(house, house.getFutureProtectionLevel()))).toString() + 
					ChatColor.GRAY + " Applied after next tax cycle. " + repeat(" ", (int) (15 - Math.round((cut(PMeth.houseTax(house, house.getFutureProtectionLevel())).toString().length() * 1.4))));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.GOLD + "Level 0: " + ChatColor.YELLOW + "(No protection) Anybody that can build and interact in your town can build and interact in your house." + repeat(" ", 20));
			options = options.concat(ChatColor.GOLD + "Level 1: " + ChatColor.YELLOW + "(Block protection) Only country rulers, town rulers, designated builders, and house owners can build in the house." + repeat(" ", 1));
			options = options.concat(ChatColor.GOLD + "Level 2: " + ChatColor.YELLOW + "(Interact protection) Only country rulers, town rulers, designated builders, and house owners can interact in the house." + repeat(" ", 65));
			options = options.concat(ChatColor.GOLD + "Level 3: " + ChatColor.YELLOW + "(Player protection) You gain damage protection from non-house residents." + repeat(" ", 11));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Set <level number>" + repeat(" ", 55));
		}
		else if(from.equals("goodbusiness")) {
			options = options.concat(ChatColor.YELLOW + "Current Protection Level: " + ChatColor.GOLD + good.getProtectionLevel() + ChatColor.YELLOW + repeat(" ", 44));
			options = options.concat(ChatColor.YELLOW + "New Protection Level: " + ChatColor.GOLD + good.getFutureProtectionLevel() + ChatColor.GRAY + " Applied after next tax cycle." + repeat(" ", 10));
			options = options.concat(ChatColor.YELLOW + "Current Protection Cost: " + ChatColor.GOLD + cut(PMeth.goodBusinessTax(good)).toString() + repeat(" ", (int) (47 - Math.round((cut(PMeth.goodBusinessTax(good)).toString().length() * 1.4)))));
			options = options.concat(ChatColor.YELLOW + "New Protection Cost: " + ChatColor.GOLD + cut(PMeth.goodBusinessTax(good, good.getFutureProtectionLevel())).toString() + 
					ChatColor.GRAY + " Applied after next tax cycle. " + repeat(" ", (int) (15 - Math.round((cut(PMeth.goodBusinessTax(good, good.getFutureProtectionLevel()))).toString().length() * 1.4))));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.GOLD + "Level 0: " + ChatColor.YELLOW + "(No protection) Anybody that can build and interact in your town can build and interact in your business." + repeat(" ", 12));
			options = options.concat(ChatColor.GOLD + "Level 1: " + ChatColor.YELLOW + "(Block protection) Only country rulers, town rulers, employees, and business owners can build in the business." + repeat(" ", 1));
			options = options.concat(ChatColor.GOLD + "Level 2: " + ChatColor.YELLOW + "(Interact protection) Only country rulers, town rulers, employees, and business owners can interact in the business." + repeat(" ", 65));
			options = options.concat(ChatColor.GOLD + "Level 3: " + ChatColor.YELLOW + "(Player protection) Employees and owners gain protection from attacks within the business." + repeat(" ", 11));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Set <level number>" + repeat(" ", 55));
		}
		else if(from.equals("servicebusiness")) {
			options = options.concat(ChatColor.YELLOW + "Current Protection Level: " + ChatColor.GOLD + service.getProtectionLevel() + ChatColor.YELLOW + repeat(" ", 44));
			options = options.concat(ChatColor.YELLOW + "New Protection Level: " + ChatColor.GOLD + service.getFutureProtectionLevel() + ChatColor.GRAY + " Applied after next tax cycle." + repeat(" ", 10));
			options = options.concat(ChatColor.YELLOW + "Current Protection Cost: " + ChatColor.GOLD + cut(PMeth.serviceBusinessTax(service)).toString() + repeat(" ", (int) (47 - Math.round((cut(PMeth.serviceBusinessTax(service))).toString().length() * 1.4))));
			options = options.concat(ChatColor.YELLOW + "New Protection Cost: " + ChatColor.GOLD + cut(PMeth.serviceBusinessTax(service, service.getFutureProtectionLevel())).toString() + 
					ChatColor.GRAY + " Applied after next tax cycle. " + repeat(" ", (int) (15 - Math.round(cut(PMeth.serviceBusinessTax(service, service.getFutureProtectionLevel())).toString().length() * 1.4))));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.GOLD + "Level 0: " + ChatColor.YELLOW + "(No protection) Anybody that can build and interact in your town can build and interact in your business." + repeat(" ", 12));
			options = options.concat(ChatColor.GOLD + "Level 1: " + ChatColor.YELLOW + "(Block protection) Only country rulers, town rulers, employees, and business owners can build in the business." + repeat(" ", 1));
			options = options.concat(ChatColor.GOLD + "Level 2: " + ChatColor.YELLOW + "(Interact protection) Only country rulers, town rulers, employees, and business owners can interact in the business." + repeat(" ", 65));
			options = options.concat(ChatColor.GOLD + "Level 3: " + ChatColor.YELLOW + "(Player protection) Employees and owners gain protection from attacks within the business." + repeat(" ", 11));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Set <level number>" + repeat(" ", 55));
		}
		input.add("set");
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");

		
		// FEDERAL PARK
		if (from.equals("federalpark")) {
			
			if (!input.contains(args[0].toLowerCase())) return new GeneralProtectionLevel(plugin, player, 1, "federalpark", park);
			
			// back
			if (arg.equalsIgnoreCase("back")) return new ManageFederalPark2(plugin, player, 0, park.getName());
			
			// set
			if (args[0].equalsIgnoreCase("set")) {
				try {
					int level = java.lang.Integer.parseInt(args[1]);
					park.setFutureProtectionLevel(Math.abs(level));
					return new GeneralProtectionLevel(plugin, player, 0, "federalpark", park);
				}
				catch (Exception ex) {
					return new GeneralProtectionLevel(plugin, player, 2, "federalpark", park);
				}
			}
		}
		// PARK
		else if (from.equals("park")) {
			
			if (!input.contains(args[0].toLowerCase())) return new GeneralProtectionLevel(plugin, player, 1, "park", park);
			
			// back
			if (arg.equalsIgnoreCase("back")) return new ManagePark2(plugin, player, 0, park.getName());
			
			// set
			if (args[0].equalsIgnoreCase("set")) {
				try {
					int level = java.lang.Integer.parseInt(args[1]);
					park.setFutureProtectionLevel(Math.abs(level));
					return new GeneralProtectionLevel(plugin, player, 0, "park", park);
				}
				catch (Exception ex) {
					return new GeneralProtectionLevel(plugin, player, 2, "park", park);
				}
			}
		}
		
		// HOUSE
		else if (from.equals("house")) {
			
			if (!input.contains(args[0].toLowerCase())) return new GeneralProtectionLevel(plugin, player, 1, "house", house);
			
			// back
			if (arg.equalsIgnoreCase("back")) return new ManageHouse2(plugin, player, 0, house.getName());
			
			// set
			if (args[0].equalsIgnoreCase("set")) {
				try {
					int level = java.lang.Integer.parseInt(args[1]);
					house.setFutureProtectionLevel(Math.abs(level));
					return new GeneralProtectionLevel(plugin, player, 0, "house", house);
				}
				catch (Exception ex) {
					return new GeneralProtectionLevel(plugin, player, 2, "house", house);
				}
			}
		}
		
		// GOOD BUSINESS
		else if (from.equals("goodbusiness")) {
			
			if (!input.contains(args[0].toLowerCase())) return new GeneralProtectionLevel(plugin, player, 1, "goodbusiness", good);
			
			// back
			if (arg.equalsIgnoreCase("back")) return new ManageBusiness2(plugin, player, 0, good.getName());
			
			// set
			if (args[0].equalsIgnoreCase("set")) {
				try {
					int level = java.lang.Integer.parseInt(args[1]);
					good.setFutureProtectionLevel(Math.abs(level));
					return new GeneralProtectionLevel(plugin, player, 0, "goodbusiness", good);
				}
				catch (Exception ex) {
					return new GeneralProtectionLevel(plugin, player, 2, "goodbusiness", good);
				}
			}
		}
		
		// SERVICE BUSINESS
		else if (from.equals("servicebusiness")) {
			
			if (!input.contains(args[0].toLowerCase())) return new GeneralProtectionLevel(plugin, player, 1, "servicebusiness", service);
			
			// back
			if (arg.equalsIgnoreCase("back")) return new ManageBusiness2(plugin, player, 0, service.getName());
			
			// set
			if (args[0].equalsIgnoreCase("set")) {
				try {
					int level = java.lang.Integer.parseInt(args[1]);
					service.setFutureProtectionLevel(Math.abs(level));
					return new GeneralProtectionLevel(plugin, player, 0, "servicebusiness", service);
				}
				catch (Exception ex) {
					return new GeneralProtectionLevel(plugin, player, 2, "servicebusiness", service);
				}
			}
		}
		return new HudConversationMain(plugin, player, 0);
	}



}
