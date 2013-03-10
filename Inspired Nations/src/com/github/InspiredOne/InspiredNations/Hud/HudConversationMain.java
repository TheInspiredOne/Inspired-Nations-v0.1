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

import java.util.HashMap;
import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;


public class HudConversationMain extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	
	Vector<String> inputs = new Vector<String>();
	int error;
	
	// Constructor
	public HudConversationMain(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		error = errortemp;
		PM.reSelectHouse = false;
	}
	
	// A method to repeat a string simply
	public String repeat(String entry, int multiple) {
		String temp = "";
		for (int i = 0; i < multiple; i++) {
			temp = temp.concat(entry);
		}
		return temp;
	}
	
	// A generalized find function where you give it the vector you want it to search in.
	public Vector<String> find(String name, Vector<String> test) {
		Vector<String> list = new Vector<String>();
		for (String nametest:test) {
			if (nametest.toLowerCase().contains(name.toLowerCase()) && !nametest.equalsIgnoreCase(player.getName())) {
				list.add(nametest);
			}
			if (nametest.equalsIgnoreCase(name)) {
				list.clear();
				list.add(nametest);
				return list;
			}
		}
		return list;
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String options = "";
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Welcome to the HUD. Type what you would like to do.  " + ChatColor.RESET
				+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.GREEN;
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave." + repeat(" ", 55);
		String errormsg = "";
		options = options.concat("Notifications" + repeat(" ", 58));
		inputs.add("notifications");
		if (PDI.getIsCountryRuler()) {
			options = options.concat("Manage Country" + repeat(" ", 60));
			inputs.add("manage country");
		}
		if (PDI.getIsTownMayor()) {
			options = options.concat("Manage Town" + repeat(" ", 60));
			inputs.add("manage town");
		}
		if (PDI.getIsGoodBusinessOwner() || PDI.getIsServiceBusinessOwner()) {
			options = options.concat("Manage Business" + repeat(" ", 60));
			inputs.add("manage business");
		}
		if (PDI.getIsHouseOwner()) {
			options = options.concat("Manage House" + repeat(" ", 60));
			inputs.add("manage house");
		}
		options = options.concat("Manage Money" + repeat(" ", 60));
		inputs.add("manage money");
		
		options = options.concat("New Country" + repeat(" ", 64));
		inputs.add("new country");
		if (PDI.getIsCountryResident()) {
			options = options.concat("New Town" + repeat(" ", 68));
			inputs.add("new town");
		}
		if (PDI.getIsTownResident()) {
			options = options.concat("New Business" + repeat(" ", 61));
			inputs.add("new business");
		}
		if (PDI.getIsTownResident()) {
			options = options.concat("New House" + repeat(" ", 65));
			inputs.add("new house");
		}
		if (PDI.getIsCountryResident()) {
			options = options.concat("Jobs " + repeat(" ", 60));
			inputs.add("jobs");
		}
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		
		if(find(arg, inputs).size() == 0) return new HudConversationMain(plugin,player,1);
		else if(find(arg,inputs).size() > 1) return new HudConversationMain(plugin,player,1);
		else arg = find(arg,inputs).get(0);
		
		if (!inputs.contains(arg.toLowerCase())) {
			return new HudConversationMain(plugin, player, 1);
		}
		else {
			if (arg.equalsIgnoreCase("Notifications")) {
				return new Notifications(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("Manage Citizenship")) {
				return new ManageCitizenship(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("Manage Country")) {
				return new ManageCountry(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("Manage Town")) {
				return new ManageTown(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("Manage Business")) {
				if ((PDI.getGoodBusinessOwned().size()+PDI.getServiceBusinessOwned().size()) > 1) {
					return new ManageBusiness1(plugin, player, 0);
				}
				else{
					if (PDI.getGoodBusinessOwned().size()==1) {
						return new ManageBusiness2(plugin, player,0, PDI.getGoodBusinessOwned().get(0).getName());
					}
					else return new ManageBusiness2(plugin, player, 0, PDI.getServiceBusinessOwned().get(0).getName());
				}
			}
			if (arg.equalsIgnoreCase("Manage House")) {
				if (PDI.getHouseOwned().size() > 1) {
					return new ManageHouse1(plugin, player, 0);
				}
				else return new ManageHouse2(plugin, player, 0, PDI.getHouseOwned().get(0).getName());
			}
			if (arg.equalsIgnoreCase("Manage Money")) {
				return new ManageMoney(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("New Country")) {
				return new NewCountry(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("New Town")) {
				return new NewTown(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("New Business")) {
				PM.setPolygon(new polygonPrism(player.getWorld().getName()));
				PM.setCuboid(new Cuboid(player.getWorld().getName()));
				return new NewBusiness(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("New House")) {
				PM.setPolygon(new polygonPrism(player.getWorld().getName()));
				PM.setCuboid(new Cuboid(player.getWorld().getName()));
				PM.house(true);
				return new NewHouse(plugin, player, 0);
			}
			if (arg.equalsIgnoreCase("Jobs")) {
				return new Jobs1(plugin, player, 0);
			}
		}
		
		return null;
	}
}
