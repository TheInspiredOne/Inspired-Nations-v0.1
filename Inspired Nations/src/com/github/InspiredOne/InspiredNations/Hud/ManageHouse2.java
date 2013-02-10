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
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageHouse2 extends StringPrompt {

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
	public ManageHouse2(InspiredNations instance, Player playertemp, int errortemp, String housename) {
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
	public ManageHouse2(InspiredNations instance, Player playertemp, int errortemp, String housename, Vector<String> buildertemp) {
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
		String main = ChatColor.BOLD + "Manage House: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That house name is already being used in this country.");
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
		options = options.concat(ChatColor.YELLOW + " " + ChatColor.BOLD + house.getName() + ChatColor.RESET + repeat(" ", 70 - (int)(house.getName().length()*1.4)) + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		options = options.concat(ChatColor.YELLOW + "Builders: " + ChatColor.GOLD + format(house.getBuilders()) + " " + ChatColor.GREEN);
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		
		options = options.concat("Rename <name>" + repeat(" ",60));
		options = options.concat("Manage Owners" + repeat(" ", 57));
		options = options.concat("Add Builder <player>" + repeat(" ", 50));
		if (house.getBuilders().size() > 0) {
			options = options.concat("Remove Builder <player>" + repeat( " ", 50));
			input.add("remove");
		}
		options = options.concat("Reclaim Land" + repeat(" ", 60));
		options = options.concat("Protection Level" + repeat(" ", 55));
		options = options.concat("");
		input.add("rename");
		input.add("reclaim land");
		input.add("builder");
		input.add("manage");
		input.add("remove");
		input.add("protection");
		
		
		return space + main+options + end + errormsg;
	}
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String tempname = "";
		String[] args = arg.split(" ");
		if (args.length < 2 && !arg.equalsIgnoreCase("back")) return new ManageHouse2(plugin, player, 1, name);
		if (!(input.contains(arg.toLowerCase()) || input.contains(args[0].toLowerCase()) || input.contains(args[1].toLowerCase()))) return new ManageHouse2(plugin, player, 1, name);
		// Back
		if (arg.equalsIgnoreCase("back")) {
			if (PDI.getHouseOwned().size() == 1) {
				return new HudConversationMain(plugin, player, 0);
			}
			else return new ManageHouse1(plugin, player, 0);
		}
		// Reclaim
		if (arg.equalsIgnoreCase("reclaim land")) {
			PM.reSelectHouse = true;
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.house(true);
			return new ReclaimHouse1(plugin, player, 0, house);
		}
		// Rename
		if (args[0].equalsIgnoreCase("rename")) {
			for(int i = 0; i < args.length - 1; i++) {
				tempname = tempname.concat(args[i+1] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			for(Town towntest: PDI.getCountryResides().getTowns()) {
				for (House housetest: towntest.getHouses()) {
					if (housetest.getName().equalsIgnoreCase(tempname) && !housetest.equals(house)) {
						return new ManageHouse2(plugin, player, 2, name);
					}
				}	
			}
			house.setName(tempname);
			return new ManageHouse2(plugin, player, 0, tempname);
		}
		// Add Builder
		if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("builder")) {
			if (find(args[2]).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2]).size() == 0) return new ManageHouse2(plugin, player, 6, name);
			else return new ManageHouse2(plugin, player, 9, name, find(args[2]));
			
			house.addBuilder(args[2]);
			return new ManageHouse2(plugin, player, 0, name);
		}
		// Protection Level
		if (args[0].equalsIgnoreCase("protection")) {
			return new GeneralProtectionLevel(plugin, player, 0, "house", house);
		}
		// Remove Builder
		if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("builder")) {
			if (find(args[2], house.getBuilders()).size() == 1) args[2] = find(args[2], house.getBuilders()).get(0);
			else if (find(args[2], house.getBuilders()).size()== 0) return new ManageHouse2(plugin, player, 3, name);
			else return new ManageHouse2(plugin, player, 9, name, find(args[2], house.getBuilders()));
			
			house.removeBuilder(args[2]);
			return new ManageHouse2(plugin, player, 0, name);
		}
		
		// Manage Owners
		if (args[0].equalsIgnoreCase("manage") && args[1].equalsIgnoreCase("owners")) {
			return new ManageHouseOwners(plugin, player, 0, name);
		}
		return new ManageHouse2(plugin, player, 1, name);
	}
}
