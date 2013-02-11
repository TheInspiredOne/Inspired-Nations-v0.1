package com.github.InspiredOne.InspiredNations.Hud;

import java.math.BigDecimal;
import java.util.HashMap;
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

public class ManageMoney extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerMethods PMI;
	HashMap<String, Integer> inputs = new HashMap<String, Integer>();
	int error;
	Vector<String> names = new Vector<String>();
	
	// Constructor
	public ManageMoney(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
	}
	
	public ManageMoney(InspiredNations instance, Player playertemp, int errortemp, Vector<String> namestemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
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
	
	// A method to cut off decimals greater than the hundredth place;
	public double cut(double x) {
		int y;
		y = (int) (x*100);
		return y/100.0;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_DOWN);
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
		int moneylength = (int) (cut(PDI.getMoney()).toString().length()* 1.4);
		int onhandlength = (int) (cut(PDI.getMoneyOnHand()).toString().length()* 1.4);
		int inbanklength = (int) (cut(PDI.getMoneyInBank()).toString().length()* 1.4);
		int housetaxlength = (int) ((cut(PMI.houseTax())).toString().length() * 1.4);
		int goodtaxlength = (int) ((cut(PMI.goodBusinessTax())).toString().length() * 1.4);
		int servicetaxlength = (int) ((cut(PMI.serviceBusinessTax())).toString().length() * 1.4);
		int totaltaxlength = (int) ((cut(PMI.taxAmount())).toString().length() * 1.4);
		int loanlength = (int) (cut(PDI.getLoanAmount()).toString().length() * 1.4);
		int maxLoanLength = (int) ((cut(PDI.getMaxLoan()).toString().length()) * 1.4);
		String options = "";
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Money: Type what you would like to do." + ChatColor.RESET + repeat(" ", 10)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Too many arguments.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "You do not have that much money on hand.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "Only numbers are allowed as the argument. Do not use letters.");
		}
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "You do not have that much money in the bank.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That player does not exist on this server.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED + "That exceeds the maximum loan.");
		}
		if (error == 8) {
			errormsg = errormsg.concat(ChatColor.RED + "You do not have that much loan to pay back.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(names) + ". ");
		}
		if (error == 10) {
			errormsg = errormsg.concat(ChatColor.RED + "Not enough arguments.");
		}
		
		// Building options
		options = options.concat(ChatColor.BOLD + "You have:" + ChatColor.RESET +  repeat(" ", 65)) + ChatColor.YELLOW;
		options = options.concat(ChatColor.GOLD + "" + cut(PDI.getMoney()).toString() + ChatColor.YELLOW + " " + PDI.getPluralMoney() + " in total." + repeat(" ", 69 - moneylength - (int)(PDI.getPluralMoney().length()*1.4)));
		options = options.concat(ChatColor.GOLD + "" + cut(PDI.getMoneyOnHand()).toString() + ChatColor.YELLOW + " " + PDI.getPluralMoney() + " on hand." + repeat(" ", 67 - onhandlength - (int)(PDI.getPluralMoney().length()*1.4)));
		options = options.concat(ChatColor.GOLD + "" + cut(PDI.getMoneyInBank()).toString() + ChatColor.YELLOW + " " + PDI.getPluralMoney() + " in the bank." + repeat(" ", 63 - inbanklength - (int)(PDI.getPluralMoney().length()*1.4)));
		if (PDI.getIsHouseOwner() || PDI.getIsGoodBusinessOwner() || PDI.getIsServiceBusinessOwner()) {
			options = options.concat(ChatColor.RED + "");
			options = options.concat(ChatColor.BOLD + "Yearly Taxes:" + ChatColor.RESET + repeat(" ", 59) + ChatColor.RED);
			options = options.concat("Total: " + ChatColor.GOLD + cut(PMI.taxAmount()).toString() + ChatColor.RED + " " + PDI.getPluralMoney() + " per year." + repeat(" ", 50 - totaltaxlength - (int)(PDI.getPluralMoney().length()*1.4)));
		}
		if (PDI.getIsHouseOwner()) {
			options = options.concat("Residential: " + ChatColor.GOLD + cut(PMI.houseTax()) + ChatColor.RED + " " + PDI.getPluralMoney() + " per year." + repeat(" ", 45 - housetaxlength - (int)(PDI.getPluralMoney().length()*1.4)));
		}
		if (PDI.getIsGoodBusinessOwner()) {
			options = options.concat("Commercial Goods: " + ChatColor.GOLD + cut(PMI.goodBusinessTax()) + ChatColor.RED + " " + PDI.getPluralMoney() + " per year." + repeat(" ", 40 - goodtaxlength - (int)(PDI.getPluralMoney().length()*1.4)));
		}
		if (PDI.getIsServiceBusinessOwner()) {
			options = options.concat("Commercial Services: "  + ChatColor.GOLD + cut(PMI.serviceBusinessTax()) + ChatColor.RED + " " + PDI.getPluralMoney() + " per year." + repeat(" ", 36 - servicetaxlength - (int)(PDI.getPluralMoney().length()*1.4)));
		}
		if (PDI.getLoanAmount().compareTo(new BigDecimal(0.0)) != 0) {
			options = options.concat(ChatColor.LIGHT_PURPLE + "");
			options = options.concat(ChatColor.BOLD + "Loans due:" + ChatColor.RESET + repeat(" ", 63));
			options = options.concat(ChatColor.GOLD + "" + PDI.getLoanAmount() + " / " + PDI.getMaxLoan() + " " + ChatColor.LIGHT_PURPLE + PDI.getPluralMoney() + repeat(" ", 57 - loanlength - maxLoanLength));
		}
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		if ((!PDI.getIsInFederalBank() || PDI.getIsInLocalBank())/* && PDI.getCountryIn().getName().equals(PDI.getCountryResides().getName())*/) {
			options = options.concat("Deposit <amount>" + repeat(" ", 58));
			options = options.concat("Withdraw <amount>" + repeat(" ", 56));
			options = options.concat("Take Out Loan <amount>" + ChatColor.GRAY + " Max: " + PDI.getMaxLoan() + ChatColor.GREEN + repeat(" ", 40 - maxLoanLength));
			if (PDI.getLoanAmount().compareTo(new BigDecimal(0)) != 0) {
				options = options.concat("Repay Loan <amount>" + repeat(" ", 53));
				inputs.put("repay", 1);
			}
			inputs.put("deposit", 1);
			inputs.put("withdraw", 1);
			inputs.put("take", 1);
		}
		options = options.concat("Pay <player> <amount>" + repeat(" ", 50));
		inputs.put("pay", 1);
		inputs.put("back", 1);
		
		return space + main + options + end + errormsg;
	}
		
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (args.length != 1 && args.length != 2 && args.length != 3 && args.length != 4) return new ManageMoney(plugin, player, 2);
		if (!inputs.containsKey(args[0].toLowerCase())) return new ManageMoney(plugin, player, 1);

		// Deposit
		if (args[0].equalsIgnoreCase("Deposit") && args.length == 2) {
			try {
				BigDecimal amount = new BigDecimal(args[1]).abs();
				amount = cut(amount);
				if (amount.compareTo(PDI.getMoneyOnHand()) == 1) {
					return new ManageMoney(plugin, player, 3);
				}
				else {
					PDI.transferMoneyToBank(amount);
					return new ManageMoney(plugin, player, 0);
				}
			}
			catch (Exception ex) {
				return new ManageMoney(plugin, player, 4);
			}
		}
		
		// Withdraw
		if (args[0].equalsIgnoreCase("Withdraw") && args.length == 2) {
			try {
				BigDecimal amount = new BigDecimal(args[1]).abs();
				amount = cut(amount);
				if (amount.compareTo(PDI.getMoneyInBank()) == 1) {
					return new ManageMoney(plugin, player, 5);
				}
				else {
					PDI.transferMoneyToBank(amount.negate());
					return new ManageMoney(plugin, player, 0);
				}
			}
			catch (Exception ex) {
				return new ManageMoney(plugin, player, 4);
			}
		}
		
		// Pay
		if (args[0].equalsIgnoreCase("Pay") && args.length == 3) {
			
			if (find(args[1]).size() == 1) args[1] = find(args[1]).get(0);
			else if (find(args[1]).size() == 0) return new ManageMoney(plugin, player, 6);
			else return new ManageMoney(plugin, player, 9, find(args[1]));

			PlayerData targetPDI = plugin.playerdata.get(args[1].toLowerCase());
			try {
				BigDecimal amount =new BigDecimal(args[2]).abs();
				amount = cut(amount);
				if (amount.compareTo(PDI.getMoneyOnHand()) == 1) return new ManageMoney(plugin, player, 3);
				PDI.transferMoney(amount, args[1]);
				if (plugin.getServer().getOfflinePlayer(args[1]).isOnline()) {
					plugin.getServer().getPlayer(args[1]).sendRawMessage(player.getName() + " has paid you " + cut(amount.divide(PDI.getMoneyMultiplyer()).multiply(targetPDI.getMoneyMultiplyer())) + " " + targetPDI.getPluralMoney());
				}
				return new ManageMoney(plugin, player, 0);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return new ManageMoney(plugin, player, 4);
			}	
			
		}
		
		// Take Out Loan
		if (args[0].equalsIgnoreCase("Take") && args.length == 4) {
			try {
				BigDecimal amount = new BigDecimal(args[3]).abs();
				amount = cut(amount);
				if (amount.compareTo((PDI.getMaxLoan().subtract(PDI.getLoanAmount()))) == 1) {
					return new ManageMoney(plugin, player, 7);
				}
				else {
					PDI.addLoan(amount);
					PDI.addMoney(amount);
					return new ManageMoney(plugin, player, 0);
				}
			}
			catch (Exception ex) {
				return new ManageMoney(plugin, player, 4);
			}
		}
		
		// Repay Loan
		if (args[0].equalsIgnoreCase("Repay") && args.length == 3) {
			try {
				BigDecimal amount = new BigDecimal(args[2]).abs();
				amount = cut(amount);
				if (amount.compareTo(PDI.getLoanAmount()) > 0) {
					return new ManageMoney(plugin, player, 8);
				}
				if (amount.compareTo(PDI.getMoneyOnHand()) > 0) {
					return new ManageMoney(plugin, player, 3);
				}
				else {
					PDI.removeLoan(amount);
					PDI.removeMoney(amount);
					return new ManageMoney(plugin, player, 0);
				}
			}
			catch (Exception ex) {
				return new ManageMoney(plugin, player, 4);
			}
		}
		// Back
		if (args[0].equalsIgnoreCase("back")) return new HudConversationMain(plugin, player, 0);
		return new ManageMoney(plugin, player, 10);
	}
}
