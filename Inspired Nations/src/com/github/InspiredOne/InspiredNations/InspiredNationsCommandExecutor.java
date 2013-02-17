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
package com.github.InspiredOne.InspiredNations;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.Hud.ConversationBuilder;

public class InspiredNationsCommandExecutor implements CommandExecutor {
	InspiredNations plugin;
	public InspiredNationsCommandExecutor(InspiredNations instance) {
		plugin = instance;
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLable, String[] args) {
		if(!(sender instanceof Player)) {
			plugin.logger.info("HUD cannot be called from console.");
			return false;
		}
		
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
