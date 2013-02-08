package com.github.InspiredOne.InspiredNations.Hud;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerData;
import com.github.InspiredOne.InspiredNations.PlayerModes;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class ClaimPark2 extends StringPrompt {
	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	Country country;
	Town town;
	TownMethods TMI;
	Vector<String> input = new Vector<String>();
	ItemStack item;
	int error;
	
	// Constructor
	public ClaimPark2(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryResides();
		town = PDI.getTownMayored();
		TMI = new TownMethods(plugin, town);
		item = new ItemStack(plugin.getConfig().getInt("selection_tool"));
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
		int size = 0;
		
		String[] namesplit = item.getType().name().split("_");
		String name = "";
		for (int i = 0; i < namesplit.length; i++) {
			name = name.concat(namesplit[i] + " ");
		}
		
		if (PM.isSelectingCuboid()) {
			size = PM.getCuboid().Volume();
		}
		if (PM.isSelectingPolygon()) {
			size = PM.getPolygon().Volume();
		}
		
		BigDecimal expenditures = cut(new BigDecimal(size * town.getNationTax() * plugin.getConfig().getDouble("park_tax_multiplyer")));
		
		int sizeLength = (int) ((size + "").length() * .8);
		int expendituresLength = (int) (expenditures.toString().length() * .8);
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Claim Park: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Cubic Meters. " + repeat(" ", 55 - sizeLength)  + "Cost: " + ChatColor.GOLD + expenditures + 
				ChatColor.YELLOW + " " + town.getPluralMoney() + " per year." + repeat(" ", 20 - expendituresLength) + ChatColor.DARK_AQUA + repeat("-", 53)
				+ ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}
		input.add("back");
		input.add("exit");
		input.add("finish");
		input.add("cancel");
		if (PM.isSelectingCuboid()) {
			options = options.concat("Use a " + name.toLowerCase() + "to select the two opposite corners of your cuboid. Left click for one corner and right click for the other." +
					" Type 'Finish' when you are done or 'Cancel' to abandon. " + ChatColor.DARK_AQUA + repeat("-", 53));
		}
		
		if (PM.isSelectingPolygon()) {
			options = options.concat("Use a " + name.toLowerCase() + "to select the corners of your park. The lowest selection will be the bottom and the highest selection" +
					" will be the top. Type 'Finish' when you are done or 'Cancel' to abandon. " + ChatColor.DARK_AQUA + repeat("-", 53));
		}
		return space + main + options + end + errormsg;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {
		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}
		if (!input.contains(arg.toLowerCase())) return new ClaimPark2(plugin, player, 1);
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			PM.setBlocksBack();
			return new ClaimPark1(plugin, player, 0);
		}
		
		// Finish
		if (arg.equalsIgnoreCase("finish")) {
			if (PM.isSelectingPolygon()) {
				Rectangle rect = PM.getPolygon().getPolygon().getBounds();
				if (!isSimple(PM.getPolygon().getPolygon())) {
					PM.setBlocksBack();
					PM.park(false);
					return new InvalidLand(plugin, player, 1, "park");
				}
				if (PM.getPolygon().Volume() == 0) {
					PM.setBlocksBack();
					PM.park(false);
					return new InvalidLand(plugin, player, 4, "park");
				}
				for (int i = (int) rect.getMinX() - 1; i <= (int) rect.getMaxX() + 1; i++) {
					
					// Makes the Progress Bar
					int done = (int) ((i - rect.getMinX() + 1)/(rect.getMaxX() - rect.getMinX() + 2) * 30);
					player.sendRawMessage(ChatColor.YELLOW + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + "Determining if selection is valid." + repeat(" ", 35));
					player.sendRawMessage(ChatColor.GRAY + " [" + ChatColor.GREEN + repeat("#",done) +
							ChatColor.GRAY + repeat("#", (int) (30 - done)) + "]");
					
					for (int j = (int) rect.getMinY() - 1; j <= (int)rect.getMaxY() + 1; j++) {
						for (int l = PM.getPolygon().getYMin() - 1; l <= PM.getPolygon().getYMax() + 1; l++) {
							Location test = new Location(player.getWorld(), i, l, j);
							if ((!town.isIn(test)) && PM.getPolygon().isIn(test)) {
								PM.setBlocksBack();
								PM.park(false);
								return new InvalidLand(plugin, player, 3, "park");
							}
							if (PM.getPolygon().Volume() == 0) {
								PM.setBlocksBack();
								PM.park(false);
								return new InvalidLand(plugin, player, 2, "park");
							}
							for (Iterator<House> k = town.getHouses().iterator(); k.hasNext();) {
								House housetemp = k.next();
								if (housetemp.isIn(test) && PM.getPolygon().isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							for (Iterator<GoodBusiness> k = town.getGoodBusinesses().iterator(); k.hasNext();) {
								GoodBusiness businesstemp = k.next();
								if (businesstemp.isIn(test) && PM.getPolygon().isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							for (Iterator<ServiceBusiness> k = town.getServiceBusinesses().iterator(); k.hasNext();) {
								ServiceBusiness businesstemp = k.next();
								if (businesstemp.isIn(test) && PM.getPolygon().isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							for (Iterator<Park> k = town.getParks().iterator(); k.hasNext();) {
								Park parktemp = k.next();
								if (parktemp.isIn(test) && PM.getPolygon().isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasTownHall()) {
								if (town.getTownHall().isIn(test) && PM.getPolygon().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasHospital()) {
								if (town.getHospital().isIn(test) && PM.getPolygon().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasPrison()) {
								if (town.getPrison().isIn(test) && PM.getPolygon().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasBank()) {
								if (town.getBank().isIn(test) && PM.getPolygon().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
						}
					}
				}
				town.addPark(new Park(plugin, PM.getPolygon(), town.getCountry(), plugin.countrydata.get(town.getCountry().toLowerCase()).getTowns().indexOf(town),("Park " + (town.getParks().size()+1))));
			}
			
			if (PM.isSelectingCuboid()) {
				if (PM.getCuboid().Volume() == 0) {
					PM.setBlocksBack();
					PM.park(false);
					return new InvalidLand(plugin, player, 4, "park");
				}
				for (int i = PM.getCuboid().getXmin(); i <= PM.getCuboid().getXmax(); i++) {
					
					// Makes the Progress Bar
					int done = (int) ((i - PM.getCuboid().getXmin()) * 30/(PM.getCuboid().getXmax() - PM.getCuboid().getXmin()));
					player.sendRawMessage(ChatColor.YELLOW + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + "Determining if selection is valid." + repeat(" ", 35));
					player.sendRawMessage(ChatColor.GRAY + " [" + ChatColor.GREEN + repeat("#", done) +
							ChatColor.GRAY + repeat("#", (int) (30 - done)) + "]");
					
					for (int j = PM.getCuboid().getYmin(); j <= PM.getCuboid().getYmax(); j++) {
						for (int l = PM.getCuboid().getZmin(); l <= PM.getCuboid().getZmax(); l++) {
							Location test = new Location(player.getWorld(), i, j, l);
							if ((!town.isIn(test))) {
								PM.setBlocksBack();
								PM.park(false);
								return new InvalidLand(plugin, player, 3, "park");
							}
							for (Iterator<House> k = town.getHouses().iterator(); k.hasNext();) {
								House housetemp = k.next();
								if (housetemp.isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							for (Iterator<GoodBusiness> k = town.getGoodBusinesses().iterator(); k.hasNext();) {
								GoodBusiness businesstemp = k.next();
								if (businesstemp.isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							for (Iterator<ServiceBusiness> k = town.getServiceBusinesses().iterator(); k.hasNext();) {
								ServiceBusiness businesstemp = k.next();
								if (businesstemp.isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							for (Iterator<Park> k = town.getParks().iterator(); k.hasNext();) {
								Park parktemp = k.next();
								if (parktemp.isIn(test)){
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasTownHall()) {
								if (town.getTownHall().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasHospital()) {
								if (town.getHospital().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasPrison()) {
								if (town.getPrison().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
							if (town.hasBank()) {
								if (town.getBank().isIn(test) && PM.getPolygon().isIn(test)) {
									PM.setBlocksBack();
									PM.park(false);
									return new InvalidLand(plugin, player, 2, "park");
								}
							}
						}
					}
				}
				town.addPark(new Park(plugin, PM.getCuboid(), town.getCountry(), plugin.countrydata.get(town.getCountry().toLowerCase()).getTowns().indexOf(town),("Park " + (town.getParks().size()+1))));
			}
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			PM.park(false);
			PM.setBlocksBack();
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
			return new TownGovernmentRegions(plugin, player, 0);
		}
		
		// Cancel
		if (arg.equalsIgnoreCase("cancel")) {
			PM.setBlocksBack();
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			PM.park(false);
			PM.setPolygon(new polygonPrism(player.getWorld().getName()));
			PM.setCuboid(new Cuboid(player.getWorld().getName()));
		}
		return new TownGovernmentRegions(plugin, player, 0);
	}

	
	// A method to determine if a polygon is simple
	public boolean isSimple(Polygon poly) {
		Vector<Line2D> lines = new Vector<Line2D>();
		Line2D line;
		Line2D line2;
		for (int i = 0; i < poly.npoints; i++) {
			if (!((i+1)<poly.npoints)) {
				line = new Line2D.Double(new Point(poly.xpoints[i], poly.ypoints[i]), new Point(poly.xpoints[0], poly.ypoints[0]));
			}
			else {
				line = new Line2D.Double(new Point(poly.xpoints[i], poly.ypoints[i]), new Point(poly.xpoints[i+1], poly.ypoints[i+1]));
			}
			lines.add(line);
		}
		for (int i = 0; i < lines.size(); i++) {
			line = (Line2D) lines.get(i).clone();
			for (int j = 0; j < lines.size(); j++) {
				line2 = (Line2D) lines.get(j).clone();
				if (i != j && i != (j+1) && i != (j-1) && !(i == 0 && j == lines.size() - 1) && !(i == lines.size() - 1 && j == 0) && line.intersectsLine(line2)) {
					return false;
				}
			}
		}
		return true;
	}
}
