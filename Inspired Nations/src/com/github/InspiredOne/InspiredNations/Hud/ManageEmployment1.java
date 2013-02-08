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
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageEmployment1 extends StringPrompt {

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
	public ManageEmployment1(InspiredNations instance, Player playertemp, int errortemp, String business) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = business;
		PM.legalChest = true;
		PM.legalInventory = false;
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
	public ManageEmployment1(InspiredNations instance, Player playertemp, int errortemp, String business, Vector<String> buildernamestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		name = business; 
		PM.legalChest = true;
		PM.legalInventory = false;
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
	public Vector<String> findOwnerOffers(String name) {
		Vector<String> players;
		if (isGoodBusiness) {
			players = PDI.getCountryResides().getResidents();
		}
		else {
			players = PDI.getCountryResides().getResidents();
		}
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
				if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !good.getOwnerOffers().contains(nametest) && !good.getOwners().contains(nametest)) {
					list.add(nametest);
					if (nametest.equalsIgnoreCase(name)) {
						list.clear();
						list.add(nametest);
						return list;
					}
				}
			}
			else {
				if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !service.getOwnerOffers().contains(nametest) && !service.getOwners().contains(nametest)) {
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
	
	// A method to find a person given an incomplete string;
	public Vector<String> findEmploymentOffers(String name) {
		Vector<String> players;
		if (isGoodBusiness) {
			players = PDI.getCountryResides().getResidents();
		}
		else {
			players = PDI.getCountryResides().getResidents();
		}
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
				if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !good.getEmployOffers().contains(nametest) && !good.getEmployees().contains(nametest)) {
					list.add(nametest);
					if (nametest.equalsIgnoreCase(name)) {
						list.clear();
						list.add(nametest);
						return list;
					}
				}
			}
			else {
				if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !service.getEmployOffers().contains(nametest) && !service.getEmployees().contains(nametest)) {
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
		String main = ChatColor.BOLD + "Manage Employment: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
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
			errormsg = errormsg.concat(ChatColor.RED + "That player has not been offered ownership.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "That player has not been offered employment.");
		}
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not an owner.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of this country, is already being offered ownership, or is already an owner.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of this country, is already being offered employment, or is already an employee.");
		}
		if (error == 8) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not an employee.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(buildernames) + ". ");
		}
		if (error == 11) {
			errormsg = errormsg.concat(ChatColor.RED + "You cannot fire yourself. Go to the Jobs menu in the main HUD.");
		}
		if (error == 12) {
			errormsg = errormsg.concat(ChatColor.RED + "Not enough arguments.");
		}
		if (isGoodBusiness) {
			options = options.concat(ChatColor.YELLOW + "Owners: " + ChatColor.GOLD + format(good.getOwners()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Employees: " + ChatColor.GOLD + format(good.getEmployees()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Owner Offers: " + ChatColor.GOLD + format(good.getOwnerOffers()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Employment Offers: " + ChatColor.GOLD + format(good.getEmployOffers()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Fire Owner <player>" + repeat(" ", 54));
			options = options.concat("Fire Employee <player>" + repeat(" ", 52));
			options = options.concat("Offer Ownership <player>" + repeat(" ", 46));
			options = options.concat("Offer Employment <player>" + repeat(" ", 46));
			options = options.concat("Remove Owner Offer <player>" + repeat(" ", 42));
			options = options.concat("Remove Employment Offer <player>" + repeat(" ", 36));
			options = options.concat("Employment Requests " + ChatColor.GRAY + "(" + (good.getEmployRequest().size() + good.getOwnerRequest().size()) + ")"
					+ ChatColor.GREEN + repeat(" ", 45));
			input.add("fire owner");
			input.add("fire employee");
			input.add("offer ownership");
			input.add("offer employment");
			input.add("remove owner");
			input.add("remove employment");
			input.add("employment requests");
			
		}
		else {
			options = options.concat(ChatColor.YELLOW + "Owners: " + ChatColor.GOLD + format(service.getOwners()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Employees: " + ChatColor.GOLD + format(service.getEmployees()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Owner Offers: " + ChatColor.GOLD + format(service.getOwnerOffers()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Employment Offers: " + ChatColor.GOLD + format(service.getEmployOffers()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Fire Owner <player>" + repeat(" ", 54));
			options = options.concat("Fire Employee <player>" + repeat(" ", 52));
			options = options.concat("Offer Ownership <player>" + repeat(" ", 46));
			options = options.concat("Offer Employment <player>" + repeat(" ", 46));
			options = options.concat("Remove Owner Offer <player>" + repeat(" ", 42));
			options = options.concat("Remove Employment Offer <player>" + repeat(" ", 36));
			options = options.concat("Employment Requests" + ChatColor.GRAY + "(" + (service.getEmployRequest().size() + service.getOwnerRequest().size()) + ")"
					+ ChatColor.GREEN + repeat(" ", 50));
			input.add("fire owner");
			input.add("fire employee");
			input.add("offer ownership");
			input.add("offer employment");
			input.add("remove owner");
			input.add("remove employment");
			input.add("employment requests");
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

			return new ManageBusiness2(plugin, player, 0, name);
		}
		
		// Employment Requests
		if (arg.equalsIgnoreCase("employment requests")) {

			return new ManageEmployment2(plugin, player, 0, name);
		}
		
		String[] args = arg.split(" ");
		if (args.length < 2) return new ManageEmployment1(plugin, player, 1, name);
		if (!input.contains(args[0].toLowerCase() + " " + args[1].toLowerCase())) return new ManageEmployment1(plugin, player, 1, name);
		
		// GOOD BUSINESS
		if (isGoodBusiness) {
			// Offer Ownership
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("offer ownership")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 12, name);
				if (findOwnerOffers(args[2]).size() == 1) args[2] = findOwnerOffers(args[2]).get(0);
				else if (findOwnerOffers(args[2]).size() == 0) return new ManageEmployment1(plugin, player, 6, name);
				else return new ManageEmployment1(plugin, player, 9, name, findOwnerOffers(args[2]));
				
				if (good.getOwnerRequest().contains(args[2])) {
					good.addOwner(plugin.getServer().getPlayer(args[2]));
					PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
					targetPDI.addGoodBusinessOwned(good);
					targetPDI.setIsGoodBusinessOwner(true);
					good.removeOwnerRequest(args[2]);
				}
				else {
					good.addOwnerOffer(args[2]);
				}
				return new ManageEmployment1(plugin, player, 0, name);
			}
			
			// Offer Employment
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("offer employment")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 12, name);
				if (findEmploymentOffers(args[2]).size() == 1) args[2] = findEmploymentOffers(args[2]).get(0);
				else if (findEmploymentOffers(args[2]).size() == 0) return new ManageEmployment1(plugin, player, 7, name);
				else return new ManageEmployment1(plugin, player, 9, name, findEmploymentOffers(args[2]));
				
				if (good.getEmployRequest().contains(args[2])) {
					good.addEmployee(args[2]);
					good.removeEmployRequest(args[2]);
				}
				else {
					good.addEmployOffer(args[2]);
				}
				return new ManageEmployment1(plugin, player, 0, name);
			}
			
			// Remove Ownership Offer
			if ((args[0] + " " + args[1]).equalsIgnoreCase("remove owner")) {
				if (args.length != 4) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[3], good.getOwnerOffers()).size() == 1) args[3] = find(args[3],good.getOwnerOffers()).get(0);
				else if (find(args[3], good.getOwnerOffers()).size() == 0) return new ManageEmployment1(plugin, player, 3, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[3], good.getOwnerOffers()));
				
				good.removeOwnerOffer(args[3]);
				return new ManageEmployment1(plugin, player, 0, name);
			}


			
			// Remove Employment Offer

			if ((args[0] + " " + args[1]).equalsIgnoreCase("remove employment")) {
				if (args.length != 4) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[3], good.getEmployOffers()).size() == 1) args[3] = find(args[3],good.getEmployOffers()).get(0);
				else if (find(args[3], good.getEmployOffers()).size() == 0) return new ManageEmployment1(plugin, player, 4, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[3], good.getEmployOffers()));
				
				good.removeEmployOffer(args[3]);
				return new ManageEmployment1(plugin, player, 0, name);
			}

			
			// Fire Owner
			if ((args[0] + " " + args[1]).equalsIgnoreCase("fire owner")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[2], good.getOwners()).size() == 1) args[2] = find(args[2],good.getOwners()).get(0);
				else if (find(args[2], good.getOwners()).size() == 0) return new ManageEmployment1(plugin, player, 5, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[2], good.getOwners()));
				
				if (args[2].equalsIgnoreCase(player.getName())) {
					return new ManageEmployment1(plugin, player, 11, name);
				}
				else {
					good.removeOwner(plugin.getServer().getPlayer(args[2]));
					PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
					targetPDI.removeGoodBusinessOwned(good);
					if (targetPDI.getGoodBusinessOwned().size() == 0) {
						targetPDI.setIsGoodBusinessOwner(false);
					}
					if (plugin.getServer().getPlayer(args[2]).isConversing()) {
						plugin.getServer().getPlayer(args[2]).abandonConversation(targetPDI.getConversation());
					}
					return new ManageEmployment1(plugin, player, 0, name);
				}
			}

		
			// Fire Employee

			if ((args[0] + " " + args[1]).equalsIgnoreCase("fire employee")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[2], good.getEmployees()).size() == 1) args[2] = find(args[2],good.getEmployees()).get(0);
				else if (find(args[2], good.getEmployees()).size() == 0) return new ManageEmployment1(plugin, player, 8, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[2], good.getEmployees()));
				
				good.removeEmployee(args[2]);
				return new ManageEmployment1(plugin, player, 0, name);

			}

		}
		
		
		// SERVICE BUSINESS
		else {
			// Offer Ownership
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("offer ownership")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 2, name);
				if (findOwnerOffers(args[2]).size() == 1) args[2] = findOwnerOffers(args[2]).get(0);
				else if (findOwnerOffers(args[2]).size() == 0) return new ManageEmployment1(plugin, player, 6, name);
				else return new ManageEmployment1(plugin, player, 9, name, findOwnerOffers(args[2]));
				
				
				if (service.getOwnerRequest().contains(args[2])) {
					service.addOwner(plugin.getServer().getPlayer(args[2]));
					PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
					targetPDI.addServiceBusinessOwned(service);
					targetPDI.setIsServiceBusinessOwner(true);
					service.removeOwnerRequest(args[2]);
				}
				else {
					service.addOwnerOffer(args[2]);
				}
				return new ManageEmployment1(plugin, player, 0, name);
			}
			
			// Offer Employment
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("offer employment")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 2, name);
				if (findEmploymentOffers(args[2]).size() == 1) args[2] = findEmploymentOffers(args[2]).get(0);
				else if (findEmploymentOffers(args[2]).size() == 0) return new ManageEmployment1(plugin, player, 7, name);
				else return new ManageEmployment1(plugin, player, 9, name, findEmploymentOffers(args[2]));
				
				
				if (service.getEmployRequest().contains(args[2])) {
					service.addEmployee(args[2]);
					service.removeEmployRequest(args[2]);
				}
				else {
					service.addEmployOffer(args[2]);
				}
				return new ManageEmployment1(plugin, player, 0, name);
			}
			
			// Remove Ownership Offer
			if ((args[0] + " " + args[1]).equalsIgnoreCase("remove owner")) {
				if (args.length != 4) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[3], service.getOwnerOffers()).size() == 1) args[3] = find(args[3],service.getOwnerOffers()).get(0);
				else if (find(args[3], service.getOwnerOffers()).size() == 0) return new ManageEmployment1(plugin, player, 3, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[3], service.getOwnerOffers()));
				
				service.removeOwnerOffer(args[3]);
				return new ManageEmployment1(plugin, player, 0, name);
			}

			
			// Remove Employment Offer
			if ((args[0] + " " + args[1]).equalsIgnoreCase("remove employment")) {
				if (args.length != 4) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[3], service.getEmployOffers()).size() == 1) args[3] = find(args[3],service.getEmployOffers()).get(0);
				else if (find(args[3], service.getEmployOffers()).size() == 0) return new ManageEmployment1(plugin, player, 4, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[3], service.getEmployOffers()));
				
				service.removeEmployOffer(args[3]);
				return new ManageEmployment1(plugin, player, 0, name);
			}

			
			// Fire Owner

			if ((args[0] + " " + args[1]).equalsIgnoreCase("fire owner")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[2], service.getOwners()).size() == 1) args[2] = find(args[2],service.getOwners()).get(0);
				else if (find(args[2], service.getOwners()).size() == 0) return new ManageEmployment1(plugin, player, 5, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[2], service.getOwners()));
				
				if (args[2].equalsIgnoreCase(player.getName())) {
					return new ManageEmployment1(plugin, player, 11, name);
				}
				else {
					service.removeOwner(plugin.getServer().getPlayer(args[2]));
					PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
					targetPDI.removeServiceBusinessOwned(service);
					if (targetPDI.getServiceBusinessOwned().size() == 0) {
						targetPDI.setIsServiceBusinessOwner(false);
					}
					if (plugin.getServer().getPlayer(args[2]).isConversing()) {
						plugin.getServer().getPlayer(args[2]).abandonConversation(targetPDI.getConversation());
					}
					return new ManageEmployment1(plugin, player, 0, name);
				}
			}

			
			// Fire Employee
			if ((args[0] + " " + args[1]).equalsIgnoreCase("fire employee")) {
				if (args.length != 3) return new ManageEmployment1(plugin, player, 2, name);
				if (find(args[2], service.getEmployees()).size() == 1) args[2] = find(args[2],service.getEmployees()).get(0);
				else if (find(args[2], service.getEmployees()).size() == 0) return new ManageEmployment1(plugin, player, 8, name);
				else return new ManageEmployment1(plugin, player, 9, name, find(args[2], service.getEmployees()));
				
				service.removeEmployee(args[2]);
				return new ManageEmployment1(plugin, player, 0, name);

			}
			
		}
		
		
		return new ManageEmployment1(plugin, player, 2, name);
	}
}