package com.github.InspiredOne.InspiredNations.Hud;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Country.Country;

public class NewCountry extends StringPrompt {
	
	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	String playername;
	boolean permission = true;
	int error;
	
	// Constructor
	public NewCountry(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		playername = player.getName().toLowerCase();
		PM = plugin.playermodes.get(playername);
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
		String main = ChatColor.BOLD + "New Country Logistics: Read the instructions." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + "Type 'exit' to leave or 'back' to go back to the HUD." + repeat(" ", 13);
		String errormsg = "";
		if (PDI.getIsCountryRuler()) {
			options = options.concat("You must first transfer leadership of your current country." + repeat(" ", 2));
			permission = false;
			return space + main + options + end;
		}
		if (PDI.getIsTownMayor()) {
			options = options.concat("You must first transfer leadership of your town." + repeat(" ", 10));
			permission = false;
			return space + main + options + end;
		}
		options = options.concat("Type the name of your new country." + repeat(" ", 26));
		
		// build the error message
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That country name is already taken. Please try something else.");
		}
			
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (arg.equalsIgnoreCase("back")) return new HudConversationMain(plugin, player, 0);
		if (!permission) return new NewCountry(plugin, player, 0);
		if (plugin.countrydata.containsKey(arg.toLowerCase().trim())) return new NewCountry(plugin, player, 1);
		else {
			Country country = new Country(plugin, arg.trim(), player.getName().toLowerCase());
			plugin.countrydata.put(arg.toLowerCase().trim(), country);
			country.addPopulation(1);
			country.addResident(playername);
			PDI.setCountryRuler(country);
			PDI.setIsCountryRuler(true);
			PDI.setIsCountryResident(true);
			PDI.setCountryResides(country);
			return new NewCountry2(plugin, player, plugin.countrydata.get(arg.toLowerCase().trim()), 0);
		}
	}



}
