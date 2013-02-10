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
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Prison.Local.LocalPrison;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageLocalPrison extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	LocalPrison prison;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public ManageLocalPrison(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		prison = town.getPrison();
	}
	
	public ManageLocalPrison(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		prison = town.getPrison();
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
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName()) && !prison.getBuilders().contains(nametest)) {
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
		String main = ChatColor.BOLD + "Manage Prison: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		input.add("back");
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
		
		int occupantssize = (int) ((town.getPrison().getOccupants().size() + "").length() * 1.4);
		int cellssize = (int) ((town.getPrison().getCells().size() + "").length() * 1.4);
		
		options = options.concat("Occupants: " + ChatColor.GOLD + town.getPrison().getOccupants().size() + ChatColor.YELLOW + repeat(" ", 66 - occupantssize));
		options = options.concat("Cells: " + ChatColor.GOLD + town.getPrison().getCells().size() + ChatColor.YELLOW + repeat(" ", 70 - cellssize));
		options = options.concat("Builders: " + ChatColor.GOLD + format(prison.getBuilders()) + " ");
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		
		
		options = options.concat(ChatColor.GREEN + "Claim Cell" + repeat(" ", 65));
		input.add("claim");
		if (town.getPrison().getCells().size() != 0) {
			options = options.concat("Delete Cell" + repeat(" ", 65));
			input.add("delete");
		}
		if (town.getPrison().getOccupants().size() != 0) {
			options = options.concat("Unjail Prisoner" + repeat(" ", 60));
			options = options.concat("Move Prisoner" + repeat(" ", 60));
			input.add("unjail");
			input.add("move");
		}
		options = options.concat("Add Builder <player>" + repeat(" ", 53));
		input.add("add");
		if (prison.getBuilders().size() > 0) {
			options = options.concat("Remove Builder <player>" + repeat( " ", 50));
			input.add("remove");
		}
		options = options.concat("Unclaim" + repeat(" ", 55));
		input.add("unclaim");
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new ManageLocalPrison(plugin, player, 1);
		if (args.length > 3) return new ManageLocalPrison(plugin, player, 2);
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			return new TownGovernmentRegions(plugin, player, 0);
		}
		
		// Add Builder
		if (args[0].equalsIgnoreCase("add")){
			if (find(args[2]).size() == 1) args[2] = find(args[2]).get(0);
			else if (find(args[2]).size() == 0) return new ManageLocalPrison(plugin, player, 6);
			else return new ManageLocalPrison(plugin, player, 9, find(args[2]));
			
			prison.addBuilder(args[2]);
			return new ManageLocalPrison(plugin, player, 0);
		}
		
		// Remove Builder
		if (args[0].equalsIgnoreCase("remove")) {
			if (find(args[2], prison.getBuilders()).size() == 1) args[2] = find(args[2], prison.getBuilders()).get(0);
			else if (find(args[2], prison.getBuilders()).size()== 0) return new ManageLocalPrison(plugin, player, 3);
			else return new ManageLocalPrison(plugin, player, 9, find(args[2], prison.getBuilders()));
			
			prison.removeBuilder(args[2]);
			return new ManageLocalPrison(plugin, player, 0);
		}
		
		// Claim Cell
		if (arg.equalsIgnoreCase("claim cell")) {
			return new ClaimCell(plugin, player, 0);
		}
		
		// Remove Cell
		if (arg.equalsIgnoreCase("delete cell")) {
			return new RemoveCell(plugin, player, 0);
		}
		
		// Remove Prison
		if (arg.equalsIgnoreCase("unclaim")) {
			town.setPrison(null);
			for(String name: town.getCoMayors()) {
				if(plugin.getServer().getPlayerExact(name).isConversing() && !name.equalsIgnoreCase(player.getName())) {
					plugin.playerdata.get(name).getConversation().abandon();
				}
			}
			if (plugin.getServer().getPlayerExact(town.getMayor()).isConversing() && !town.getMayor().equalsIgnoreCase(player.getName())) {
				plugin.playerdata.get(town.getMayor()).getConversation().abandon();
			}
			for(Player playertarget:plugin.getServer().getOnlinePlayers()) {
				PlayerMethods PM = new PlayerMethods(plugin, playertarget);
				PM.resetLocationBooleans();
			}
			return new TownGovernmentRegions(plugin, player, 0);
		}
		
		return new ManageLocalPrison(plugin, player, 0);
	}



}
