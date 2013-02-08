package com.github.InspiredOne.InspiredNations.Hud;


import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;

public class ConversationBuilder {
	
	// Grabbing the instance of the plugin.
	InspiredNations plugin;
	public ConversationBuilder(InspiredNations instance) {
		plugin = instance;
	}
	
	public Conversation HudConvo(Player player) {
		ConversationFactory HudConvo = new ConversationFactory(plugin)
		.withModality(true)
		.withEscapeSequence("exit")
		.withFirstPrompt(new HudConversationMain(plugin, player, 0))
		.withLocalEcho(false);
		//.withTimeout(180);
		return HudConvo.buildConversation((Conversable) player);
	}
	
	public Conversation CountryClaim(Player player) {
		ConversationFactory CountryClaim = new ConversationFactory(plugin)
		.withModality(true) 
		//.withEscapeSequence("exit")
		.withFirstPrompt(new ClaimLand(plugin, player, 0))
		.withLocalEcho(false);
		//.withTimeout(180);
		return CountryClaim.buildConversation((Conversable) player);
	}
	
	public Conversation ManageCountry(Player player) {
		ConversationFactory ManageCountry = new ConversationFactory(plugin)
		.withModality(true)
		.withEscapeSequence("exit")
		.withFirstPrompt(new ManageCountry(plugin, player, 0))
		.withLocalEcho(false);
		//.withTimeout(180);
		return ManageCountry.buildConversation((Conversable) player);
	}
}
