package com.github.InspiredOne.InspiredNations.Hud;

import java.util.Iterator;
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class TransferTownLeadership extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerMethods PMI;
	int error;
	Vector<String> suggestions = new Vector<String>();
	
	// Constructor
	public TransferTownLeadership(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		country = PDI.getCountryResides();
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
	}
	
	public TransferTownLeadership(InspiredNations instance, Player playertemp, int errortemp, Vector<String> suggestiontemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		country = PDI.getCountryResides();
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
		suggestions = suggestiontemp;
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
	
	// A method to find a person given an incomplete string;
	public Vector<String> find(String name) {
		Vector<String> list = new Vector<String>();
		Vector<String> players = town.getResidents();
		for (Iterator<String> i = players.iterator(); i.hasNext();) {
			String nametest = i.next();
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName())) {
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
	
	// A method to take a Vector<String> and return all the elements as a formated String.
	public String format(Vector<String> words) {
		String result = "";
		for (String i : words) {
			result = result.concat(i + ", ");
		}
		return result.substring(0, result.length() - 2);
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Transfer Leadership: Type what you would like to do." + ChatColor.RESET + " " + ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of this town.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "Not enough arguments.");
		}
		
		if (town.getResidents().size() == 1) {
			options = options.concat("Abandon Town" + repeat(" ", 57));
			input.add("abandon");
		}
		else {


			if (PDI.isCoMayor(player.getName())) {
				options = options.concat("Make <resident> CoMayor" + repeat(" ", 46));
				options = options.concat("Leave Town" + repeat(" ", 66));
				options = options.concat("Quit CoMayorship" + repeat(" ", 40));
				input.add("quit");
				input.add("make");
				input.add("leave");
			}
			else{
				options = options.concat("Make <resident> Mayor" + repeat(" ", 40));
				input.add("make");
			}
		}
		input.add("back");
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0])) return new TransferTownLeadership(plugin, player, 1);
		
		// Abandon
		if (args[0].equalsIgnoreCase("Abandon")) {
			PDI.setIsTownMayor(false);
			PDI.setTownMayored(null);
			PMI.leaveTown();
			country.removeTown(town);
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Leave
		if (args[0].equalsIgnoreCase("Leave")) {
			PDI.setIsTownMayor(false);
			PDI.setTownMayored(null);
			PMI.leaveTown();
			town.removeCoMayor(player.getName().toLowerCase());
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Quit
		if (args[0].equalsIgnoreCase("Quit")) {
			PDI.setIsTownMayor(false);
			PDI.setTownMayored(null);
			town.removeCoMayor(player.getName().toLowerCase());
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Make <player> Leader
		if (args[0].equalsIgnoreCase("Make") && args.length == 3) {
			
			if (args[0].equalsIgnoreCase("Make") && args.length == 3) {
				
				Vector<String> names = find(args[1]);
				if (names.size() == 1) {
					args[1] = find(args[1]).get(0);
					if (PDI.getTownMayored().getCoMayors().contains(player.getName().toLowerCase())) {
						PlayerData targetPDI = plugin.playerdata.get(args[1].toLowerCase());
						PDI.setIsTownMayor(false);
						PDI.setTownMayored(null);
						targetPDI.setIsTownMayor(true);
						targetPDI.setTownMayored(town);
						town.removeCoMayor(player.getName().toLowerCase());
						town.addCoMayor(args[1].toLowerCase());
						return new HudConversationMain(plugin, player, 0);

					}
					else {
						PlayerData targetPDI = plugin.playerdata.get((args[1].toLowerCase()));
						PDI.setIsTownMayor(false);
						PDI.setTownMayored(null);
						targetPDI.setIsTownMayor(true);
						targetPDI.setTownMayored(town);
						town.setMayor(args[1].toLowerCase());
						return new HudConversationMain(plugin, player, 0);
					}
				}
				else if (names.size() == 0) return new TransferTownLeadership(plugin, player, 3);
				else return new TransferTownLeadership(plugin, player, 2, names);
			}
		}
		
		// back
		if (args[0].equalsIgnoreCase("back")) return new ManageTown(plugin, player, 0);
		return new TransferTownLeadership(plugin, player, 4);
	}
}
