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
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class TownGovernmentRegions extends StringPrompt {
	
	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	Town town;
	Vector<String> input = new Vector<String>();
	PlayerModes PM;
	int error;
	Park parkin;
	
	// Constructor
	public TownGovernmentRegions(InspiredNations instance, Player playertemp, int errortemp) {
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
		String main = ChatColor.BOLD + "Government Regions: Type what you would like to do." + ChatColor.RESET + repeat(" ", 1)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		input.add("back");
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		
		if (!town.hasTownHall()) {
			options = options.concat(ChatColor.GREEN + "Claim Town Hall" + ChatColor.GRAY + " Allows people to join your town." + ChatColor.GREEN + repeat(" ", 15));
			input.add("claim town hall");
		}
		
		else {
			options = options.concat(ChatColor.GREEN + "Manage Town Hall" + repeat(" ", 55));
			options = options.concat("Reclaim Town Hall" + ChatColor.GRAY + " Allows people to join your town." + ChatColor.GREEN + repeat(" ", 13));
			input.add("reclaim town hall");
			input.add("manage town hall");
		}
		
		if (!town.hasBank()) {
			options = options.concat("Claim Bank" + ChatColor.GRAY + " Allows people to save and borrow money." + ChatColor.GREEN + repeat(" ", 15));
			input.add("claim bank");
		}
		
		else {
			options = options.concat("Manage Bank" + repeat(" ", 65));
			options = options.concat("Reclaim Bank"  + ChatColor.GRAY + " Allows people to save and borrow money." + ChatColor.GREEN + repeat(" ", 12));
			input.add("reclaim bank");
			input.add("manage bank");
		}
		
		if (!town.hasHospital()) {
			options = options.concat("Claim Hospital" + ChatColor.GRAY + " Allows people to safely heal." + ChatColor.GREEN + repeat(" ", 23));
			input.add("claim hospital");
		}
		
		else {
			options = options.concat("Manage Hospital" + repeat(" ", 55));
			options = options.concat("Reclaim Hospital" + ChatColor.GRAY + " Allows people to safely heal." + ChatColor.GREEN + repeat(" ", 21));
			input.add("reclaim hospital");
			input.add("manage hospital");
		}
		
		if (!town.hasPrison()) {
			options = options.concat("Claim Prison" + ChatColor.GRAY +  " Allows mayors to jail town residents." + ChatColor.GREEN + repeat(" ", 19));
			input.add("claim prison");
		}
		
		else {
			options = options.concat("Manage Prison" + repeat(" ", 55));

			options = options.concat("Reclaim Prison" + ChatColor.GRAY +  " Allows mayors to jail town residents." + ChatColor.GREEN + repeat(" ", 16));
			input.add("reclaim prison");
			input.add("manage prison");
		}
		
		if (town.getParks().size() != 0 && PDI.getIsInLocalPark()) {
			options = options.concat("Manage Park" + repeat(" ", 60));
			options = options.concat("Remove Park" + ChatColor.GRAY + " Unclaims the park you are in." + ChatColor.GREEN + repeat(" ", 23));
			parkin = PDI.getLocalParkIn();
			input.add("remove park");
			input.add("manage park");
		}
		else if (town.getParks().size() != 0) {
			options = options.concat("Manage Park" + repeat(" ", 60));
			input.add("manage park");
			options = options.concat(ChatColor.DARK_GRAY + "Remove Park" + ChatColor.GRAY + " Stand in the park you want to unclaim." + ChatColor.GREEN + repeat(" ", 13));
			
		}
		
		options = options.concat("Claim Park" + ChatColor.GRAY + " Protected multi-purpose land for a price." + ChatColor.GREEN + repeat(" ", 1));
		input.add("claim park");
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new TownGovernmentRegions(plugin, player, 1);
		
		// Claim Town Hall / Re-Claim Town Hall
		if (arg.equalsIgnoreCase("claim town hall") || arg.equalsIgnoreCase("reclaim town hall")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.localHall(true);
			return new ClaimLocalHall1(plugin, player, 0);
		}
		

		
		// Manage Town Hall
		if (arg.equalsIgnoreCase("manage town hall")) {
			return new ManageTownHall(plugin, player, 0);
		}
		
		// Claim Bank / Reclaim Bank
		if (arg.equalsIgnoreCase("claim bank") || arg.equalsIgnoreCase("reclaim bank")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.localBank(true);
			return new ClaimLocalBank1(plugin, player, 0);
		}
		
		
		// Manage bank
		if (arg.equalsIgnoreCase("manage bank")) {
			return new ManageBank(plugin, player, 0);
		}
		
		// Claim / Reclaim Park
		if (arg.equalsIgnoreCase("claim park") || arg.equalsIgnoreCase("reclaim park")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.park(true);
			return new ClaimPark1(plugin, player, 0);
		}
		
		// Remove Park
		if (arg.equalsIgnoreCase("remove park")) {
			town.removePark(parkin);
			for(String name: town.getCoMayors()) {
				if(plugin.getServer().getPlayerExact(name).isConversing() && !name.equalsIgnoreCase(player.getName())) {
					plugin.playerdata.get(name).getConversation().abandon();
				}
			}
			if (plugin.getServer().getPlayerExact(town.getMayor()).isConversing() && !town.getMayor().equalsIgnoreCase(player.getName())) {
				plugin.playerdata.get(town.getMayor()).getConversation().abandon();
			}
			for(Player playertarget:plugin.getServer().getOnlinePlayers()) {
				PlayerMethods PM = new PlayerMethods(plugin, playertarget);
				PM.resetLocationBooleans();
			}
			return new TownGovernmentRegions(plugin, player, 0);
		}
		
		// Manage Park
		if (arg.equalsIgnoreCase("manage park")) {
			if (town.getParks().size() == 1) {
				return new ManagePark2(plugin, player, 0, town.getParks().get(0).getName());
			}
			else return new ManagePark1(plugin, player, 0);
		}
		
		// Claim Hospital / Reclaim Hospital
		if (arg.equalsIgnoreCase("claim hospital") || arg.equalsIgnoreCase("reclaim hospital")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.hospital(true);
			return new ClaimHospital1(plugin, player, 0);
		}
		
		
		// Manage Hospital
		if (arg.equalsIgnoreCase("manage hospital")) {
			return new ManageHospital(plugin, player, 0);
		}
		
		// Manage Prison
		if (arg.equalsIgnoreCase("manage prison")) {
			return new ManageLocalPrison(plugin, player, 0);
		}
		
		// Claim Prison / Reclaim Prison
		if (arg.equalsIgnoreCase("claim prison") || arg.equalsIgnoreCase("reclaim prison")) {
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			PM.localPrison(true);
			return new ClaimLocalPrison1(plugin, player, 0);
		}
		
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			return new ManageTown(plugin, player, 0);
		}
		
		return new TownGovernmentRegions(plugin, player, 0);
	}



}
