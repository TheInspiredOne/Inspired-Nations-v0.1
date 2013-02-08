package com.github.InspiredOne.InspiredNations.Hud;

import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class JoinNewTown extends StringPrompt {


	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerMethods PMI;
	Vector<String> input = new Vector<String>();
	Vector<Town> newTown = new Vector<Town>();
	int error;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public JoinNewTown(InspiredNations instance, Player playertemp, Vector<Town> newtowns, int errortemp) {
		plugin = instance;
		player = playertemp;
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		newTown = newtowns;
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
	}
	
	public JoinNewTown(InspiredNations instance, Player playertemp, Vector<Town> newtowns, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		newTown = newtowns;
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
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
	
	// A method to find a town given an incomplete string;
	public Vector<String> findTown(String name) {
		
		Vector<String> list = new Vector<String>();
		for (Town town: newTown) {
			String nametest = town.getName();
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName())) {
				if (PDI.getIsTownResident()) {
					if (!PDI.getTownResides().getName().equalsIgnoreCase(nametest));
					list.add(nametest);
				}
				else list.add(nametest);

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
	
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Join New Town: Type the Town from the list you would like to join." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That town is not an option.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(names) + ". ");
		}
		for (int i = 0; i < newTown.size(); i++) {
			int length = (int) (newTown.get(i).getName().length() * 1.4);
			if (PDI.getIsTownResident()) {
				if (!PDI.getTownResides().getName().equals(newTown.get(i).getName())) {
					options = options.concat(newTown.get(i).getName() + repeat(" ", 79 - length));
					input.add(newTown.get(i).getName().toLowerCase());
				}
			}
			else {
				options = options.concat(newTown.get(i).getName() + repeat(" ", 79 - length));
				input.add(newTown.get(i).getName().toLowerCase());
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

		// back
		if (arg.equalsIgnoreCase("back")) return new ManageCitizenship(plugin, player, 0);
		
		Vector<String> townnames = findTown(arg);
		
		if (townnames.size() == 1) {
			arg = townnames.get(0);
			Town town = null;
			for (Town towntemp : PDI.getCountryResides().getTowns()) {
				if (towntemp.getName().equalsIgnoreCase(arg)) {
					town = towntemp;
				}
			}
			boolean sucess = PMI.transferTown(town);
			if (!sucess) return new JoinNewTown(plugin, player, newTown, 2);
			else if (sucess) return new ManageCitizenship(plugin, player, 0);
		}
		else if (townnames.size() == 0) return new JoinNewTown(plugin, player, newTown, 2);
		else return new JoinNewTown(plugin, player, newTown, 3, townnames);
		

		return null;
	}

}
