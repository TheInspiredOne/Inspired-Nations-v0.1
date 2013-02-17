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
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ChangeTownTaxRates extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	PlayerMethods PMI;
	Vector<String> inputs = new Vector<String>();
	TownMethods TMI;
	int error;
	
	// Constructor
	public ChangeTownTaxRates(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PMI = new PlayerMethods(plugin, player);
		TMI = new TownMethods(plugin, town);
		error = errortemp;
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

	@Override
	public String getPromptText(ConversationContext arg0) {
		
		int moneylength = (int) (("" + town.getMoney()).length()* 1.4);
		int loanlength = (int) (("" + town.getLoan()).length() * 1.4);
		int houseTaxLength = (int) (("" + town.getHouseTax()).length() * 1.4);
		int goodTaxLength = (int) (("" + town.getGoodBusinessTax()).length() * 1.4);
		int serviceTaxLength = (int) (("" + town.getServiceBusinessTax()).length() * 1.4);
		
		BigDecimal taxRevenue = new BigDecimal(0);
		BigDecimal houseRevenue = new BigDecimal(0);
		BigDecimal goodBusinessRevenue = new BigDecimal(0);
		BigDecimal serviceBusinessRevenue = new BigDecimal(0);
		for (Iterator<String> i = town.getResidents().iterator(); i.hasNext(); ) {
			String targetname = i.next();
			PlayerMethods PMITarget = new PlayerMethods(plugin, plugin.getServer().getPlayer(targetname));
			taxRevenue = taxRevenue.add(PMITarget.taxAmount(town.getName().toLowerCase()));
			houseRevenue = houseRevenue.add(PMITarget.houseTax(town.getName().toLowerCase()));
			goodBusinessRevenue = goodBusinessRevenue.add(PMITarget.goodBusinessTax(town.getName().toLowerCase()));
			serviceBusinessRevenue = serviceBusinessRevenue.add(PMITarget.serviceBusinessTax(town.getName().toLowerCase()));
		}
		taxRevenue = cut(taxRevenue);
		houseRevenue = cut(houseRevenue);
		goodBusinessRevenue = cut(goodBusinessRevenue);
		serviceBusinessRevenue = cut(serviceBusinessRevenue);
		
		int revenueLength = (int) (taxRevenue.toString().length() * 1.4);
		int houseLength = (int) (houseRevenue.toString().length() * 1.4);
		int goodBusinessLength = (int) (goodBusinessRevenue.toString().length() * 1.4);
		int serviceBusinessLength = (int) (serviceBusinessRevenue.toString().length() * 1.4);
		BigDecimal expenditures = cut(TMI.getTaxAmount());
		int expendituresLength = (int) (expenditures.toString().length() * 1.4);
		int differenceLength = (int) (taxRevenue.subtract(expenditures).toString().length() * 1.4);
		
		String options = "";
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Change Tax Rates: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5) + ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "You put too many arguments.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "Only numbers are aloud as the argument. Do not use letters.");
		}
		
		// Building options
		options = options.concat(ChatColor.BOLD + "The Town Has:" + ChatColor.RESET +  repeat(" ", 58)) + ChatColor.YELLOW;
		options = options.concat(ChatColor.GOLD + "" + town.getMoney() + ChatColor.YELLOW + " " + town.getPluralMoney() + " in total." + repeat(" ", 65 - moneylength - town.getPluralMoney().length()));
		options = options.concat(ChatColor.RED + "");
		options = options.concat(ChatColor.BOLD + "Yearly Taxes:" + ChatColor.RESET + repeat(" ", 59) + ChatColor.RED);
		options = options.concat("House Tax Rate: " + ChatColor.GOLD + town.getHouseTax() + "%  (" + houseRevenue.toString() + ")" + ChatColor.RED + repeat(" ", 52 - houseLength- houseTaxLength));
		options = options.concat("Goods Business Tax Rate: " + ChatColor.GOLD + town.getGoodBusinessTax() + "%  ("+goodBusinessRevenue.toString()+")" + ChatColor.RED + repeat(" ", 41-goodBusinessLength - goodTaxLength));
		options = options.concat("Service Business Tax Rate: " + ChatColor.GOLD + town.getServiceBusinessTax() + "%  ("+serviceBusinessRevenue+")" + ChatColor.RED + repeat(" ", 36-serviceBusinessLength - serviceTaxLength));
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.RED);
		options = options.concat("Expenditures: " + ChatColor.DARK_RED + expenditures + ChatColor.RED + " " + town.getPluralMoney() + " per year." + repeat(" ", 48 - expendituresLength - (int) (town.getPluralMoney().length()*1.4)));
		options = options.concat("Revenue: " + ChatColor.DARK_GREEN + taxRevenue.toString() + ChatColor.RED + " " + town.getPluralMoney() + " per year." + repeat(" ", 51 - revenueLength - (int) (town.getPluralMoney().length()*1.4)));
		options = options.concat("Difference: " + ChatColor.GOLD + (taxRevenue.subtract(expenditures)).toString() + ChatColor.RED + " " + town.getPluralMoney() + " per year." + repeat(" ", 48 - differenceLength - (int) (town.getPluralMoney().length()*1.4)));
		
		if (town.getLoan().compareTo(new BigDecimal(0)) != 0) {
			options = options.concat(ChatColor.LIGHT_PURPLE.toString() + "");
			options = options.concat(ChatColor.BOLD + "Loans due:" + ChatColor.RESET + repeat(" ", 63));
			options = options.concat(ChatColor.GOLD + "" + town.getLoan() + " " + ChatColor.LIGHT_PURPLE + town.getPluralMoney() + repeat(" ", 60 - loanlength));
		}
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);

		options = options.concat("House Tax <new rate>" + repeat(" ", 48));
		options = options.concat("Goods Tax <new rate>" + repeat(" ", 48));
		options = options.concat("Service Tax <new rate>" + repeat(" ", 40));
		inputs.add("house");
		inputs.add("goods");
		inputs.add("service");
		inputs.add("back");
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		String[] args = arg.split(" ");
		if (args.length != 1 && args.length != 2 && args.length != 3 && args.length != 4 && args.length !=5) return new ChangeTownTaxRates(plugin, player, 2);
		if (!inputs.contains(args[0].toLowerCase())) return new ChangeTownTaxRates(plugin, player, 1);
		
		// House Tax
		if (args[0].equalsIgnoreCase("House")) {
			try {
				double rate = cut(java.lang.Double.parseDouble(args[2]));
					town.setHouseTax(rate);
					for(int i = 0; i < town.getResidents().size(); i++) {
						PlayerData PDITemp = plugin.playerdata.get(town.getResidents().get(i));
						PDITemp.setHouseTax(rate);
					}
					return new ChangeTownTaxRates(plugin, player, 0);
			}
			catch(Exception ex) {
				return new ChangeTownTaxRates(plugin, player, 3);
			}
		}
		
		// Goods Tax
		if (args[0].equalsIgnoreCase("Goods")) {
			try {
				double rate = cut(java.lang.Double.parseDouble(args[2]));
					town.setGoodBusinessTax(rate);
					for(int i = 0; i < town.getResidents().size(); i++) {
						PlayerData PDITemp = plugin.playerdata.get(town.getResidents().get(i));
						PDITemp.setGoodBusinessTax(rate);
					}
					return new ChangeTownTaxRates(plugin, player, 0);
			}
			catch(Exception ex) {
				return new ChangeTownTaxRates(plugin, player, 3);
			}
		}
		
		// Service Tax
		if (args[0].equalsIgnoreCase("Service")) {
			try {
				double rate = cut(java.lang.Double.parseDouble(args[2]));
					town.setServiceBusinessTax(rate);
					for(int i = 0; i < town.getResidents().size(); i++) {
						PlayerData PDITemp = plugin.playerdata.get(town.getResidents().get(i));
						PDITemp.setServiceBusinessTax(rate);
					}
					return new ChangeTownTaxRates(plugin, player, 0);
			}
			catch(Exception ex) {
				return new ChangeTownTaxRates(plugin, player, 3);
			}
		}
		
		// back
		if (args[0].equalsIgnoreCase("back")) {
			return new ManageTownFinances(plugin, player, 0);
		}
		
		return new ChangeTownTaxRates(plugin, player, 1);
	}
}
