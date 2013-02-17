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
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageHouseOwners extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	String name;
	House house;
	Vector<String> buildernames = new Vector<String>();
	
	// Constructor
	public ManageHouseOwners(InspiredNations instance, Player playertemp, int errortemp, String housename) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = housename;
		for(House i: PDI.getHouseOwned()){
			if (i.getName().equalsIgnoreCase(name)) {
				house = i;
				break;
			}
		}
	}
	public ManageHouseOwners(InspiredNations instance, Player playertemp, int errortemp, String housename, Vector<String> buildertemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = housename;
		for(House i: PDI.getHouseOwned()){
			if (i.getName().equalsIgnoreCase(name)) {
				house = i;
				break;
			}
		}
		buildernames = buildertemp;
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
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !house.getBuilders().contains(nametest)) {
				list.add(nametest);
				if (nametest.equalsIgnoreCase(name)) {
					list.clear();
					list.add(nametest);
					return list;
				}
			}
		}
		return list;
	}
	
	// A generalized find function where you give it the vector you want it to search in.
	public Vector<String> find(String name, Vector<String> test) {
		Vector<String> list = new Vector<String>();
		for (String nametest:test) {
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName())) {
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
		String main = ChatColor.BOLD + "Manage House: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That house name is already being used in this town.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not an owner.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "That player has not been offered ownership.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player has not requested ownership of your house.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a citizen of this country.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(buildernames) + ". ");
		}
		
		options = options.concat(ChatColor.YELLOW + " " + ChatColor.BOLD + house.getName() + ChatColor.RESET + repeat(" ", 70 - (int)(house.getName().length()*1.4)) + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Owners: " + ChatColor.GOLD + format(house.getOwners()) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Owner Offers: " + ChatColor.GOLD + format(house.getOwnerOffers()) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Owner Requests: " + ChatColor.GOLD + format(house.getOwnerRequest()) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		
		options = options.concat(ChatColor.GREEN + "Accept Request <player>" + repeat(" ", 45));
		options = options.concat("Reject Request <player>" + repeat(" ", 45));
		options = options.concat("Offer Owner <player>" + repeat(" ", 52));
		options = options.concat("Undo Offer <player>" + repeat( " ", 52));
		options = options.concat("Remove Owner <player>" + repeat(" ", 45));
		input.add("accept");
		input.add("offer");
		input.add("remove");
		input.add("reject");
		input.add("undo");
		
		return space + main+options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		
		String[] args = arg.split(" ");
		if (args.length < 3 && !arg.equalsIgnoreCase("back")) return new ManageHouseOwners(plugin, player, 1, name);
		if (!input.contains(args[0].toLowerCase())) return new ManageHouseOwners(plugin, player, 1, name);
		// Back
		if (arg.equalsIgnoreCase("back")) {
			return new ManageHouse2(plugin, player, 0, name);
		}

		// Accept Request to become co-owner of your house.
		if (args[0].equalsIgnoreCase("accept") && args.length == 3) {
			if (find(args[2], house.getOwnerRequest()).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2], house.getOwnerRequest()).size() == 0) return new ManageHouseOwners(plugin, player, 6, name);
			else return new ManageHouseOwners(plugin, player, 9, name, find(args[2], house.getOwnerRequest()));
			
			house.addOwner(plugin.getServer().getPlayerExact(args[2]));
			plugin.playerdata.get(args[2]).addHousesOwned(house);
			return new ManageHouseOwners(plugin, player, 0, name);
		}
		
		// Reject Request
		if (args[0].equalsIgnoreCase("reject") && args.length == 3) {
			if (find(args[2], house.getOwnerRequest()).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2], house.getOwnerRequest()).size() == 0) return new ManageHouseOwners(plugin, player, 6, name);
			else return new ManageHouseOwners(plugin, player, 9, name, find(args[2], house.getOwnerRequest()));
			
			house.removeOwnerRequest(plugin.getServer().getPlayerExact(args[2]));
			return new ManageHouseOwners(plugin, player, 0, name);
		}
		
		// Remove Owner
		if (args[0].equalsIgnoreCase("remove") && args.length == 3) {
			if (find(args[2], house.getOwners()).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2], house.getOwners()).size() == 0) return new ManageHouseOwners(plugin, player, 3, name);
			else return new ManageHouseOwners(plugin, player, 9, name, find(args[2], house.getOwners()));
			
			house.removeOwner(plugin.getServer().getPlayerExact(args[2]));
			plugin.playerdata.get(args[2]).removeHousesOwned(house);
			plugin.getServer().getPlayerExact(args[2]).abandonConversation(plugin.playerdata.get(args[2]).getConversation());
			
			return new ManageHouseOwners(plugin, player, 0, name);
		}
		
		// Undo Offer
		if (args[0].equalsIgnoreCase("undo") && args.length == 3) {
			if (find(args[2], house.getOwnerOffers()).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2], house.getOwnerOffers()).size() == 0) return new ManageHouseOwners(plugin, player, 4, name);
			else return new ManageHouseOwners(plugin, player, 9, name, find(args[2], house.getOwnerOffers()));
			
			house.removeOwnerOffer(plugin.getServer().getPlayerExact(args[2]));
			return new ManageHouseOwners(plugin, player, 0, name);
		}
		
		// Offer Owner
		if (args[0].equalsIgnoreCase("offer") && args.length == 3) {
			if (find(args[2], PDI.getCountryResides().getResidents()).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2], PDI.getCountryResides().getResidents()).size() == 0) return new ManageHouseOwners(plugin, player, 7, name);
			else return new ManageHouseOwners(plugin, player, 9, name, find(args[2], PDI.getCountryResides().getResidents()));
			
			house.addOwnerOffer(plugin.getServer().getPlayerExact(args[2]));
			return new ManageHouseOwners(plugin, player, 0, name);
		}
		return new ManageHouseOwners(plugin, player, 1, name);
	}




}
