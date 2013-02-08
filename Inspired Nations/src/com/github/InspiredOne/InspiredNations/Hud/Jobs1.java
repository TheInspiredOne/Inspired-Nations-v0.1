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

public class Jobs1 extends StringPrompt {

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
	
	// Constructor
	public Jobs1(InspiredNations instance, Player playertemp, int errortemp) {
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
	public Jobs1(InspiredNations instance, Player playertemp, int errortemp, Vector<String> businessnamestemp) {
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
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Incorrect number of arguments.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "You are not employed there.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "You are not an owner there.");
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
			
		options = options.concat(ChatColor.YELLOW + "Owner of: " + ChatColor.GOLD + format(owner) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Employee at: " + ChatColor.GOLD + format(employed) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Owner Requests: " + ChatColor.GOLD + format(ownerrequests) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Employment Requests: " + ChatColor.GOLD + format(employrequests) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat("Quit Owner <business>" + repeat(" ", 50));
		options = options.concat("Quit Employment <business>" + repeat(" ", 45));
		options = options.concat("Request Owner <business>" + repeat(" ", 45));
		options = options.concat("Request Employment <business>" + repeat(" ", 40));
		options = options.concat("Remove Owner Request <business>" + repeat(" ", 35));
		options = options.concat("Remove Employment Request <business>" + repeat(" ", 30));
		options = options.concat("Job Offers "  + ChatColor.GRAY + "(" + (owneroffers.size() + employoffers.size()) + ")"
				+ repeat(" ", 3));
		input.add("quit owner");
		input.add("quit employment");
		input.add("request owner");
		input.add("request employment");
		input.add("remove owner");
		input.add("remove employment");
		input.add("job offers");
		
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
			return new HudConversationMain(plugin, player, 0);
		}
		if (arg.equalsIgnoreCase("job offers")) {
			return new Jobs2(plugin, player, 0);
		}
		String[] args = arg.split(" ");
		if (args.length < 2) return new Jobs1(plugin, player, 1);
		if (!input.contains(args[0].toLowerCase() + " " + args[1].toLowerCase())) return new Jobs1(plugin, player, 1);
		
		// Quit Owner
		if ((args[0] + " " + args[1]).equalsIgnoreCase("quit owner")) {
			if (args.length < 3) return new Jobs1(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], owner).size() == 1) args[2] = find(args[2], owner).get(0);
			else if (find(args[2], owner).size() == 0) return new Jobs1(plugin, player, 4);
			else return new Jobs1(plugin, player, 9, find(args[2], owner));
			
			for(GoodBusiness i: PDI.getGoodBusinessOwned()){
				if (i.getName().equalsIgnoreCase(args[2])) {
					good = i;
					break;
				}
			}
			for(ServiceBusiness i: PDI.getServiceBusinessOwned()) {
				if (i.getName().equalsIgnoreCase(args[2])) {
					service = i;
					isGoodBusiness = false;
					break;
				}
			}
			if (isGoodBusiness) {
				if(good.getOwners().size() == 1) {
					
				}
				else {
					good.removeOwner(plugin.getServer().getPlayer(player.getName()));
					PDI.removeGoodBusinessOwned(good);
					if (PDI.getGoodBusinessOwned().size() == 0) {
						PDI.setIsGoodBusinessOwner(false);
					}
				}
			}
			else {
				if(service.getOwners().size() == 1) {
					
				}
				else {
					service.removeOwner(plugin.getServer().getPlayer(player.getName()));
					PDI.removeServiceBusinessOwned(service);
					if (PDI.getServiceBusinessOwned().size() == 0) {
						PDI.setIsServiceBusinessOwner(false);
					}
				}
			}
			return new Jobs1(plugin, player, 0);
		}
		
		// Quit Employment
		if ((args[0] + " " + args[1]).equalsIgnoreCase("quit employment")) {
			if (args.length < 3) return new Jobs1(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], employed).size() == 1) args[2] = find(args[2],employed).get(0);
			else if (find(args[2], employed).size() == 0) return new Jobs1(plugin, player, 3);
			else return new Jobs1(plugin, player, 9, find(args[2], employed));
			
			for (Town town: PDI.getCountryResides().getTowns()) {
				for(GoodBusiness i: town.getGoodBusinesses()){
					if (i.getName().equalsIgnoreCase(args[2])) {
						good = i;
						break;
					}
				}
				for(ServiceBusiness i: town.getServiceBusinesses()) {
					if (i.getName().equalsIgnoreCase(args[2])) {
						service = i;
						isGoodBusiness = false;
						break;
					}
				}
			}
			if (isGoodBusiness) {
				good.removeEmployee(player.getName());
			}
			else {
				service.removeEmployee(player.getName());
			}
			return new Jobs1(plugin, player, 0);
		}
		
		// Remove Owner Request
		if ((args[0] + " " + args[1]).equalsIgnoreCase("remove owner")) {
			if (args.length < 4) return new Jobs1(plugin, player, 2);
			
			String tempname = "";
			for(int i = 3; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[3]=tempname;
			
			if (find(args[3], ownerrequests).size() == 1) args[3] = find(args[3],ownerrequests).get(0);
			else if (find(args[3], ownerrequests).size() == 0) return new Jobs1(plugin, player, 5);
			else return new Jobs1(plugin, player, 9, find(args[3], ownerrequests));
			
			for (Town town: PDI.getCountryResides().getTowns()) {
				for(GoodBusiness i: town.getGoodBusinesses()){
					if (i.getName().equalsIgnoreCase(args[3])) {
						good = i;
						break;
					}
				}
				for(ServiceBusiness i: town.getServiceBusinesses()) {
					if (i.getName().equalsIgnoreCase(args[3])) {
						service = i;
						isGoodBusiness = false;
						break;
					}
				}
			}
			if (isGoodBusiness) {
				good.removeOwnerRequest(player.getName());
			}
			else {
				service.removeOwnerRequest(player.getName());
			}
			return new Jobs1(plugin, player, 0);
		}
		
		// Remove Employment Request
		if ((args[0] + " " + args[1]).equalsIgnoreCase("remove employment")) {
			if (args.length < 4) return new Jobs1(plugin, player, 2);
			
			String tempname = "";
			for(int i = 3; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[3]=tempname;
			
			if (find(args[3], employrequests).size() == 1) args[3] = find(args[3],employrequests).get(0);
			else if (find(args[3], employrequests).size() == 0) return new Jobs1(plugin, player, 6);
			else return new Jobs1(plugin, player, 9, find(args[3], employrequests));
			
			for (Town town: PDI.getCountryResides().getTowns()) {
				for(GoodBusiness i: town.getGoodBusinesses()){
					if (i.getName().equalsIgnoreCase(args[3])) {
						good = i;
						break;
					}
				}
				for(ServiceBusiness i: town.getServiceBusinesses()) {
					if (i.getName().equalsIgnoreCase(args[3])) {
						service = i;
						isGoodBusiness = false;
						break;
					}
				}
			}
			if (isGoodBusiness) {
				good.removeEmployRequest(player.getName());
			}
			else {
				service.removeEmployRequest(player.getName());
			}
			return new Jobs1(plugin, player, 0);
		}
		
		// Request Owner
		if ((args[0] + " " + args[1]).equalsIgnoreCase("request owner")) {
			if (args.length < 3) return new Jobs1(plugin, player, 2);
			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {
				tempname = tempname.concat(args[i] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			if (find(args[2], availablebusinesses).size() == 1) args[2] = find(args[2],availablebusinesses).get(0);
			else if (find(args[2], availablebusinesses).size() == 0) return new Jobs1(plugin, player, 7);
			else return new Jobs1(plugin, player, 9, find(args[2], availablebusinesses));
			
			for (Town town: PDI.getCountryResides().getTowns()) {
				for(GoodBusiness i: town.getGoodBusinesses()){
					if (i.getName().equalsIgnoreCase(args[2])) {
						good = i;
						break;
					}
				}
				for(ServiceBusiness i: town.getServiceBusinesses()) {
					if (i.getName().equalsIgnoreCase(args[2])) {
						service = i;
						isGoodBusiness = false;
						break;
					}
				}
			}
			if (isGoodBusiness) {
				if (good.getOwners().contains(player) || good.getOwnerRequest().contains(player.getName())) {
					return new Jobs1(plugin, player, 8);
				}
				if (good.getOwnerOffers().contains(player.getName())) {
					good.addOwner(player);
					PDI.setIsGoodBusinessOwner(true);
					PDI.addGoodBusinessOwned(good);
					good.removeOwnerOffer(player.getName());
				}
				else {
					good.addOwnerRequest(player.getName());
				}
			}
			else {
				if (service.getOwners().contains(player.getName()) || service.getOwnerRequest().contains(player.getName())) {
					return new Jobs1(plugin, player, 8);
				}
				if (service.getOwnerOffers().contains(player.getName())) {
					service.addOwner(player);
					PDI.setIsServiceBusinessOwner(true);
					PDI.addServiceBusinessOwned(service);
					service.removeOwnerOffer(player.getName());
				}
				else {
					service.addOwnerRequest(player.getName());
				}
			}
			return new Jobs1(plugin, player, 0);
		}
		
		// Request Employment
		if ((args[0] + " " + args[1]).equalsIgnoreCase("request employment")) {
			
			if (args.length < 3) return new Jobs1(plugin, player, 2);
			

			
			String tempname = "";
			for(int i = 2; i < args.length; i++) {

				tempname = tempname.concat(args[i] + " ");

			}

			tempname = tempname.substring(0, tempname.length()-1);
			args[2]=tempname;
			
			
			
			if (find(args[2], availablebusinesses).size() == 1) args[2] = find(args[2],availablebusinesses).get(0);
			else if (find(args[2], availablebusinesses).size() == 0) return new Jobs1(plugin, player, 7);
			else return new Jobs1(plugin, player, 9, find(args[2], availablebusinesses));
			
			
			for (Town town: PDI.getCountryResides().getTowns()) {
				for(GoodBusiness i: town.getGoodBusinesses()){
					if (i.getName().equalsIgnoreCase(args[2])) {
						good = i;
						break;
					}
				}
				for(ServiceBusiness i: town.getServiceBusinesses()) {
					if (i.getName().equalsIgnoreCase(args[2])) {
						service = i;
						isGoodBusiness = false;
						break;
					}
				}
			}
			

			
			if (isGoodBusiness) {
				if (good.getEmployees().contains(player.getName()) || good.getEmployRequest().contains(player.getName())) {
					return new Jobs1(plugin, player, 8);
				}
				if (good.getEmployOffers().contains(player.getName())) {
					good.addEmployee(player.getName());
					good.removeEmployOffer(player.getName());
				}
				else {
					good.addEmployRequest(player.getName());
				}
			}
			
			
			
			else {
				if (service.getEmployees().contains(player.getName()) || service.getEmployRequest().contains(player.getName())) {
					return new Jobs1(plugin, player, 8);
				}
				if (service.getEmployOffers().contains(player.getName())) {
					service.addEmployee(player.getName());
					service.removeEmployOffer(player.getName());
				}
				else {
					service.addEmployRequest(player.getName());
				}
			}
			
			return new Jobs1(plugin, player, 0);
		}
		return new Jobs1(plugin, player, 2);
	}


}
