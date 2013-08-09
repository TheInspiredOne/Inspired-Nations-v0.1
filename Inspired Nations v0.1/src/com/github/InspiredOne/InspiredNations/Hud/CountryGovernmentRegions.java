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

import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class CountryGovernmentRegions extends StringPrompt {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Country country;
	Vector<String> input = new Vector<String>();
	int error;
	Town townin;
	Park parkin;
	PlayerModes PM;
	
	// Constructor
	public CountryGovernmentRegions(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		country = PDI.getCountryRuled();
		error = errortemp;
		if (PDI.getIsInTown()) {
			townin = PDI.getTownIn();
		}
		PM = plugin.playermodes.get(player.getName().toLowerCase());
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
		String main = ChatColor.BOLD + "Government Regions: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		int townlength = 0;
		if (PDI.getIsInTown()){
			townlength = (int) (townin.getName().length() * 1.1);
		}
		input.add("back");
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		if (PDI.getIsInCountry()) {
			if (PDI.getCountryIn().equals(PDI.getCountryRuled())) {
				if (PDI.getIsInTown()) {
					if (!PDI.getTownIn().isCapital()) {
						options = options.concat("Make Capital" + ChatColor.GRAY + " Makes " + townin.getName() + " the capital." + repeat(" ", 37 - townlength)
								+ ChatColor.GREEN);
						input.add("make capital");
					}
					else {
						options = options.concat(ChatColor.DARK_GRAY + "Make Capital" + ChatColor.GRAY + " Stand inside a town that is not already the capital." + ChatColor.GREEN + repeat(" ", 1));
					}
				}
				else {
					options = options.concat(ChatColor.DARK_GRAY + "Make Capital" + ChatColor.GRAY + " Stand inside a town that is not already the capital." + ChatColor.GREEN + repeat(" ", 1));
				}
			}
			else {
				options = options.concat(ChatColor.DARK_GRAY + "Make Capital" + ChatColor.GRAY + " Stand inside a town that is not already the capital." + ChatColor.GREEN + repeat(" ", 1));
			}
		}
		else {
			options = options.concat(ChatColor.DARK_GRAY + "Make Capital" + ChatColor.GRAY + " Stand inside a town that is not already the capital." + ChatColor.GREEN + repeat(" ", 1));
		}
		
		options = options.concat("Claim Park" + ChatColor.GRAY + " Protected multi-purpose land for a price." + ChatColor.GREEN + repeat(" ", 13));
		input.add("claim park");
		
		if (PDI.getCountryRuled().getParks().size() != 0) {
			options = options.concat("Manage Park" + repeat(" ", 58));
			input.add("manage park");
			if (PDI.getIsInFederalPark()){
				options = options.concat("Unclaim Park" + ChatColor.GRAY + " Unclaims the park you are in." + ChatColor.GREEN + repeat(" ", 23));
				parkin = PDI.getFederalParkIn();
				input.add("unclaim park");
			}
			else {
				options = options.concat(ChatColor.DARK_GRAY + "Unclaim Park" + ChatColor.GRAY + " Stand in the park you would like to unclaim." + ChatColor.GREEN + repeat(" ", 10));
			}
		}

		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new CountryGovernmentRegions(plugin, player, 1);
		
		// Make Capital
		if (arg.equalsIgnoreCase("make capital")) {
			
			if (PDI.getCountryRuled().getName().equalsIgnoreCase(townin.getCountry())) {
				if (country.hasCapital()) {
					country.getCapital().setIsCapital(false);
				}
				townin.setIsCapital(true);

				return new CountryGovernmentRegions(plugin, player, 0);
			}
		}
		
		// Manage Park
		if (arg.equalsIgnoreCase("manage park")) {
			if (country.getParks().size() == 1) {
				return new ManageFederalPark2(plugin, player, 0, country.getParks().get(0).getName());
			}
			else return new ManageFederalPark1(plugin, player, 0);
		}
		
		// Claim Park
		if (arg.equalsIgnoreCase("claim park")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.federalPark(true);
			return new ClaimFederalPark1(plugin, player, 0);
		}
		
		// Unclaim Park
		if (arg.equalsIgnoreCase("unclaim park")) {
			country.removePark(parkin);
			for(Player playertarget:plugin.getServer().getOnlinePlayers()) {
				PlayerMethods PM = new PlayerMethods(plugin, playertarget);
				PM.resetLocationBooleans();
			}
			for(String name: country.getCoRulers()) {
				if(plugin.getServer().getPlayerExact(name).isConversing() && !name.equalsIgnoreCase(player.getName())) {
					plugin.playerdata.get(name).getConversation().abandon();
				}
			}
			if (plugin.getServer().getPlayerExact(country.getRuler()).isConversing() && !country.getRuler().equalsIgnoreCase(player.getName())) {
				plugin.playerdata.get(country.getRuler()).getConversation().abandon();
			}
			PDI.setFederalParkIn(null);
			PDI.setInFederalPark(false);
			return new CountryGovernmentRegions(plugin, player, 0);
		}
		// back
		if (arg.equalsIgnoreCase("back")) {
			return new ManageCountry(plugin, player, 0);
		}
		
		return new CountryGovernmentRegions(plugin, player, 0);
	}

}
