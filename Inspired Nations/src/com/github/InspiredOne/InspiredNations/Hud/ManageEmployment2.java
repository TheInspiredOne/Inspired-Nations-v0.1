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

public class ManageEmployment2 extends StringPrompt{

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
	public ManageEmployment2(InspiredNations instance, Player playertemp, int errortemp, String business) {
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
	public ManageEmployment2(InspiredNations instance, Player playertemp, int errortemp, String business, Vector<String> buildernamestemp) {
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
			errormsg = errormsg.concat(ChatColor.RED + "That player has not requested ownership.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "That player has not requested employment.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(buildernames) + ". ");
		}
		
		if (isGoodBusiness) {
			options = options.concat(ChatColor.YELLOW + "Owner Requests: " + ChatColor.GOLD + format(good.getOwnerRequest()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Employment Requests: " + ChatColor.GOLD + format(good.getEmployRequest()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Accept Owner <player>" + repeat(" ", 50));
			options = options.concat("Accept Employee <player>" + repeat(" ", 45));
			options = options.concat("Reject Owner <player>" + repeat(" ", 50));
			options = options.concat("Reject Employee <player>" + repeat(" ", 45));
			input.add("accept owner");
			input.add("accept employee");
			input.add("reject owner");
			input.add("reject employee");
		}
		
		else {
			options = options.concat(ChatColor.YELLOW + "Owner Requests: " + ChatColor.GOLD + format(service.getOwnerRequest()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat(ChatColor.YELLOW + "Employment Requests: " + ChatColor.GOLD + format(service.getEmployRequest()) + " " + ChatColor.GREEN);
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Accept Owner <player>" + repeat(" ", 50));
			options = options.concat("Accept Employee <player>" + repeat(" ", 45));
			options = options.concat("Reject Owner <player>" + repeat(" ", 50));
			options = options.concat("Reject Employee <player>" + repeat(" ", 45));
			input.add("accept owner");
			input.add("accept employee");
			input.add("reject owner");
			input.add("reject employee");
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

			return new ManageEmployment1(plugin, player, 0, name);
		}
		String[] args = arg.split(" ");
		if (args.length < 2) return new ManageEmployment2(plugin, player, 1, name);
		if (!input.contains(args[0].toLowerCase() + " " + args[1].toLowerCase())) return new ManageEmployment2(plugin, player, 1, name);
		
		// GOOD BUSINESS
		if(isGoodBusiness) {
			// Accept Owner
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("accept owner")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], good.getOwnerRequest()).size() == 1) args[2] = find(args[2], good.getOwnerRequest()).get(0);
				else if (find(args[2], good.getOwnerRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], good.getOwnerRequest()));
				
				PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
				targetPDI.addGoodBusinessOwned(good);
				targetPDI.setIsGoodBusinessOwner(true);
				good.addOwner(plugin.getServer().getPlayer(args[2]));

				good.removeOwnerRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
			
			// Accept Employee
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("accept employee")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], good.getEmployRequest()).size() == 1) args[2] = find(args[2], good.getEmployRequest()).get(0);
				else if (find(args[2], good.getEmployRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], good.getEmployRequest()));
				
				good.addEmployee(args[2]);
				good.removeEmployRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
			
			// Reject Owner
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("reject owner")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], good.getOwnerRequest()).size() == 1) args[2] = find(args[2], good.getOwnerRequest()).get(0);
				else if (find(args[2], good.getOwnerRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], good.getOwnerRequest()));
				
				good.removeOwnerRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
			
			// Reject Employee
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("reject employee")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], good.getEmployRequest()).size() == 1) args[2] = find(args[2], good.getEmployRequest()).get(0);
				else if (find(args[2], good.getEmployRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], good.getEmployRequest()));
				

				good.removeEmployRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
		}
		
		// SERVICE BUSINESS
		else {
			// Accept Owner
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("accept owner")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], service.getOwnerRequest()).size() == 1) args[2] = find(args[2], service.getOwnerRequest()).get(0);
				else if (find(args[2], service.getOwnerRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], service.getOwnerRequest()));
				
				PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
				targetPDI.addServiceBusinessOwned(service);
				targetPDI.setIsServiceBusinessOwner(true);
				
				service.addOwner(plugin.getServer().getPlayer(args[2]));
				service.removeOwnerRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
			
			// Accept Employee
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("accept employee")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], service.getEmployRequest()).size() == 1) args[2] = find(args[2], service.getEmployRequest()).get(0);
				else if (find(args[2], service.getEmployRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], service.getEmployRequest()));
				
				service.addEmployee(args[2]);
				service.removeEmployRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
			
			// Reject Owner
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("reject owner")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], service.getOwnerRequest()).size() == 1) args[2] = find(args[2], service.getOwnerRequest()).get(0);
				else if (find(args[2], service.getOwnerRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], service.getOwnerRequest()));
				
				service.removeOwnerRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
			
			// Reject Employee
			if ((args[0].toLowerCase() + " " + args[1].toLowerCase()).equalsIgnoreCase("reject employee")) {
				if (args.length != 3) return new ManageEmployment2(plugin, player, 2, name);
				if (find(args[2], service.getEmployRequest()).size() == 1) args[2] = find(args[2], service.getEmployRequest()).get(0);
				else if (find(args[2], service.getEmployRequest()).size() == 0) return new ManageEmployment2(plugin, player, 3, name);
				else return new ManageEmployment2(plugin, player, 9, name, find(args[2], service.getEmployRequest()));
				

				service.removeEmployRequest(args[2]);
				
				return new ManageEmployment2(plugin, player, 0, name);
			}
		}
		return new ManageEmployment2(plugin, player, 0, name);
	}
	
}
