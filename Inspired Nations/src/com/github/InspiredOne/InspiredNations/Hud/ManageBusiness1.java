package com.github.InspiredOne.InspiredNations.Hud;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
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

public class ManageBusiness1 extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	Vector<String> businessnames = new Vector<String>();
	
	// Constructor
	public ManageBusiness1(InspiredNations instance, Player playertemp, int errortemp) {

		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;

	}
	public ManageBusiness1(InspiredNations instance, Player playertemp, int errortemp, Vector<String> businesstemp) {

		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		businessnames = businesstemp;

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
	
	// A method to find a business given an incomplete string;
	public Vector<String> find(String name) {
		Vector<String> list = new Vector<String>();
		Vector<GoodBusiness> GoodBusiness = PDI.getGoodBusinessOwned();
		Vector<ServiceBusiness> ServiceBusiness = PDI.getServiceBusinessOwned();
		for (GoodBusiness i: GoodBusiness) {
			String nametest = i.getName();
			if (nametest.toLowerCase().contains(name.toLowerCase())) {
				list.add(nametest);
				if (nametest.equalsIgnoreCase(name)) {
					list.clear();
					list.add(nametest);
					return list;
				}
			}
		}
		for (ServiceBusiness i: ServiceBusiness) {
			String nametest = i.getName();
			if (nametest.toLowerCase().contains(name.toLowerCase())) {
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
		String main = ChatColor.BOLD + "Manage Business: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		input.add("back");
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(businessnames) + ".");
		}
		options = options.concat(ChatColor.YELLOW + "Select the business you would like to manage." + repeat(" ", 20) + ChatColor.DARK_AQUA + repeat("-", 53));
		options = options.concat(ChatColor.GREEN + "");
		for (GoodBusiness i:PDI.getGoodBusinessOwned()) {
			options = options.concat(i.getName() + ", ");
		}
		for (ServiceBusiness i: PDI.getServiceBusinessOwned()) {
			options = options.concat(i.getName()+ ", ");
		}
		options = options.substring(0, options.length()-2).concat(" ");
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}

		// Back
		if (arg.equalsIgnoreCase("back")) {
			return new HudConversationMain(plugin, player, 0);
		}
		
		// Name of Business
		if (find(arg).size() == 1) {
			arg = find(arg).get(0);
			return new ManageBusiness2(plugin, player, 0, arg);
		}
		else if(find(arg).isEmpty()) {
			return new ManageBusiness1(plugin, player, 1);
		}
		else return new ManageBusiness1(plugin, player, 2, find(arg));
		
	}

}
