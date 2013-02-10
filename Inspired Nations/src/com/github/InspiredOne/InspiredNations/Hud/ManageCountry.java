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
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Country.CountryMethods;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageCountry extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Vector<String> input = new Vector<String>();
	int error;
	Vector<String> suggestions;
	CountryMethods countryMethods;
	
	// Constructor
	public ManageCountry(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = plugin.countrydata.get(PDI.getCountryRuled().getName().toLowerCase());
		error = errortemp;
		countryMethods = new CountryMethods(plugin, country);
	}
	public ManageCountry(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		suggestions = namestemp;
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
		int countryLength = (int) (country.getName().length() * 1.3);
		int popLength = (int) ((country.getPopulation() + "").length()*1.4);
		int sizeLength = (int) ((country.size() + "").length()*1.4);
		int minLength = (int) ((plugin.getConfig().getInt("min_population") + "").length() * 1.4);
		int coRulerLength = (int) (((plugin.getConfig().getInt("min_corulers") - country.getCoRulers().size()) + "").length() * 1.4);
		int maxClaimableChunks = countryMethods.getMaxClaimableChunks();
		int maxChunksLength = (int) ((maxClaimableChunks + "").length() * 1.4);
		String popPlural;
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Country: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = "";
		
		input.add("back");
		options = options.concat(ChatColor.BOLD + country.getName() + ChatColor.RESET + repeat(" ", 70 - countryLength) + ChatColor.RESET + ChatColor.YELLOW);
		if (country.getPopulation() == 1) popPlural = " Person";
		else popPlural = " People";
		options = options.concat("Population: " + ChatColor.GOLD + country.getPopulation() + ChatColor.YELLOW + popPlural + repeat(" ", 55 - popLength));
		options = options.concat("Size: " + ChatColor.GOLD + country.size() + " / " + maxClaimableChunks + ChatColor.YELLOW + " Chunks" + repeat(" ", 56 - sizeLength - maxChunksLength));
		if (country.getPluralMoney().isEmpty() || country.getSingularMoney().isEmpty() || country.getMoneyMultiplyer().equals(new BigDecimal(Math.PI))) {
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Setup Economy" + repeat (" ", 60));
			options = options.concat("Rename <name>" + repeat(" ", 61));
			options = options.concat("Transfer LeaderShip" + repeat(" ", 53));
			input.add("setup");
			input.add("transfer");
			input.add("rename");
			// Building the error message
			if (error == 1) {
				errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
			}
			if (error == 2) {
				errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of your country.");
			}
			if (error == 3) {
				errormsg = errormsg.concat(ChatColor.RED + "That player is already a coruler.");
			}
			if (error == 4) {
				errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
			}
			if (error == 5) {
				errormsg = errormsg.concat(ChatColor.RED + "Put the name of the player you want to add as CoMayor.");
			}
			if (error == 7) {
				errormsg = errormsg.concat(ChatColor.RED+ "That country name is already taken.");
			}
			return space + main + options + end + errormsg;
		}
		
		if (country.getPopulation() < plugin.getConfig().getInt("min_population")) {
			options = options.concat(ChatColor.RED + "You need at least a population of " + plugin.getConfig().getInt("min_population") + " to claim land." + repeat(" ", 17 - minLength));
			options = options.concat("People can join your country by going into 'Manage Citizenship', selecting 'Join New Nation', and selecting your nation from the list. ");
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Manage Economy" + repeat(" ", 60));
			options = options.concat("Transfer LeaderShip" + repeat(" ", 53));
			options = options.concat("Rename <name>" + repeat(" ", 61));
			options = options.concat("Protection Level" + repeat(" ", 58));
			input.add("manage");
			input.add("transfer");
			input.add("protection");
			input.add("rename");
			// Building the error message
			if (error == 1) {
				errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
			}
			if (error == 2) {
				errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of your country.");
			}
			if (error == 3) {
				errormsg = errormsg.concat(ChatColor.RED + "That player is already a coruler.");
			}
			if (error == 4) {
				errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
			}
			if (error == 5) {
				errormsg = errormsg.concat(ChatColor.RED + "Put the name of the player you want to add as CoMayor.");
			}
			if (error == 7) {
				errormsg = errormsg.concat(ChatColor.RED+ "That country name is already taken.");
			}
			return space + main + options + end + errormsg;
		}
		if (country.getCoRulers().size() < plugin.getConfig().getInt("min_corulers")) {
			options = options.concat(ChatColor.RED + "You need to add " + (plugin.getConfig().getInt("min_corulers") - country.getCoRulers().size()) + " Co-Rulers before you can claim land." + repeat(" ", 10 - coRulerLength));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Add CoRuler <player>" + repeat(" ", 50));
			options = options.concat("Manage Economy" + repeat(" ", 60));
			options = options.concat("Transfer LeaderShip" + repeat(" ", 53));
			options = options.concat("Rename <name>" + repeat(" ", 61));
			options = options.concat("Protection Level" + repeat(" ", 58));
			input.add("add");
			input.add("manage");
			input.add("transfer");
			input.add("protection");
			input.add("rename");
			// Building the error message
			if (error == 1) {
				errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
			}
			if (error == 2) {
				errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of your country.");
			}
			if (error == 3) {
				errormsg = errormsg.concat(ChatColor.RED + "That player is already a coruler.");
			}
			if (error == 4) {
				errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
			}
			if (error == 5) {
				errormsg = errormsg.concat(ChatColor.RED + "Put the name of the player you want to add as CoMayor.");
			}
			if (error == 7) {
				errormsg = errormsg.concat(ChatColor.RED+ "That country name is already taken.");
			}
			return space + main + options + end + errormsg;
		}
		if (country.getPopulation() >= plugin.getConfig().getInt("min_population")) {
			if (!country.hasCapital() && country.size() != 0) {
				options = options.concat(ChatColor.RED + "People cannot join your country until you have a capital. Select your capital using 'Government Regions'. ");
			}
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Add CoRuler <player>" + repeat(" ", 50));
			options = options.concat("Claim Land" + repeat(" ", 60));
			input.add("claim");
			input.add("add");

		}
		if (country.size() != 0) {
			options = options.concat("Unclaim Land" + repeat(" ", 60));
			options = options.concat("Government Regions" + repeat(" ", 53));
			input.add("government");
			input.add("unclaim");
		}
		options = options.concat("Manage Economy" + repeat(" ", 60));
		options = options.concat("Transfer LeaderShip" + repeat(" ", 53));
		options = options.concat("Rename <name>" + repeat(" ", 61));
		options = options.concat("Protection Level" + repeat(" ", 58));
		input.add("manage");
		input.add("rename");
		input.add("transfer");
		input.add("protection");
		
		// Building the error message
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of your country.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is already a coruler.");
		}
		
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
		}
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "Put the name of the player you want to add as CoMayor.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is the ruler of this country.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED+ "That country name is already taken.");
		}
		return space + main + options + end + errormsg;
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new ManageCountry(plugin, player, 1);
		
		// Setup Economy (for when the economy is not finished)
		if (arg.equalsIgnoreCase("Setup Economy")) {
			if (country.getPluralMoney().isEmpty() || country.getSingularMoney().isEmpty()) return new NewCountry2(plugin, player, country, 0);
			else return new NewCountry3(plugin, player, country, 0);
		}
		
		// Add CoRuler
		if (args[0].equalsIgnoreCase("Add") && args.length == 3) {
			
			Vector<String> names = find(args[2]);
			if (names.size() == 1) {
				args[2] = find(args[2]).get(0);
				if (country.getCoRulers().contains(args[2].toLowerCase())) return new ManageCountry(plugin,player, 3);
				if (country.getRuler().equalsIgnoreCase(args[2])) return new ManageCountry(plugin, player, 6);
				country.addCoRuler(args[2].toLowerCase());
				PlayerData targetPDI = plugin.playerdata.get(args[2].toLowerCase());
				targetPDI.setIsCountryRuler(true);
				targetPDI.setCountryRuler(country);
				return new ManageCountry(plugin, player, 0);
			}
			else if (names.size() == 0) return new ManageCountry(plugin, player, 2);
			else return new ManageCountry(plugin, player, 4, names);
			
		}
		else if (args[0].equalsIgnoreCase("Add") && args.length != 3) return new ManageCountry(plugin, player, 5);
		
		// Rename
		if (args[0].equalsIgnoreCase("rename")) {
			String tempname = "";
			for(int i = 0; i < args.length - 1; i++) {
				tempname = tempname.concat(args[i+1] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			for(String countryname:plugin.countrydata.keySet()) {
				if(countryname.equalsIgnoreCase(tempname)) {
					return new ManageCountry(plugin, player, 7);
				}
			}
			plugin.countrydata.remove(country.getName());
			country.setName(tempname);
			plugin.countrydata.put(tempname.toLowerCase(), country);
			return new ManageCountry(plugin, player, 0);
		}
		
		// Manage Economy
		if (args[0].equalsIgnoreCase("Manage")) {
			return new ManageCountryEconomy(plugin, player, 0);
		}
		// Claim Land
		if (args[0].equalsIgnoreCase("Claim")) {
			plugin.playermodes.get(player.getName().toLowerCase()).preCountry(true);
			return new ClaimLand(plugin, player, 0);
		}
		
		// Unclaim Land
		if (args[0].equalsIgnoreCase("Unclaim")) {
			plugin.playermodes.get(player.getName().toLowerCase()).predecountry(true);
			return new UnclaimLand(plugin, player, 0);
		}
		
		// Transfer Leadership
		if (args[0].equalsIgnoreCase("Transfer")) {
			return new TransferCountryLeadership(plugin, player, 0);
		}
		
		// Protection Level
		if (args[0].equalsIgnoreCase("Protection")) {
			return new CountryProtectionLevel(plugin, player, 0);
		}
		
		// back
		if (arg.equalsIgnoreCase("back")) return new HudConversationMain(plugin, player, 0);

		// Government Regions
		if (args[0].equalsIgnoreCase("Government")){
			return new CountryGovernmentRegions(plugin, player, 0);
		}
		
		return new ManageCountry(plugin, player, 0);
	}
}
