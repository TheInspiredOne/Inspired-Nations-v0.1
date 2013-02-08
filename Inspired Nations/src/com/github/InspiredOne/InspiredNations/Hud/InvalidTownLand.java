package com.github.InspiredOne.InspiredNations.Hud;

import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;

public class InvalidTownLand extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Vector<String> input = new Vector<String>();
	int error;
	int error2;
	
	// Constructor
	public InvalidTownLand(InspiredNations instance, Player playertemp, int errortemp, int error2temp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		error2 = error2temp;
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
		String errormsg = ChatColor.RED + "";
		if (error2 == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		input.add("back");
		if (error == 1) {
			options = options.concat(ChatColor.RED + "The selection you made was a complex shape. This means that the sides of the shape crossed each other. This is not aloud." + repeat(" ", 4));

		}
		if (error == 2) {
			options = options.concat(ChatColor.RED + "The selection you contained another town." + repeat(" ", 4));

		}
		if (error == 3) {
			options = options.concat(ChatColor.RED + "The selection you made went outside of the country." + repeat(" ", 4));

		}
		if (error == 4) {
			options = options.concat(ChatColor.RED + "The selection you made was too small with an area of 0. Please select something larger." + repeat(" ", 4));

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
		if (!input.contains(arg.toLowerCase().trim())) return new InvalidTownLand(plugin, player, error, 1);
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			return new ManageTown(plugin, player, 0);
		}
		
		// reclaim
		if (arg.equalsIgnoreCase("reclaim")) {
			plugin.playermodes.get(player.getName().toLowerCase()).town(true);
			plugin.playermodes.get(player.getName().toLowerCase()).setPolygon(new polygonPrism(player.getWorld().getName()));
			return new ClaimTownLand(plugin, player, 0);
		}
		
		// cancel
		if (arg.equalsIgnoreCase("cancel")) {
			return new ManageTown(plugin, player, 0);
		}
		return new InvalidTownLand(plugin, player, error, 0);
	}





}
