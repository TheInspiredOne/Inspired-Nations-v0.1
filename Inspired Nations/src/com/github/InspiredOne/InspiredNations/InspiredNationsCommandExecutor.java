package com.github.InspiredOne.InspiredNations;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;

import com.github.InspiredOne.InspiredNations.Hud.ConversationBuilder;

public class InspiredNationsCommandExecutor implements CommandExecutor {
	InspiredNations plugin;
	public InspiredNationsCommandExecutor(InspiredNations instance) {
		plugin = instance;
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLable, String[] args) {
		PlayerData PDI = plugin.playerdata.get(sender.getName().toLowerCase());
		if (CommandLable.equalsIgnoreCase("hud")) {
			// Handles Commands
			ConversationBuilder convo = new ConversationBuilder(plugin);
			Conversation conversation = convo.HudConvo(plugin.getServer().getPlayer(sender.getName()));
			PDI.setConversation(conversation);
			conversation.begin();
		}
		else return false;
		return false;
	}

}
