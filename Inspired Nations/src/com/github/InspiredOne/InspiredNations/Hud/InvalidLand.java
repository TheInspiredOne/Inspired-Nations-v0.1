package com.github.InspiredOne.InspiredNations.Hud;

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
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.House.ClaimHousePlayerListener;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;

public class InvalidLand extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	Country country;
	Vector<String> input = new Vector<String>();
	int error;
	int error2;
	String from;
	House house;
	GoodBusiness goodbusiness;
	ServiceBusiness servicebusiness;
	
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, int error2temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		error2 = error2temp;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, House housetemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		house = housetemp;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, House housetemp, int error2temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		error2 = error2temp;
		house = housetemp;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		error2 = 0;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, GoodBusiness temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		goodbusiness = temp;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, GoodBusiness temp, int error2temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		error2 = error2temp;
		goodbusiness = temp;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, ServiceBusiness temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		servicebusiness = temp;
	}
	// Constructor
	public InvalidLand(InspiredNations instance, Player playertemp, int errortemp, String fromtemp, ServiceBusiness temp, int error2temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		from = fromtemp;
		error2 = error2temp;
		servicebusiness = temp;
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
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Invalid Selection: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to Manage Town." + repeat(" ", 5);
		if (from.equals("house")) {
			end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to HUD." + repeat(" ", 5);
		}
		String errormsg = ChatColor.RED + "";
		if (error2 == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		input.add("back");
		
		if (error == 1) {
			options = options.concat(ChatColor.RED + "The selection you made was a complex shape. This means that the sides of the shape crossed each other." + repeat(" ", 1));

		}
		if (error == 2) {
			if (from.equalsIgnoreCase("federalpark")) {
				options = options.concat(ChatColor.RED + "The selection you made contained another Federal Park." + repeat(" ", 1));
			}
			else {
				options = options.concat(ChatColor.RED + "The selection you made contained another region." + repeat(" ", 4));
			}
		}
		if (error == 3) {
			if (from.equalsIgnoreCase("federalpark")) {
				options = options.concat(ChatColor.RED + "The selection you made went outside of the country." + repeat(" ", 2));
			}
			else {
				options = options.concat(ChatColor.RED + "The selection you made went outside of the town." + repeat(" ", 4));
			}
		}
		if (error == 4) {
			options = options.concat(ChatColor.RED + "The selection you made was too small with a volume of 0. Please select something larger." + repeat(" ", 4));

		}
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat("Reclaim" + repeat(" ", 65));
		options = options.concat("Cancel" + repeat(" ", 60));
		input.add("reclaim");
		input.add("cancel");
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase().trim())) return new InvalidLand(plugin, player, error, from, house, 1);
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			PM.setBlocksBack();
			PM.goodBusiness(false);
			PM.serviceBusiness(false);
			PM.house(false);
			PM.town(false);
			PM.localBank(false);
			PM.localHall(false);
			PM.localPrison(false);
			PM.park(false);
			PM.country(false);
			PM.federalPark(false);
			PM.reSelectHouse = false;
			PM.reSelectGoodBusiness = false;
			PM.reSelectServiceBusiness = false;
			if (from.equals("house") || from.equals("serviceBusiness") || from.equals("goodbusiness")) {
				return new HudConversationMain(plugin, player, 0);
			}
			else if(from.equals("rehouse")) {
				return new ManageHouse2(plugin, player, 0, house.getName());
			}
			else if(from.equals("regoodbusiness")) {
				return new ManageBusiness2(plugin, player, 0, goodbusiness.getName());
			}
			else if(from.equals("reserviceBusiness")) {
				return new ManageBusiness2(plugin, player, 0, servicebusiness.getName());
			}
			else if(from.equals("federalpark")) {
				return new CountryGovernmentRegions(plugin, player, 0);
			}
			else {
				return new TownGovernmentRegions(plugin, player, 0);
			}
		}
		
		// reclaim
		if (arg.equalsIgnoreCase("reclaim")) {
			plugin.playermodes.get(player.getName().toLowerCase()).setPolygon(new polygonPrism(player.getWorld().getName()));
			plugin.playermodes.get(player.getName().toLowerCase()).setCuboid(new Cuboid(player.getWorld().getName()));
			if (from.equals("localhall")) {
				PM.localHall(true);
				return new ClaimLocalHall2(plugin, player, 0);
			}
			else if(from.equals("localbank")) {
				PM.localBank(true);
				return new ClaimLocalBank2(plugin, player, 0);
			}
			else if(from.equals("park")) {
				PM.park(true);
				return new ClaimPark2(plugin, player, 0);
			}
			else if(from.equals("hospital")) {
				PM.hospital(true);
				return new ClaimHospital2(plugin, player, 0);
			}
			else if (from.equals("localprison")) {
				PM.localPrison(true);
				return new ClaimLocalPrison2(plugin, player, 0);
			}
			else if (from.equals("federalpark")) {
				PM.federalPark(true);
				return new ClaimFederalPark2(plugin, player, 0);
			}
			else if (from.equals("house")) {
				PM.house(true);
				return new NewHouse2(plugin, player, 0);
			}
			else if (from.equals("rehouse")) {
				PM.house(true);
				return new ReclaimHouse2(plugin, player, 0, house);
			}
			else if (from.equals("serviceBusiness")) {
				PM.serviceBusiness(true);
				return new NewServiceBusiness2(plugin, player, 0);
			}
			else if (from.equals("goodbusiness")) {
				PM.goodBusiness(true);
				return new NewGoodBusiness2(plugin, player, 0);
			}
			else if (from.equals("regoodbusiness")) {
				PM.goodBusiness(true);
				return new ReclaimGoodBusiness2(plugin, player, 0, goodbusiness);
			}
			else if (from.equals("reserviceBusiness")) {
				PM.serviceBusiness(true);
				return new ReclaimServiceBusiness2(plugin, player, 0, servicebusiness);
			}
		}

		// cancel
		if (arg.equalsIgnoreCase("cancel")) {
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			if (from.equals("house") || from.equals("serviceBusiness") || from.equals("goodbusiness")) {
				return new HudConversationMain(plugin, player, 0);
			}
			else if (from.equals("rehouse")) {
				PM.reSelectHouse = false;
				return new ManageHouse2(plugin, player, 0, house.getName());
			}
			else if (from.equals("regoodbusiness") || from.equals("reserviceBusiness")) {
				PM.reSelectGoodBusiness = false;
				PM.reSelectServiceBusiness = false;
				return new ManageBusiness2(plugin, player, 0, goodbusiness.getName());
			}
			else if(from.equals("federalpark")) {
				return new CountryGovernmentRegions(plugin, player, 0);
			}
			else {
				return new TownGovernmentRegions(plugin, player, 0);
			}
		}
		return new InvalidLand(plugin, player, error, from);
	}
}
