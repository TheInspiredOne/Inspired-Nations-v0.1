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
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Prison.Local.Cell;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ClaimCell extends StringPrompt {

	
	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	
	// Constructor
	public ClaimCell(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
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
	public double cut(double x) {
		int y;
		y = (int) (x*100);
		return y/100.0;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Claim Cell: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "Cell name too long.");
		}
		else if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Cell name already taken.");
		}
		else if (error == 3) {
			errormsg = errormsg.concat(ChatColor.RED + "You need to stand inside your prison.");
		}
		
		options = options.concat("Stand inside the cell where you would like a prisoner to spawn. Then type the name of the cell." + repeat(" ", 30));		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!town.getPrison().isIn(player.getLocation())) return new ClaimCell(plugin, player, 3);
		if (arg.length() > 25) return new ClaimCell(plugin, player, 1);
		if (town.getPrison().getCells().containsKey(arg.toLowerCase())) return new ClaimCell(plugin, player, 2);
		// back
		if (arg.equalsIgnoreCase("back")) {
			return new ManageLocalPrison(plugin, player, 0);
		}
		
		// name for cell
		else {
			town.getPrison().addCell(arg, new Cell(player.getLocation(), arg));
			return new ManageLocalPrison(plugin, player, 0);
		}
		
	}



}
