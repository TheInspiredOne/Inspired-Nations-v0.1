/*******************************************************************************
 * Copyright (c) 2013 InspiredOne.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     InspiredOne - initial API and implementation
 ******************************************************************************/
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
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Country.CountryMethods;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ManageCountryEconomy extends StringPrompt {
	
	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	PlayerMethods PMI;
	Vector<String> inputs = new Vector<String>();
	int error;
	Vector<String> suggestions = new Vector<String>();
	CountryMethods countryMethods;
	
	// Constructor
	public ManageCountryEconomy(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
		countryMethods = new CountryMethods(plugin, country);
	}
	
	public ManageCountryEconomy(InspiredNations instance, Player playertemp, int errortemp, Vector<String> suggestionstemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		PMI = new PlayerMethods(plugin, player);
		error = errortemp;
		suggestions = suggestionstemp;
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
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public double cut(double x) {
		int y;
		y = (int) (x*100);
		return y/100.0;
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
	
	// A method to find a town given an incomplete string;
	public Vector<String> findTown(String name) {
		
		Vector<Town> towns = country.getTowns();
		
		Vector<String> list = new Vector<String>();
		for (Town town : towns) {
			String nametest = town.getName();
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
	
	// A method to find a country given an incomplete string;
	public Vector<String> findCountry(String name) {
		
		Set<String> countries = plugin.countrydata.keySet();
		
		Vector<String> list = new Vector<String>();
		for (String countryname: countries) {
			String nametest = plugin.countrydata.get(countryname).getName();
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
		int moneylength = (int) (("" + country.getMoney()).length()* 1.4);
		int loanlength = (int) (("" + country.getLoanAmount()).length() * 1.4);
		int taxLength = (int) (("" + country.getTaxRate()).length() * 1.4);

		
		BigDecimal taxRevenuetemp = new BigDecimal(0);
		for (int i = 0; i < country.getTowns().size(); i++) {
			TownMethods TMI = new TownMethods(plugin, country.getTowns().get(i));
			taxRevenuetemp = taxRevenuetemp.add(TMI.getTaxAmount());
		}
		BigDecimal taxRevenue = cut(taxRevenuetemp);
		
		int revenueLength = (int) (("" + taxRevenue).length() * 1.4);
		
		BigDecimal expenditures;
		expenditures = cut(countryMethods.getTaxAmount());
		int expendituresLength = (int) (("" + expenditures).length() * 1.4);
		int differenceLength = (int) (("" + (taxRevenue.subtract(expenditures))).length() * 1.4);
		int maxLoanLength = (int) (country.getMaxLoan().toString().length() * 1.4);
		String options = "";
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Manage Economy: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = "";
		
		// Building options
		options = options.concat(ChatColor.BOLD + "The Country Has:" + ChatColor.RESET +  repeat(" ", 53)) + ChatColor.YELLOW;
		options = options.concat(ChatColor.GOLD + "" + country.getMoney() + ChatColor.YELLOW + " " + country.getPluralMoney() + " in total." + repeat(" ", 65 - moneylength - country.getPluralMoney().length()));
		options = options.concat(ChatColor.RED + "");
		options = options.concat(ChatColor.BOLD + "Yearly Taxes:" + ChatColor.RESET + repeat(" ", 59) + ChatColor.RED);
		options = options.concat("Tax Rate: " + ChatColor.GOLD + country.getTaxRate() + "%" + ChatColor.RED + repeat(" ", 58 - taxLength));
		options = options.concat("Expenditures: " + ChatColor.DARK_RED + expenditures + ChatColor.RED + " " + country.getPluralMoney() + " per year." + repeat(" ", 45 - expendituresLength - (int) (country.getPluralMoney().length()*1.4)));
		options = options.concat("Revenue: " + ChatColor.DARK_GREEN + taxRevenue + ChatColor.RED + " " + country.getPluralMoney() + " per year." + repeat(" ", 43 - revenueLength - (int) (country.getPluralMoney().length()*1.4)));
		options = options.concat("Difference: " + ChatColor.GOLD + (taxRevenue.subtract(expenditures)) + ChatColor.RED + " " + country.getPluralMoney() + " per year." + repeat(" ", 46 - differenceLength - (int) (country.getPluralMoney().length()*1.4)));
		
		if (country.getLoanAmount().compareTo(new BigDecimal(0)) != 0) {
			options = options.concat(ChatColor.LIGHT_PURPLE + "");
			options = options.concat(ChatColor.BOLD + "Loans due:" + ChatColor.RESET + repeat(" ", 63));
			options = options.concat(ChatColor.GOLD + "" + country.getLoanAmount() + " / " + cut(country.getMaxLoan()) + " " + ChatColor.LIGHT_PURPLE + country.getPluralMoney() + repeat(" ", 60 - loanlength - maxLoanLength));
		}
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		if (!PDI.getIsInFederalBank()/* && PDI.getCountryIn().getName().equals(PDI.getCountryResides().getName())*/) {
			options = options.concat("Take Out Loan <amount>" + ChatColor.GRAY + " Max: " + cut(country.getMaxLoan()) + ChatColor.GREEN + repeat(" ", 40 - maxLoanLength));
			if (country.getLoanAmount().compareTo(new BigDecimal(0)) != 0.0) {
				options = options.concat("Repay Loan <amount>" + repeat(" ", 53));
				inputs.add("repay");
			}
			inputs.add("take");
		}
		options = options.concat("Change Tax Rate <Rate>" + repeat(" ", 46));
		if (country.getLoanAmount().compareTo(new BigDecimal(0)) == 0) {
			options = options.concat("Pay <player> <amount>" + repeat(" ", 50));
			options = options.concat("Give <country> <amount>" + repeat(" ", 48));
			options = options.concat("Fund <town> <amount>" + repeat(" ", 50));
			inputs.add("pay");
			inputs.add("give");
			inputs.add("fund");
		}
		else if (country.getLoanAmount().compareTo(new BigDecimal(0)) != 0) {
			options = options.concat(ChatColor.DARK_GRAY + "Pay <player> <amount>" + ChatColor.GRAY + " Can't do until you pay back loans." + repeat(" ", 7));
			options = options.concat(ChatColor.DARK_GRAY + "Give <country> <amount>" + ChatColor.GRAY + " Can't do until you pay back loans." +  repeat(" ", 5));
			options = options.concat(ChatColor.DARK_GRAY + "Fund <town> <amount>" + ChatColor.GRAY + " Can't do until you pay back loans." + repeat(" ", 7) + ChatColor.GREEN);
		}
		options = options.concat("Rename Money <singular> <plural>" + repeat(" ", 34));
		inputs.add("change");
		inputs.add("rename");
		inputs.add("back");
		
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Only numbers are allowed as the argument. Do not use letters.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That player does not exist on this server.");
		}
		if (error == 4) {
			errormsg = errormsg.concat(ChatColor.RED + "The country doesn't have that much money.");
		}
		if (error == 5) {
			errormsg = errormsg.concat(ChatColor.RED + "That country does not exist on this server.");
		}
		if (error == 6) {
			errormsg = errormsg.concat(ChatColor.RED + "That money name is already taken.");
		}
		if (error == 7) {
			errormsg = errormsg.concat(ChatColor.RED + "That town does not exist in this country.");
		}
		if (error == 8) {
			errormsg = errormsg.concat(ChatColor.RED + "That exceeds the maximum loan.");
		}
		if (error == 9) {
			errormsg = errormsg.concat(ChatColor.RED + "A country cannot make payments when it has loans out.");
		}
		if (error == 10) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(suggestions) + ".");
		}
		if (error == 11) {
			errormsg = errormsg.concat(ChatColor.RED + "The country doesn't have that much loan to pay back.");
		}
		return space + main + options + end + errormsg;
	}

		@Override
		public Prompt acceptInput(ConversationContext arg0, String arg) {
			if (arg.startsWith("/")) {
				arg = arg.substring(1);
			}
			String[] args = arg.split(" ");
			if (args.length != 1 && args.length != 2 && args.length != 3 && args.length != 4 && args.length !=5) return new ManageCountryEconomy(plugin, player, 1);
			if (!inputs.contains(args[0].toLowerCase())) return new ManageCountryEconomy(plugin, player, 1);

			// Take Out Loan
			if (args[0].equalsIgnoreCase("Take") && args.length == 4) {
				try {
					BigDecimal amount = cut(new BigDecimal(args[3]).abs());
					if (amount.compareTo(country.getMaxLoan().subtract(country.getLoanAmount())) > 0) {
						return new ManageCountryEconomy(plugin, player, 8);
					}
					country.addLoan(amount);
					country.addMoney(amount);
					return new ManageCountryEconomy(plugin, player, 0);
				}
				catch(Exception ex) {
					return new ManageCountryEconomy(plugin, player, 2);
				}
			}

			// Repay Loan
			if (args[0].equalsIgnoreCase("Repay") && args.length == 3) {
				try {
					BigDecimal amount = cut(new BigDecimal(args[2]).abs());
					if (amount.compareTo(country.getLoanAmount()) <= 0) {
						if (amount.compareTo(country.getMoney()) <= 0) {
							country.removeLoan(amount);
							country.removeMoney(amount);
							return new ManageCountryEconomy(plugin, player, 0);
						}
						else return new ManageCountryEconomy(plugin, player, 4);
					}
					else return new ManageCountryEconomy(plugin, player, 11);
				}
				catch (Exception ex) {
					return new ManageCountryEconomy(plugin, player, 2);
				}
			}
			
			// Pay Player
			if (args[0].equalsIgnoreCase("Pay") && args.length == 3) {
				Vector<String> findings = find(args[1]);
				if (findings.size() == 1) {
					args[1] = findings.get(0);
					if(country.getLoanAmount().compareTo(new BigDecimal(0)) != 0) return new ManageCountryEconomy(plugin, player, 9);
					else {
						PlayerData targetPDI = plugin.playerdata.get(args[1].toLowerCase());
						try {
							BigDecimal amount = new BigDecimal(args[2]).abs();
							amount = cut(amount);
							if (amount.compareTo(country.getMoney()) == 1) return new ManageCountryEconomy(plugin, player, 4);
							country.removeMoney(amount);
							targetPDI.addMoney(amount.divide(country.getMoneyMultiplyer()).multiply(targetPDI.getMoneyMultiplyer()));
							if (plugin.getServer().getPlayer(args[1]).isOnline()) {
								plugin.getServer().getPlayer(args[1]).sendMessage(country.getName() + " has paid you " + cut(amount.divide(PDI.getMoneyMultiplyer()).multiply(targetPDI.getMoneyMultiplyer())) + " " + targetPDI.getPluralMoney());
							}
						}
						catch (Exception ex) {
							return new ManageCountryEconomy(plugin, player, 2);
						}	
					}
				}
				else if (findings.size() == 0) return new ManageCountryEconomy(plugin, player, 5);
				else return new ManageCountryEconomy(plugin, player, 10, findings);
				
				if (!plugin.playerdata.containsKey(args[1])) return new ManageCountryEconomy(plugin, player, 3);

			}
			
			// Give Country
			if (args[0].equalsIgnoreCase("Give") && args.length == 3) {
				Vector<String> findings = findCountry(args[1]);
				if (findings.size() == 1) {
					args[1] = findings.get(0);
					if (country.getLoanAmount().compareTo(new BigDecimal(0)) != 0) return new ManageCountryEconomy(plugin, player, 9);
					else {
						try {
							BigDecimal amount = cut(new BigDecimal(args[3]).abs());
							if (amount.compareTo(country.getMoney()) == -1) {
								country.removeMoney(amount);
								plugin.countrydata.get(args[1].toLowerCase().trim()).addMoney(amount);
								return new ManageCountryEconomy(plugin, player, 0);
							}
							else return new ManageCountryEconomy(plugin, player, 4);
						}
						catch (Exception ex) {
							return new ManageCountryEconomy(plugin, player, 2);
						}
					}
				}
				else if (findings.size() == 0) return new ManageCountryEconomy(plugin, player, 5);
				else return new ManageCountryEconomy(plugin, player, 10, findings);
			}
			
			// Change Tax Rate
			if (args[0].equalsIgnoreCase("Change") && args.length == 4) {
				try {
					double rate = cut(java.lang.Double.parseDouble(args[3]));
					country.setTaxRate(rate);
					for (Iterator<Town> i = country.getTowns().iterator(); i.hasNext();) {
						i.next().setNationTax(rate);
					}
					return new ManageCountryEconomy(plugin, player, 0);
				}
				catch(Exception ex) {
					return new ManageCountryEconomy(plugin, player, 2);
				}
			}
			
			// Rename Money
			if (args[0].equalsIgnoreCase("Rename") && args.length == 4) {
				boolean same = false;
				for (Iterator<String> i = plugin.countrydata.keySet().iterator(); i.hasNext();) {
					Country temp = plugin.countrydata.get(i.next());
						if (temp.getPluralMoney().equalsIgnoreCase(args[3]) || temp.getSingularMoney().equalsIgnoreCase(args[2]) || temp.getPluralMoney().equalsIgnoreCase(args[2]) || temp.getSingularMoney().equalsIgnoreCase(args[3])) {
							same = true;
						}
				}
				if (same) return new ManageCountryEconomy(plugin, player, 6);
				else {
					country.setPluralMoney(args[3]);
					country.setSingularMoney(args[2]);
					for (int i = 0; i < country.getResidents().size(); i++) {
						plugin.playerdata.get(country.getResidents().get(i)).setPluralMoney(args[3]);
						plugin.playerdata.get(country.getResidents().get(i)).setSingularMoney(args[2]);
					}
					for (Town town: country.getTowns()) {
						town.setSingularMoney(args[2]);
						town.setPluralMoney(args[3]);
					}
				}
			}
			
			// Fund Town
			if (args[0].equalsIgnoreCase("Fund") && args.length == 3) {
				Vector<String> findings = findTown(args[1]);
				if (findings.size() == 1) {
					args[1] = findings.get(0);
					try {
						boolean contains = false;
						int index = 0;
						BigDecimal amount = cut(new BigDecimal(args[2]).abs());
						for (int i = 0; i < country.getTowns().size(); i++) {
							if (country.getTowns().get(i).getName().equalsIgnoreCase(args[1])) {
								contains = true;
								index = i;
							}
						}
						if (country.getMoney().compareTo(amount) == 1) {
							return new ManageCountryEconomy(plugin, player, 4);
						} 
						if (contains) {
							country.getTowns().get(index).addMoney(amount);
							country.removeMoney(amount);
						}
						else return new ManageCountryEconomy(plugin, player, 7);
					}
					catch (Exception ex) {
						return new ManageCountryEconomy(plugin, player, 2);
					}
				}
				else if (findings.size() == 0) return new ManageCountryEconomy(plugin, player, 7);
				else return new ManageCountryEconomy(plugin, player, 10, findings);

			}

			// back
			if (args[0].equalsIgnoreCase("back")) {
				return new ManageCountry(plugin, player, 0);
			}
			
			return new ManageCountryEconomy(plugin, player, 0);
		}
		

}
