package com.github.InspiredOne.InspiredNations.Bank.Local;

import java.awt.Point;
import java.math.BigDecimal;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.Point3D;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ClaimLocalBankPlayerListener {
	InspiredNations plugin;
	Player player;
	String playername;
	PlayerData PDI;
	PlayerModes PM;
	PlayerMoveEvent eventmove;
	PlayerInteractEvent eventinteract;
	ItemStack item;
	
	public ClaimLocalBankPlayerListener(InspiredNations instance, PlayerMoveEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		eventmove = event;
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
	}
	
	public ClaimLocalBankPlayerListener(InspiredNations instance, PlayerInteractEvent event) {
		plugin = instance;
		player = event.getPlayer();
		playername = player.getName().toLowerCase();
		PDI = plugin.playerdata.get(playername);
		PM = plugin.playermodes.get(playername);
		eventinteract = event;
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
	}
	
	public void onPlayerMove() {
		if (!PM.localBankSelect() || !(PM.isSelectingCuboid() || PM.isSelectingPolygon())) return;
		if (!PDI.getIsTownMayor()) return;
	}
	
	public void onPlayerInteract() {
		if (!PM.localBankSelect() || !(PM.isSelectingCuboid() || PM.isSelectingPolygon())) return;
		if (!PDI.getIsTownMayor()) return;
		
		
		// Generic selection code
		if (PM.isSelectingCuboid()) {
			if (!eventinteract.hasItem()) return;
			if (eventinteract.getItem().getTypeId() != item.getTypeId()) return;
			if (eventinteract.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				PM.point1 = eventinteract.getClickedBlock().getLocation();
				if (PM.hasPoint1() && PM.hasPoint2()) {
					PM.setCuboid(new Cuboid(PM.point1, PM.point2));
				}
				player.sendRawMessage(generateMessage());
				player.sendRawMessage(ChatColor.RED + "Point 1 selected, if you haven't selected Point 2 do that now. ");
			}
			if (eventinteract.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				PM.point2 = eventinteract.getClickedBlock().getLocation();
				if (PM.hasPoint1() && PM.hasPoint2()) {
					PM.setCuboid(new Cuboid(PM.point1, PM.point2));
				}
				player.sendRawMessage(generateMessage());
				player.sendRawMessage(ChatColor.RED + "Point 2 selected, if you haven't selected Point 1 do that now. ");
			}
			eventinteract.setCancelled(true);
		}
		
		if (PM.isSelectingPolygon()) {
			if (PM.getAloudSelect()) {
				if (!eventinteract.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
				if (!eventinteract.hasItem()) return;
				if (eventinteract.getItem().getTypeId() != item.getTypeId()) return;
				Town town = PDI.getTownMayored();
				polygonPrism prism = PM.getPolygon();
				Location spot = eventinteract.getClickedBlock().getLocation();
				Point3D spot1 = new Point3D(spot.getWorld().getName(), spot.getBlockX(), spot.getBlockY(), spot.getBlockZ());
				Point3D spot2;
				Point3D spot3;
				Point3D spot4;
				Point3D spot5;
				
				if (!town.isIn(spot)) return;
				prism.addVertex(new Point(spot.getBlockX(), spot.getBlockZ()), spot.getBlockY());
				
				// Make the bedrock marker
				PM.addBlock(spot1, spot.getBlock());
				if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
					spot.getBlock().setTypeId(7);
				}
				spot.setY(spot.getY() + 1);
				spot2 = new Point3D(spot1.getWorld(), spot1.x, spot1.y + 1, spot1.z);
				if (player.getLocation().getBlockX() != spot.getBlockX() || player.getLocation().getBlockZ() != spot.getBlockZ()) {
					PM.addBlock(spot2, spot.getBlock());
					if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
						spot.getBlock().setTypeId(7);
					}
				}
				spot.setY(spot.getY() + 1);
				spot3 = new Point3D(spot2.getWorld(), spot2.x, spot2.y + 1, spot2.z);
				if (player.getLocation().getBlockX() != spot.getBlockX() || player.getLocation().getBlockZ() != spot.getBlockZ()) {
					PM.addBlock(spot3, spot.getBlock());
					if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
						spot.getBlock().setTypeId(7);
					}
				}
				spot.setY(spot.getY() + 1);
				spot4 = new Point3D(spot3.getWorld(), spot3.x, spot3.y + 1, spot3.z);
				PM.addBlock(spot4,  spot.getBlock());
				if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
					spot.getBlock().setTypeId(7);
				}
				spot.setY(spot.getY() + 1);
				spot5 = new Point3D(spot4.getWorld(), spot4.x, spot4.y + 1, spot4.z);
				PM.addBlock(spot5, spot.getBlock());
				if (spot.getBlock().getTypeId() != 54 && spot.getBlock().getTypeId() != 52) {
					spot.getBlock().setTypeId(7);
				}
				
				// Delays the clicks
				PM.setAloudSelect(false);
				plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
						public void run() {
							PM.setAloudSelect(true);
						}
				}, 10);
				
	
				player.sendRawMessage(generateMessage());
			}
			eventinteract.setCancelled(true);
		}
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
	
	// A method to generate the message
	public String generateMessage() {
		String[] namesplit = item.getType().name().split("_");
		String name = "";
		for (int i = 0; i < namesplit.length; i++) {
			name = name.concat(namesplit[i] + " ");
		}
		int size = 0;
		if (PM.isSelectingCuboid()) {
			size = PM.getCuboid().Volume();
		}
		if (PM.isSelectingPolygon()) {
			size = PM.getPolygon().Volume();
		}
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Claim Bank: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Cubic Meters. " + ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back.";

		if (PM.isSelectingCuboid()) {
			options = options.concat("Use a " + name.toLowerCase() + "to select the two opposite corners of your cuboid. Left click for one corner and right click for the other." +
					" Type 'Finish' when you are done or 'Cancel' to abandon. " + ChatColor.DARK_AQUA + repeat("-", 53));
		}
		
		if (PM.isSelectingPolygon()) {
			options = options.concat("Use a " + name.toLowerCase() + "to select the corners of your bank. The lowest selection will be the bottom and the highest selection" +
					" will be the top. Type 'Finish' when you are done or 'Cancel' to abandon. " + ChatColor.DARK_AQUA + repeat("-", 53));
		}
		return space + main + options + end;
	}
}
