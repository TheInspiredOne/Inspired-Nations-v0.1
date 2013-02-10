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
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageTown extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error;
	Vector<String> suggestions = new Vector<String>();
	
	// Constructor
	public ManageTown(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
	}
	
	public ManageTown(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
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
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
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
		int townLength = (int) (town.getName().length() * 1.7);
		int popLength = (int) ((town.getResidents().size() + "").length()*1.4);
		int sizeLength = (int) ((town.getArea() + "").length()*1.4);
		int coRulerLength = (int) (((plugin.getConfig().getInt("min_comayors") - town.getCoMayors().size()) + "").length() * 1.4);
		String popPlural;
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Town: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		input.add("back");
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is already a mayor of your town.");
		}
		
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is not a resident of your town.");
		}
		
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "Put the name of the player you want to add as CoMayor.");
		}
		
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
		}
		
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player is the mayor of this town.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED+ "That town name is already taken.");
		}
		
		options = options.concat(ChatColor.BOLD + town.getName() + ChatColor.RESET + repeat(" ", 80 - townLength) + ChatColor.RESET + ChatColor.YELLOW);
		if (town.getResidents().size() == 1) popPlural = " Person";
		else popPlural = " People";
		options = options.concat("Population: " + ChatColor.GOLD + town.getResidents().size() + ChatColor.YELLOW + popPlural + repeat(" ", 55 - popLength));
		options = options.concat("Size: " + ChatColor.GOLD + town.getArea() + ChatColor.YELLOW + " Square Meters" + repeat(" ", 53 - sizeLength));

		
		if (town.getCoMayors().size() < plugin.getConfig().getInt("min_comayors")) {
			if (town.getArea() == 0) {
				options = options.concat(ChatColor.RED + "Until you have land, people can join your town by entering 'Manage Citizenship', selecting 'Join New Town' and then selecting your town from the list." + repeat(" ", 38));
			}
			options = options.concat(ChatColor.RED + "You need to add " + (plugin.getConfig().getInt("min_comayors") - town.getCoMayors().size()) + " Co-Mayors before you can claim land." + repeat(" ", 10 - coRulerLength));
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Add CoMayor <player>" + repeat(" ", 50));
			options = options.concat("Manage Finances" + repeat(" ", 60));
			options = options.concat("Transfer Leadership" + repeat(" ", 53));
			options = options.concat("Rename <name>" + repeat(" ", 61));
			options = options.concat("Protection Level" + repeat(" ", 58));
			input.add("add");
			input.add("rename");
			input.add("manage");
			input.add("transfer");
			input.add("protection");
			return space + main + options + end + errormsg;
		}

		else {
			if (town.getArea() == 0) {
				options = options.concat(ChatColor.RED + "Until you have land, people can join your town by entering 'Manage Citizenship', selecting 'Join New Town' and then selecting your town from the list." + repeat(" ", 38));
			}
			if (!town.hasTownHall() && town.getArea() != 0) {
				options = options.concat(ChatColor.RED + "You need to claim a town hall in order for people to join your town. Go into 'Government Regions' to select one. ");
			}
			options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
			options = options.concat("Add CoMayor <player>" + repeat(" ", 50));
			if (town.getCoMayors().size() >= plugin.getConfig().getInt("min_comayors")) {
				options = options.concat("Claim Land" + repeat(" ", 60));
				input.add("claim");
			}
			options = options.concat("Manage Finances" + repeat(" ", 60));
			options = options.concat("Transfer LeaderShip" + repeat(" ", 53));
			options = options.concat("Rename <name>" + repeat( " ", 61));
			options = options.concat("Protection Level" + repeat(" ", 58));
			if (town.getArea() != 0) {
				options = options.concat("Unclaim Land" + repeat(" ", 60));
				options = options.concat("Government Regions" + repeat(" ", 50));
				input.add("government");
				input.add("unclaim");
			}
			input.add("add");
			input.add("rename");
			input.add("manage");
			input.add("transfer");
			input.add("protection");

		}
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (!input.contains(args[0].toLowerCase())) return new ManageTown(plugin, player, 1);
		
		// back
		if (args[0].equalsIgnoreCase("back")) return new HudConversationMain(plugin, player, 0);
		
		// Manage Finances
		if (args[0].equalsIgnoreCase("manage")) return new ManageTownFinances(plugin, player, 0);
		
		// Transfer Leadership
		if (args[0].equalsIgnoreCase("Transfer")) return new TransferTownLeadership(plugin, player, 0);
		
		// Add CoMayor
		if (args[0].equalsIgnoreCase("Add") && args.length == 3) {
			
			Vector<String> names = find(args[2]);
			if (names.size() == 1) {
				args[2] = find(args[2]).get(0);
				if (town.getCoMayors().contains(args[2])) return new ManageTown(plugin, player, 2);
				if (town.getMayor().equalsIgnoreCase(args[2])) return new ManageTown(plugin, player, 6);
				PlayerData PDITarget = plugin.playerdata.get(args[2].toLowerCase());
				PDITarget.setIsTownMayor(true);
				PDITarget.setTownMayored(town);
				town.addCoMayor(args[2].toLowerCase());
				return new ManageTown(plugin, player, 0);
			}
			else if (names.size() == 0) return new ManageTown(plugin, player, 3);
			else return new ManageTown(plugin, player, 5, names);
		}
				
		else if (args[0].equalsIgnoreCase("Add") && args.length != 3) return new ManageTown(plugin, player, 4);
		
		// Claim Land
		if (args[0].equalsIgnoreCase("claim")) {
			PM.preTown(true);
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			return new ClaimTownLand(plugin, player, 0);
		}
		
		// Rename
		if (args[0].equalsIgnoreCase("rename")) {
			String tempname = "";
			for(int i = 0; i < args.length - 1; i++) {
				tempname = tempname.concat(args[i+1] + " ");
			}
			tempname = tempname.substring(0, tempname.length()-1);
			for(Town townname:PDI.getCountryResides().getTowns()) {
				if(townname.getName().equalsIgnoreCase(tempname)) {
					return new ManageTown(plugin, player, 7);
				}
			}
			town.setName(tempname);
			return new ManageTown(plugin, player, 0);
		}
		
		// Unclaim Land
		if (args[0].equalsIgnoreCase("Unclaim")) {
			PM.predetown(true);
			return new UnclaimTownLand(plugin, player, 0);
		}
		
		// Protection Level
		if (args[0].equalsIgnoreCase("protection")) {
			return new TownProtectionLevel(plugin, player, 0);
		}
		
		
		// Government Regions
		if (args[0].equalsIgnoreCase("Government")) return new TownGovernmentRegions(plugin, player, 0);
		
		
		return new ManageTown(plugin, player, 0);
	}
}
