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

public class TransferCountryLeadership extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Vector<String> input = new Vector<String>();
	PlayerMethods PMI;
	int error;
	Vector<String> suggestions = new Vector<String>();
	
	// Constructor
	public TransferCountryLeadership(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
	}
	public TransferCountryLeadership(InspiredNations instance, Player playertemp, int errortemp, Vector<String> suggestiontemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
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
		Vector<String> players = country.getResidents();
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
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of this country.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "Not enough arguments.");
		}
		if (country.getPopulation() == 1) {
			options = options.concat("Abandon Country" + repeat(" ", 55));
			input.add("abandon");
		}
		
		else {

			if (PDI.isCoRuler(player.getName().toLowerCase())){
				options = options.concat("Make <resident> CoRuler" + repeat(" ", 48));
				input.add("make");
				options = options.concat("Quit CoRulership" + repeat(" ", 58));
				input.add("quit");
				if (PDI.getIsTownMayor()) {
					options = options.concat(ChatColor.DARK_GRAY + "Leave Country" + ChatColor.GRAY + " Transfer Town Mayorship first." + repeat(" ", 1) + ChatColor.GREEN);
				}
				else {
					options = options.concat("Leave Country" + repeat(" " ,52));
					input.add("leave");
				}
			}
			else {
				options = options.concat("Make <resident> Ruler" + repeat(" ", 40));
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
		if (!input.contains(args[0])) return new TransferCountryLeadership(plugin, player, 1);
		
		// Abandon
		if (args[0].equalsIgnoreCase("Abandon") && country.getPopulation() == 1) {
			PDI.setIsCountryRuler(false);
			PDI.setCountryRuler(null);
			PDI.setIsTownMayor(false);
			PDI.setTownMayored(null);
			PMI.leaveCountry();
			for (int i = 0; i < country.getArea().Chunks.size(); i++) {
				plugin.chunks.remove(country.getArea().Chunks.get(i));
			}
			plugin.countrydata.remove(country.getName().toLowerCase());
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Leave
		if (args[0].equalsIgnoreCase("Leave")) {
			PDI.setIsCountryRuler(false);
			PDI.setCountryRuler(null);
			PMI.leaveCountry();
			country.removeCoRuler(player.getName().toLowerCase());
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Quit
		if (args[0].equalsIgnoreCase("Quit")) {
			PDI.setIsCountryRuler(false);
			PDI.setCountryRuler(null);
			country.removeCoRuler(player.getName().toLowerCase());
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Make <player> Leader
		if (args[0].equalsIgnoreCase("Make") && args.length == 3) {
			
			Vector<String> names = find(args[1]);
			if (names.size() == 1) {
				args[1] = find(args[1]).get(0);
				if (PDI.isCoRuler(player.getName())) {
					PlayerData targetPDI = plugin.playerdata.get(args[1].toLowerCase());
					PDI.setIsCountryRuler(false);
					PDI.setCountryRuler(null);
					targetPDI.setIsCountryRuler(true);
					targetPDI.setCountryRuler(country);
					country.removeCoRuler(player.getName().toLowerCase());
					country.addCoRuler(args[1].toLowerCase());
					return new HudConversationMain(plugin, player, 0);

				}
				else {
					PlayerData targetPDI = plugin.playerdata.get((args[1].toLowerCase()));
					PDI.setIsCountryRuler(false);
					PDI.setCountryRuler(null);
					targetPDI.setIsCountryRuler(true);
					targetPDI.setCountryRuler(country);
					country.setRuler(args[1].toLowerCase());
					return new HudConversationMain(plugin, player, 0);
				}
			}
			else if (names.size() == 0) return new TransferCountryLeadership(plugin, player, 3);
			else return new TransferCountryLeadership(plugin, player, 2, names);

			
		}
		
		// back
		if (args[0].equalsIgnoreCase("back")) {
			return new ManageCountry(plugin, player, 0);
		}
		return new TransferCountryLeadership(plugin, player, 4);
	}
}
