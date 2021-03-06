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
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageBusiness2 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	Vector<String> businessnames = new Vector<String>();
	boolean isGoodBusiness = true;
	GoodBusiness good;
	ServiceBusiness service;
	String name;
	Vector<String> buildernames = new Vector<String>();
	
	// Constructor
	public ManageBusiness2(InspiredNations instance, Player playertemp, int errortemp, String business) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = business;
		PM.legalChest = false;
		PM.legalItem = false;
		PM.placesign = false;
		PM.legalsign = false;
		PM.onfallblock = false;
		PM.outside = false;
		PM.againstoutside = false;
		for(GoodBusiness i: PDI.getGoodBusinessOwned()){
			if (i.getName().equalsIgnoreCase(business)) {
				good = i;
				break;
			}
		}
		for(ServiceBusiness i: PDI.getServiceBusinessOwned()) {
			if (i.getName().equalsIgnoreCase(business)) {
				service = i;
				isGoodBusiness = false;
				break;
			}
		}
	}
	public ManageBusiness2(InspiredNations instance, Player playertemp, int errortemp, String business, Vector<String> buildernamestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = business; 
		PM.legalChest = false;
		PM.legalItem = false;
		PM.placesign = false;
		PM.legalsign = false;
		PM.onfallblock = false;
		PM.outside = false;
		PM.againstoutside = false;
		buildernames = buildernamestemp;

		for(GoodBusiness i: PDI.getGoodBusinessOwned()){
			if (i.getName().equalsIgnoreCase(business)) {
				good = i;
			}
		}
		for(ServiceBusiness i: PDI.getServiceBusinessOwned()) {
			if (i.getName().equalsIgnoreCase(business)) {
				service = i;
				isGoodBusiness = false;
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
			if (isGoodBusiness) {
				if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !good.getBuilders().contains(nametest)) {
					list.add(nametest);
					if (nametest.equalsIgnoreCase(name)) {
						list.clear();
						list.add(nametest);
						return list;
					}
				}
			}
			else {
				if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !service.getBuilders().contains(nametest)) {
					list.add(nametest);
					if (nametest.equalsIgnoreCase(name)) {
						list.clear();
						list.add(nametest);
						return list;
					}
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
		String main = ChatColor.BOLD + "Manage Business: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That business name is already being used in this country.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a builder.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player does not exist on this server or is already a builder.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(buildernames) + ". ");
		}
		options = options.concat(ChatColor.YELLOW + "" + ChatColor.BOLD + name + repeat(" ", 60 - ((int)(name.length()*1.4))) + ChatColor.RESET);
		if (isGoodBusiness) {
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Builders: " + ChatColor.GOLD + format(good.getBuilders()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Rename <name>" + repeat(" ", 60));
			options = options.concat("Reclaim Land" + repeat(" ", 65));
			options = options.concat("Place Shop" + repeat(" ", 65));
			options = options.concat("Add Builder <player>" + repeat(" ", 46));
			if (good.getBuilders().size() > 0) {
				options = options.concat("Remove Builder <player>" + repeat( " ", 50));
				input.add("remove");
			}
			options = options.concat("Payment Portions" + repeat(" ", 50));
			options = options.concat("Protection Level" + repeat(" ", 55));
			options = options.concat("Manage Employment " + ChatColor.GRAY + "(" + (good.getEmployRequest().size() + good.getOwnerRequest().size()) + ")"
					+ ChatColor.GREEN+ repeat(" ", 50));
			input.add("rename");
			input.add("reclaim");
			input.add("place");
			input.add("add");
			input.add("remove");
			input.add("manage");
			input.add("payment");
			input.add("protection");
		}
		else{
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Builders: " + ChatColor.GOLD + format(service.getBuilders())  + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Rename <name>" + repeat(" ", 60));
			options = options.concat("Reclaim Land" + repeat(" ", 65));
			options = options.concat("Add Builder <player>" + repeat(" ", 50));
			if (service.getBuilders().size() > 0) {
				options = options.concat("Remove Builder <player>" + repeat( " ", 46));
				input.add("remove");
			}
			options = options.concat("Payment Portions" + repeat(" ", 50));
			options = options.concat("Protection Level" + repeat(" ", 55));
			options = options.concat("Manage Employment " + ChatColor.GRAY + "(" + (service.getEmployRequest().size() + service.getOwnerRequest().size()) + ")"
					+ ChatColor.GREEN+ repeat(" ", 50));
			input.add("rename");
			input.add("add");
			input.add("remove");
			input.add("manage");
			input.add("payment");
			input.add("reclaim");
			input.add("protection");
		}
		return space + main + options + end + errormsg;
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new ManageBusiness2(plugin, player, 1, name);

		// Back
		if (arg.equalsIgnoreCase("back")) {
			if ((PDI.getServiceBusinessOwned().size() + PDI.getGoodBusinessOwned().size()) ==1) {
				return new HudConversationMain(plugin, player, 0);
			}
			else return new ManageBusiness1(plugin, player, 0);
		}
		// GOOD BUSINESS
		if (isGoodBusiness) {
			// Place Shop
			if (arg.equalsIgnoreCase("place shop")) {
				PM.setPlaceItem(true);
				PM.businessName = name;
				return new PlaceShop1(plugin, player, 0, name);
			}
			
			// Reclaim
			if (arg.equalsIgnoreCase("reclaim land")) {
				PM.goodBusiness(true);
				PM.reSelectGoodBusiness = true;
				PM.setPolygon(new polygonPrism(player.getWorld().getName()));
				PM.setCuboid(new Cuboid(player.getWorld().getName()));
				return new ReclaimGoodBusiness1(plugin, player, 0, good);
			}
			
			// Rename
			if (args[0].equalsIgnoreCase("rename")) {
				String tempname = "";
				for(int i = 0; i < args.length - 1; i++) {
					tempname = tempname.concat(args[i+1] + " ");
				}
				tempname = tempname.substring(0, tempname.length()-1);
				for(Town towntemp: PDI.getCountryResides().getTowns()) {
					for(GoodBusiness temp: towntemp.getGoodBusinesses()) {
						if(temp.getName().equalsIgnoreCase(tempname) && !temp.equals(good)) {
							return new ManageBusiness2(plugin, player, 2, name);
						}
					}
					for(ServiceBusiness temp: towntemp.getServiceBusinesses()) {
						if(temp.getName().equalsIgnoreCase(tempname)) {
							return new ManageBusiness2(plugin, player, 2, name);
						}
					}
				}
				good.setName(tempname);
				return new ManageBusiness2(plugin, player, 0, tempname);
			}
			// Add Builder
			if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("builder") && args.length == 3) {
				if (find(args[2]).size() == 1) args[2] = find(args[2]).get(0);
				else if (find(args[2]).size() == 0) return new ManageBusiness2(plugin, player, 6, name);
				else return new ManageBusiness2(plugin, player, 9, name, find(args[2]));
				
				good.addBuilder(args[2]);
				return new ManageBusiness2(plugin, player, 0, name);
			}
			// Remove Builder
			if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("builder") && args.length == 3) {
				if (find(args[2], good.getBuilders()).size() == 1) args[2] = find(args[2], good.getBuilders()).get(0);
				else if (find(args[2], good.getBuilders()).size()== 0) return new ManageBusiness2(plugin, player, 3, name);
				else return new ManageBusiness2(plugin, player, 9, name, find(args[2], good.getBuilders()));
				
				good.removeBuilder(args[2]);
				return new ManageBusiness2(plugin, player, 0, name);
			}
			
			// Manage Employment
			if (arg.equalsIgnoreCase("manage employment")){
				return new ManageEmployment1(plugin, player, 0, name);
			}
			// Protection Level
			if (args[0].equalsIgnoreCase("protection")) {
				return new GeneralProtectionLevel(plugin, player, 0, "goodbusiness", good);
			}
		}
		// SERVICE BUSINESS
		else{
			// Reclaim
			if (arg.equalsIgnoreCase("reclaim land")) {
				PM.serviceBusiness(true);
				PM.reSelectServiceBusiness = true;
				PM.setPolygon(new polygonPrism(player.getWorld().getName()));
				PM.setCuboid(new Cuboid(player.getWorld().getName()));
				return new ReclaimServiceBusiness1(plugin, player, 0, service);
			}
			
			// Rename
			if (args[0].equalsIgnoreCase("rename")) {
				String tempname = "";
				for(int i = 0; i < args.length - 1; i++) {
					tempname = tempname.concat(args[i+1] + " ");
				}
				tempname = tempname.substring(0, tempname.length()-1);
				for(Town towntemp: PDI.getCountryResides().getTowns()) {
					for(GoodBusiness temp: towntemp.getGoodBusinesses()) {
						if(temp.getName().equalsIgnoreCase(tempname)) {
							return new ManageBusiness2(plugin, player, 2, name);
						}
					}
					for(ServiceBusiness temp: towntemp.getServiceBusinesses()) {
						if(temp.getName().equalsIgnoreCase(tempname) && !temp.equals(service)) {
							return new ManageBusiness2(plugin, player, 2, name);
						}
					}
				}

				service.setName(tempname);
				return new ManageBusiness2(plugin, player, 0, tempname);
			}
			// Add Builder
			if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("builder") && args.length == 3) {
				if (find(args[2]).size() == 1) args[2] = find(args[2]).get(0);
				else if (find(args[2]).size() == 0) return new ManageBusiness2(plugin, player, 6, name);
				else return new ManageBusiness2(plugin, player, 9, name, find(args[2]));
				
				service.addBuilder(args[2]);
				return new ManageBusiness2(plugin, player, 0, name);
			}
			// Remove Builder
			if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("builder") && args.length == 3) {
				if (find(args[2], service.getBuilders()).size() == 1) args[2] = find(args[2], service.getBuilders()).get(0);
				else if (find(args[2], service.getBuilders()).size()== 0) return new ManageBusiness2(plugin, player, 3, name);
				else return new ManageBusiness2(plugin, player, 9, name, find(args[2], service.getBuilders()));
				
				service.removeBuilder(args[2]);
				return new ManageBusiness2(plugin, player, 0, name);
			}
			
			// Manage Employment
			if (arg.equalsIgnoreCase("manage employment")){
				return new ManageEmployment1(plugin, player, 0, name);
			}
			// Protection Level
			if (args[0].equalsIgnoreCase("protection")) {
				return new GeneralProtectionLevel(plugin, player, 0, "servicebusiness", service);
			}
			
		}
		
		return new ManageBusiness2(plugin, player, 1, name);
	}

}
