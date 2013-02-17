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
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class Jobs2 extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	Vector<String> businessnames = new Vector<String>();
	Vector<String> employoffers = new Vector<String>();
	Vector<String> owneroffers = new  Vector<String>();
	Vector<String> employrequests = new Vector<String>();
	Vector<String> ownerrequests = new Vector<String>();
	Vector<String> employed = new Vector<String>();
	Vector<String> owner = new Vector<String>();
	Vector<String> availablebusinesses = new Vector<String>();
	Vector<GoodBusiness> allGood = new Vector<GoodBusiness>();
	Vector<ServiceBusiness> allService = new Vector<ServiceBusiness>();
	
	// Constructor
	public Jobs2(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		
		for(GoodBusiness business: PDI.getGoodBusinessOwned()) {
			owner.add(business.getName());
		}
		for(ServiceBusiness business: PDI.getServiceBusinessOwned()) {
			owner.add(business.getName());
		}
		
		for(Town town: PDI.getCountryResides().getTowns()) {
			for(GoodBusiness business:town.getGoodBusinesses()) {
				if (business.getEmployOffers().contains(player.getName())) {
					employoffers.add(business.getName());
				}
				if (business.getOwnerOffers().contains(player.getName())) {
					owneroffers.add(business.getName());
				}
				if (business.getEmployRequest().contains(player.getName())) {
					employrequests.add(business.getName());
				}
				if (business.getOwnerRequest().contains(player.getName())) {
					ownerrequests.add(business.getName());
				}
				if (business.getEmployees().contains(player.getName())) {
					employed.add(business.getName());
				}
				availablebusinesses.add(business.getName());
				allGood.add(business);
			}
			for(ServiceBusiness business:town.getServiceBusinesses()) {
				if (business.getEmployOffers().contains(player.getName())) {
					employoffers.add(business.getName());
				}
				if (business.getOwnerOffers().contains(player.getName())) {
					owneroffers.add(business.getName());
				}
				if (business.getEmployRequest().contains(player.getName())) {
					employrequests.add(business.getName());
				}
				if (business.getOwnerRequest().contains(player.getName())) {
					ownerrequests.add(business.getName());
				}
				if (business.getEmployees().contains(player.getName())) {
					employed.add(business.getName());
				}
				availablebusinesses.add(business.getName());
				allService.add(business);
			}
		}
	}
	public Jobs2(InspiredNations instance, Player playertemp, int errortemp, Vector<String> businessnamestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		businessnames = businessnamestemp;
		
		for(GoodBusiness business: PDI.getGoodBusinessOwned()) {
			owner.add(business.getName());
		}
		for(ServiceBusiness business: PDI.getServiceBusinessOwned()) {
			owner.add(business.getName());
		}
		
		for(Town town: PDI.getCountryResides().getTowns()) {
			for(GoodBusiness business:town.getGoodBusinesses()) {
				if (business.getEmployOffers().contains(player.getName())) {
					employoffers.add(business.getName());
				}
				if (business.getOwnerOffers().contains(player.getName())) {
					owneroffers.add(business.getName());
				}
				if (business.getEmployRequest().contains(player.getName())) {
					employrequests.add(business.getName());
				}
				if (business.getOwnerRequest().contains(player.getName())) {
					ownerrequests.add(business.getName());
				}
				if (business.getEmployees().contains(player.getName())) {
					employed.add(business.getName());
				}
				availablebusinesses.add(business.getName());
			}
			for(ServiceBusiness business:town.getServiceBusinesses()) {
				if (business.getEmployOffers().contains(player.getName())) {
					employoffers.add(business.getName());
				}
				if (business.getOwnerOffers().contains(player.getName())) {
					owneroffers.add(business.getName());
				}
				if (business.getEmployRequest().contains(player.getName())) {
					employrequests.add(business.getName());
				}
				if (business.getOwnerRequest().contains(player.getName())) {
					ownerrequests.add(business.getName());
				}
				if (business.getEmployees().contains(player.getName())) {
					employed.add(business.getName());
				}
				availablebusinesses.add(business.getName());
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
		String main = ChatColor.BOLD + "Jobs: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Incorrect number of arguments.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "You have not been offered employment there.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "You have not been offered ownership there.");
		}
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "You have not requested ownership there.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "You have not requested employment there.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED + "That business does not exist in this contry.");
		}
		if (error == 8) {
			errormsg = errormsg.concat(ChatColor.RED + "You have either already requested ownership, or are already owner of that business.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(businessnames) + ". ");
		}
		if (error == 10) {
			errormsg = errormsg.concat(ChatColor.RED + "You have either already reqeusted employment, or are already employed at that business.");
		}
			
		options = options.concat(ChatColor.YELLOW + "Owner Offers: " + ChatColor.GOLD + format(owneroffers) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Employment Offers: " + ChatColor.GOLD + format(employoffers) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat("Accept Ownership <business>" + repeat(" ", 44));
		options = options.concat("Accept Employment <business>" + repeat(" ", 43));
		options = options.concat("Reject Ownership <business>" + repeat(" ", 43));
		options = options.concat("Reject Employment <business>" + repeat(" ", 43));
		input.add("accept ownership");
		input.add("accept employment");
		input.add("reject ownership");
		input.add("reject employment");
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		boolean isGoodBusiness = true;
		GoodBusiness good = null;
		ServiceBusiness service = null;
		
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}

		// Back
		if (arg.equalsIgnoreCase("back")) {
			return new Jobs1(plugin, player, 0);
		}
		
		String[] args = arg.split(" ");
		if (args.length < 2) return new Jobs2(plugin, player, 1);
		if (!input.contains(args[0].toLowerCase() + " " + args[1].toLowerCase())) return new Jobs2(plugin, player, 1);
		
		// Accept Ownership
		if ((args[0] + " " + args[1]).equalsIgnoreCase("accept ownership")) {
			if (args.length < 3) return new Jobs2(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], owneroffers).size() == 1) args[2] = find(args[2], owneroffers).get(0);
			else if (find(args[2], owneroffers).size() == 0) return new Jobs2(plugin, player, 4);
			else return new Jobs2(plugin, player, 9, find(args[2], owneroffers));
			plugin.logger.info("Made it here 1");
			for(GoodBusiness i: allGood){
				if (i.getName().equalsIgnoreCase(args[2])) {
					good = i;
					break;
				}
			}
			plugin.logger.info("Made it here 2");
			for(ServiceBusiness i: allService) {
				if (i.getName().equalsIgnoreCase(args[2])) {
					service = i;
					isGoodBusiness = false;
					break;
				}
			}
			plugin.logger.info(good.getName());
			plugin.logger.info("Made it here 3");

			if (isGoodBusiness) {
				good.addOwner(player);
				PDI.addGoodBusinessOwned(good);
				PDI.setIsGoodBusinessOwner(true);
				good.removeOwnerOffer(player.getName());
				plugin.logger.info("Made it here 4");
			}
			else {
				service.addOwner(player);
				PDI.addServiceBusinessOwned(service);
				PDI.setIsServiceBusinessOwner(true);
				service.removeOwnerOffer(player.getName());
				plugin.logger.info("Made it here 5");
			}
			return new Jobs2(plugin, player, 0);
		}
		
		// Accept Employment
		if ((args[0] + " " + args[1]).equalsIgnoreCase("accept employment")) {
			if (args.length < 3) return new Jobs2(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], employoffers).size() == 1) args[2] = find(args[2], employoffers).get(0);
			else if (find(args[2], employoffers).size() == 0) return new Jobs2(plugin, player, 3);
			else return new Jobs2(plugin, player, 9, find(args[2], employoffers));
			
			for(GoodBusiness i: allGood){
				if (i.getName().equalsIgnoreCase(args[2])) {
					good = i;
					break;
				}
			}
			for(ServiceBusiness i: allService) {
				if (i.getName().equalsIgnoreCase(args[2])) {
					service = i;
					isGoodBusiness = false;
					break;
				}
			}
			if (isGoodBusiness) {
				good.addEmployee(player.getName());
				good.removeEmployOffer(player.getName());
			}
			else {
				service.addEmployee(player.getName());
				service.removeEmployOffer(player.getName());
			}
			return new Jobs2(plugin, player, 0);
		}
		
		// Reject Ownership
		if ((args[0] + " " + args[1]).equalsIgnoreCase("reject ownership")) {
			if (args.length < 3) return new Jobs2(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], owneroffers).size() == 1) args[2] = find(args[2], owneroffers).get(0);
			else if (find(args[2], owneroffers).size() == 0) return new Jobs2(plugin, player, 4);
			else return new Jobs2(plugin, player, 9, find(args[2], owneroffers));
			
			for(GoodBusiness i: allGood){
				if (i.getName().equalsIgnoreCase(args[2])) {
					good = i;
					break;
				}
			}
			for(ServiceBusiness i: allService) {
				if (i.getName().equalsIgnoreCase(args[2])) {
					service = i;
					isGoodBusiness = false;
					break;
				}
			}
			if (isGoodBusiness) {
				good.removeOwnerOffer(player.getName());
			}
			else {
				service.removeOwnerOffer(player.getName());
			}
			return new Jobs2(plugin, player, 0);
		}
		
		// Reject Employment
		if ((args[0] + " " + args[1]).equalsIgnoreCase("reject employment")) {
			if (args.length < 3) return new Jobs2(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], employoffers).size() == 1) args[2] = find(args[2], employoffers).get(0);
			else if (find(args[2], employoffers).size() == 0) return new Jobs2(plugin, player, 3);
			else return new Jobs2(plugin, player, 9, find(args[2], employoffers));
			
			for(GoodBusiness i: allGood){
				if (i.getName().equalsIgnoreCase(args[2])) {
					good = i;
					break;
				}
			}
			for(ServiceBusiness i: allService) {
				if (i.getName().equalsIgnoreCase(args[2])) {
					service = i;
					isGoodBusiness = false;
					break;
				}
			}
			if (isGoodBusiness) {
				good.removeEmployOffer(player.getName());
			}
			else {
				service.removeEmployOffer(player.getName());
			}
			return new Jobs2(plugin, player, 0);
		}
		
		return new Jobs2(plugin, player, 1);
	}



}
