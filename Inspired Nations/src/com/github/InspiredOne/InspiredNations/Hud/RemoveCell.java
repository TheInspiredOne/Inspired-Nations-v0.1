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
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class RemoveCell extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error = 0;
	Vector<String> cellnames;
	
	// Constructor
	public RemoveCell(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
	}
	
	public RemoveCell(InspiredNations instance, Player playertemp, int errortemp, Vector<String> celltemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		town = PDI.getTownMayored();
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		cellnames = celltemp;
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
	
	// A method to find a cell given an incomplete string;
	public Vector<String> find(String name) {
		Vector<String> list = new Vector<String>();
		Set<String> cells = town.getPrison().getCells().keySet();
		for (Iterator<String> i = cells.iterator(); i.hasNext();) {
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
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Remove Cell: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		if (error == 2) {
			errormsg = errormsg.concat(ChatColor.RED + "Add more letters. It could be: " + format(cellnames) + ".");
		}
		
		int cellssize = (int) ((town.getPrison().getCells().size() + "").length() * 1.4);
		
		options = options.concat("Cells: " + ChatColor.GOLD + town.getPrison().getCells().size() + ChatColor.YELLOW + repeat(" ", 70 - cellssize));
		options = options.concat("Type the name of the cell from the list below that you would like to remove." + repeat(" ", 20));
		options = options.concat(ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN);
		
		for (Iterator<String> i = town.getPrison().getCells().keySet().iterator(); i.hasNext();) {
			String name = i.next();
			options = options.concat(name + ", ");
		}
		options = options.substring(0, options.length() - 2) + " ";
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}

		// back
		if (arg.equalsIgnoreCase("back")) {
			return new ManageLocalPrison(plugin, player, 0);
		}
		
		// Cell to remove
		
		if (find(arg).size() == 1) {
			arg = find(arg).get(0);
			town.getPrison().removeCell(arg);
			if (town.getPrison().getCells().size() > 0) {
				return new RemoveCell(plugin, player, 0);
			}
			else return new ManageLocalPrison(plugin, player, 0);
		}
		else if (find(arg).size() == 0) return new RemoveCell(plugin, player, 1);
		else return new RemoveCell(plugin, player, 2, find(arg));
	}

}
