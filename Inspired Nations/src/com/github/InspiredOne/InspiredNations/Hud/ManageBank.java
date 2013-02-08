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
import com.github.InspiredOne.InspiredNations.Bank.Local.LocalBank;
import com.github.InspiredOne.InspiredNations.Hall.Local.LocalHall;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageBank extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error;
	LocalBank bank;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public ManageBank(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		bank = town.getBank();
	}
	
	public ManageBank(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		bank = town.getBank();
		names = namestemp;
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
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !bank.getBuilders().contains(nametest)) {
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
		String main = ChatColor.BOLD + "Manage Bank: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		input.add("back");
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}	
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Too many arguments.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a builder.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player does not exist on this server or is already a builder.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(names) + ". ");
		}
		if (error == 10) {
			errormsg = errormsg.concat(ChatColor.RED + "You put too few arguments.");
		}
		
		options = options.concat(ChatColor.YELLOW + "Builders: " + ChatColor.GOLD + format(bank.getBuilders())+" ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-",53) + ChatColor.GREEN);
		options = options.concat("Add Builder <player>" + repeat(" ", 50));
		if(bank.getBuilders().size() != 0 ){
			options = options.concat("Remove Builder <player>" + repeat(" ", 50));
			input.add("remove");
		}
		input.add("add");

		
		return space + main + options + end + errormsg;
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new ManageBank(plugin, player, 1);
		// back
		if (args[0].equalsIgnoreCase("back")){
			return new TownGovernmentRegions(plugin, player, 0);
		}
		
		if (args.length < 3) return new ManageBank(plugin, player, 10);
		if (args.length > 3) return new ManageBank(plugin, player, 2);
		
		// add
		if (args[0].equalsIgnoreCase("add")){
			if (find(args[2]).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2]).size() == 0) return new ManageBank(plugin, player, 6);
			else return new ManageBank(plugin, player, 9, find(args[2]));
			
			bank.addBuilder(args[2]);
			return new ManageBank(plugin, player, 0);
		}
		
		// remove
		if (args[0].equalsIgnoreCase("remove")) {
			if (find(args[2], bank.getBuilders()).size() == 1) args[2] = find(args[2], bank.getBuilders()).get(0);
			else if (find(args[2], bank.getBuilders()).size()== 0) return new ManageBank(plugin, player, 3);
			else return new ManageBank(plugin, player, 9, find(args[2], bank.getBuilders()));
			
			bank.removeBuilder(args[2]);
			return new ManageBank(plugin, player, 0);
		}

		return new ManageBank(plugin, player, 0);
	}
}
