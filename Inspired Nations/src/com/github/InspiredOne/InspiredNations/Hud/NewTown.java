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


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class NewTown extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	String playername;
	PlayerMethods PMI;
	boolean permission = true;
	int error;
	
	// Constructor
	public NewTown(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		playername = player.getName().toLowerCase();
		PM = plugin.playermodes.get(playername);
		PMI = new PlayerMethods(plugin, player);
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
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "New Town Logistics: Read the instructions." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "You must first transfer leadership of your town. Go to manage town to do this.");
		}
		if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "That town name is already taken.");
		}
		if (PDI.getIsTownMayor()) {
			options = options.concat("You must first transfer leadership of your town." + repeat(" ", 10));
			permission = false;
			return space + main + options + end;
		}
		options = options.concat("Type the name of your new town." + repeat(" ", 26));
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (arg.equalsIgnoreCase("back")) return new HudConversationMain(plugin, player, 0);
		if (!permission) return new NewTown(plugin, player, 2);
		if (PDI.getCountryResides().getTowns().contains(arg.toLowerCase().trim())) return new NewTown(plugin, player, 3);
		else {
			Town town = new Town(plugin, arg.trim(), player.getName(),PDI.getCountryResides().getName());
			town.setPluralMoney(PDI.getCountryResides().getPluralMoney());
			town.setSingularMoney(PDI.getCountryResides().getSingularMoney());
			town.setMoneyMultiplyer(PDI.getCountryResides().getMoneyMultiplyer());
			town.setNationTax(PDI.getCountryResides().getTaxRate());
			if(PDI.getCountryResides().getTowns().size() == 0) {
				town.setIsCapital(true);
			}
			PDI.getCountryResides().addTown(town);
			PMI.transferTown(town);
			PDI.setTownMayored(town);
			PDI.setIsTownMayor(true);
			return new ManageTown(plugin, player, 0);
		}
	}



}
