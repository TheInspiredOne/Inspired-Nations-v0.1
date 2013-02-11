package com.github.InspiredOne.InspiredNations.Hud;

import java.math.BigDecimal;
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

public class ManageCitizenship extends StringPrompt {
	
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	Vector<String> input = new Vector<String>();
	Vector<Country> newCountry = new Vector<Country>();
	Vector<Town> newTown = new Vector<Town>();
	PlayerMethods PMI;
	int error;
	Country countryin;
	Town townin;
	
	// Constructor
	public ManageCitizenship(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
		if (PDI.getIsInCountry()) {
			countryin = PDI.getCountryIn();
		}
		if (PDI.getIsInTown()) {
			townin = PDI.getTownIn();
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
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		boolean resides = false;
		for (Iterator<String> i = plugin.countrydata.keySet().iterator(); i.hasNext();) {
			Country temp = plugin.countrydata.get(i.next());
			if (PDI.getIsCountryResident()) {
				if(PDI.getCountryResides().getName().equals(temp.getName())) {
					resides = true;
				}
			}
			if (temp.size() == 0 && temp.getMoneyMultiplyer().compareTo(new BigDecimal(0)) != 0 && !resides && !temp.getPluralMoney().isEmpty() && !temp.getSingularMoney().isEmpty() && !PDI.getIsCountryRuler() && !PDI.getIsTownMayor()) newCountry.add(temp);			
		}
		resides = false;
		if (PDI.getIsCountryResident()) {
			for (Iterator<Town> i = PDI.getCountryResides().getTowns().iterator(); i.hasNext();) {
				Town town = i.next();
				if (PDI.getIsTownResident()) {
					if (PDI.getTownResides().getName().equals(town.getName())) resides = true;
				}
				if (town.getArea() == 0 && !PDI.getIsTownMayor() && !resides) newTown.add(town);
			}
		}
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Citizenship: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5) + ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		input.add("back");
		
		if (PDI.getIsCountryResident()) {
			int countryLength = (int) (PDI.getCountryResides().getName().length() * 1.4);
			options = options.concat(ChatColor.BOLD + "You currently reside in:" + ChatColor.RESET + repeat(" ", 40) + ChatColor.YELLOW);
			options = options.concat("Country: " + ChatColor.GOLD + PDI.getCountryResides().getName() + ChatColor.YELLOW + repeat(" ", 68 - countryLength));
			if (PDI.getIsTownResident()) {
				int townLength = (int) (PDI.getTownResides().getName().length() * 1.4);
				options = options.concat("Town: " + ChatColor.GOLD + PDI.getTownResides().getName() + ChatColor.YELLOW + repeat(" ", 74 - townLength));
			}
			if (PDI.getIsCountryRuler()) {
				options = options.concat(ChatColor.RED + "You need to transfer rulership of your Country before changing citizenship." + repeat(" ", 54));
			}
			if (PDI.getIsTownMayor()) {
				options = options.concat(ChatColor.RED + "You need to transfer mayorship of your Town before changing citizenship." + repeat(" ", 10));
			}
			if (!options.isEmpty() && (newCountry.size() != 0 || newTown.size() != 0 || (PDI.getIsCountryResident() && !PDI.getIsCountryRuler() && !PDI.getIsTownMayor()) || (PDI.getIsTownResident() && !PDI.getIsTownMayor()) || (PDI.getIsInFederalHall() && !PDI.getIsCountryRuler() && !PDI.getIsTownMayor() || (PDI.getIsInLocalHall() && PDI.getIsCountryResident() && !PDI.getIsCountryRuler() && !PDI.getIsTownMayor()))) ) {
				options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			}
		}
		options = options.concat(ChatColor.GREEN + "");
		if (newCountry.size() != 0) {
			options = options.concat("Join New Nation" + repeat(" ", 60));
			input.add("join new nation");
		}
		if (newTown.size() != 0) {
			options = options.concat("Join New Town"+ repeat(" ", 58));
			input.add("join new town");
		}
		if (PDI.getIsCountryResident() &&  !PDI.getIsCountryRuler() && !PDI.getIsTownMayor()) {
			options = options.concat("Leave Nation" + repeat(" ", 60));
			input.add("leave nation");
		}
		if (PDI.getIsTownResident() && !PDI.getIsTownMayor()) {
			options = options.concat("Leave Town" + repeat(" ", 60));
			input.add("leave town");
		}
		if (PDI.getIsInFederalHall() && !PDI.getIsCountryRuler() && !PDI.getIsTownMayor()) {
			if (PDI.getIsCountryResident()) {
				if (PDI.getCountryResides() != PDI.getCountryIn()) {
					options = options.concat("Join This Nation" + repeat(" ", 55));
					input.add("join this nation");
				}
			}
			else {
				options = options.concat("Join This Nation" + repeat(" ", 55));
				input.add("join this nation" );
			}
		}
		if (PDI.getIsInLocalHall() && PDI.getIsCountryResident() && !PDI.getIsCountryRuler() && !PDI.getIsTownMayor()) {
			if (PDI.getCountryResides() == PDI.getCountryIn()) {
				if(PDI.getIsTownResident() && !PDI.getIsTownMayor()) {
					if (PDI.getTownResides() != PDI.getTownIn()) {
						options = options.concat("Join This Town" + repeat(" ", 55));
						input.add("join this town");
					}
				}
				else {
					options = options.concat("Join This Town" + repeat(" ", 55));
					input.add("join this town");
				}

			}
		}
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new ManageCitizenship(plugin, player, 1);
		if (arg.equalsIgnoreCase("Join New Nation")) return new JoinNewNation(plugin, player, newCountry, 0);
		if (arg.equalsIgnoreCase("Join New Town")) return new JoinNewTown(plugin, player, newTown, 0);
		if (arg.equalsIgnoreCase("Leave Nation")) {
			PMI.leaveCountry();
		}
		if (arg.equalsIgnoreCase("Leave Town")) {
			PMI.leaveTown();
		}
		if (arg.equalsIgnoreCase("Join This Nation")) {
			PMI.transferCountry(countryin);
		}
		if (arg.equalsIgnoreCase("Join This Town")) {
			PMI.transferTown(townin);
		}
		if (arg.equalsIgnoreCase("back")) return new HudConversationMain(plugin, player, 0);
		return new ManageCitizenship(plugin, player, 0);
	}



}
