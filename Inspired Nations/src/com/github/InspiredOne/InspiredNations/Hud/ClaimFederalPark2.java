package com.github.InspiredOne.InspiredNations.Hud;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
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
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;

public class ClaimFederalPark2 extends StringPrompt{

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	PlayerModes PM;
	Country country;
	Vector<String> input = new Vector<String>();
	ItemStack item;
	int error;
	
	// Constructor
	public ClaimFederalPark2(InspiredNations instance, Player playertemp, int errortemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		PM = plugin.playermodes.get(player.getName().toLowerCase());
		country = PDI.getCountryResides();
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
		String[] namesplit = item.getType().name().split("_");
		String name = "";
		for (int i = 0; i < namesplit.length; i++) {
			name = name.concat(namesplit[i] + " ");
		}
		
		int size;
		if (PM.isSelectingCuboid()) {
			size = PM.getCuboid().Volume();
		}
		else {
			size = PM.getPolygon().Volume();
		}
		BigDecimal expenditures =  cut(cut(new BigDecimal(size * plugin.getConfig().getDouble("federal_park_base_cost")).multiply(country.getMoneyMultiplyer())));
		
		int sizeLength = (int) ((size + "").length() * 1);
		int expendituresLength = (int) (expenditures.toString().length() * 1);
		
		String space = ChatColor.DARK_AQUA + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + ChatColor.GOLD;
		String main = ChatColor.BOLD + "Claim Park: Type what you would like to do." + ChatColor.RESET + repeat(" ", 5)+ ChatColor.DARK_AQUA + repeat("-", 53) + ChatColor.YELLOW;
		String options = "";
		String end = ChatColor.GOLD + "" + size + ChatColor.YELLOW + " Cubic Meters. Cost: " + ChatColor.GOLD + expenditures + ChatColor.YELLOW + " " + country.getPluralMoney() + " per year. " 
		+ ChatColor.DARK_AQUA + repeat("-", 26 - sizeLength - expendituresLength - (int) (country.getPluralMoney().length()*1.4)) + ChatColor.AQUA + " Type 'exit' to leave or 'back' to go back." + repeat(" ", 25);
		String errormsg = ChatColor.RED + "";
		if (error == 1) {
			errormsg = errormsg.concat(ChatColor.RED + "That is not an option. Check your spelling?");
		}

		options = options.concat("Use a " + name.toLowerCase() + "to select the corners of your park. Type 'Finish' when you are done or 'Cancel' to abandon. ");
		
		String map = "" + ChatColor.DARK_AQUA + repeat("-", 53);
		Location spot = player.getLocation();
		Point chunk = new Point(spot.getChunk().getX(), spot.getChunk().getZ());
		Chunks area = country.getArea();
		
		input.add("finish");
		input.add("back");
		input.add("cancel");
		
		for (int z = -8; z < 4; z++) {
			for (int x = -26; x < 27; x++) {
				Point test = new Point(chunk.x + x, chunk.y + z);
				if (z == 0 && x == 0) {
					if (plugin.chunks.containsKey(chunk) && !area.isIn(chunk, spot.getWorld().getName())) {
						if (plugin.countrydata.get(plugin.chunks.get(chunk).toLowerCase()).getArea().getWorld().equals(spot.getWorld().getName())) {
							map = map.concat(ChatColor.RED + "@");
						}
						else {
							map = map.concat(ChatColor.GRAY + "@");
						}
					}
					else if (area.isIn(chunk, spot.getWorld().getName())) {
						map = map.concat(ChatColor.GREEN + "@");
					}
					else {
						map = map.concat(ChatColor.GRAY + "@");
					}
				}
				if (z != 0 || x != 0) {
					if (area.isIn(test, spot.getWorld().getName())) {
						map = map.concat(ChatColor.GREEN + "+");
					}
					else if (!area.isIn(test, spot.getWorld().getName().toLowerCase()) && plugin.chunks.containsKey(test)) {
						if (plugin.countrydata.get(plugin.chunks.get(test)).getArea().getWorld().equals(spot.getWorld().getName())) {
							map = map.concat(ChatColor.RED + "+");
						}
						else {
							map = map.concat(ChatColor.GRAY + "/");
						}
					}
					else {
						map = map.concat(ChatColor.GRAY + "/");
					}
				}
			}
		}
		
		// Direction Icon
		if ((-45 < spot.getYaw() && 45 >= spot.getYaw()) || (315 < spot.getYaw() && 360 >= spot.getYaw())
				|| (-360 < spot.getYaw() && -315 >= spot.getYaw())) {
			map = map.substring(0, 1566).concat("`|`").concat(map.substring(1567));
		}
		if ((45 < spot.getYaw() && 135 >= spot.getYaw()) || (-315 < spot.getYaw() && -225 >= spot.getYaw())) {
			map = map.substring(0, 1404).concat("-").concat(map.substring(1405));
		} 
		if ((135 < spot.getYaw() && 225 >= spot.getYaw()) || (-225 < spot.getYaw() && -135 >= spot.getYaw())) {
			map = map.substring(0, 1248).concat(",|,").concat(map.substring(1249));

		}
		if ((225 < spot.getYaw() && 315 >= spot.getYaw()) || (-135 < spot.getYaw() && -45 >= spot.getYaw())) {
			map = map.substring(0, 1410).concat("-").concat(map.substring(1411));

		}
		
		map = map.concat(ChatColor.GREEN + " +" + ChatColor.YELLOW + " = Your Country." + ChatColor.RED + " +" + ChatColor.YELLOW + " = Others." + ChatColor.GRAY + " /" + ChatColor.YELLOW + " = Unclaimed Land. @ = You.     ");
		return space + main + options + map + end + errormsg;
	}
	
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg) {

		if (arg.startsWith("/")) {
			arg = arg.substring(1);
		}

		if (!input.contains(arg.toLowerCase())) return new ClaimFederalPark2(plugin, player, 1);
		
		// back
		if (arg.equalsIgnoreCase("back")) {
			PM.setBlocksBack();
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			return new ClaimFederalPark1(plugin, player, 0);
		}

		
		// finish
		if (arg.equalsIgnoreCase("finish")) {
			if (PM.isSelectingPolygon()) {
				Rectangle rect = PM.getPolygon().getPolygon().getBounds();
				if (!isSimple(PM.getPolygon().getPolygon())) {
					PM.setBlocksBack();
					PM.federalPark(false);
					return new InvalidLand(plugin, player, 1, "federalpark", 0);
				}

				if (PM.getPolygon().Area() == 0){
					PM.setBlocksBack();
					PM.federalPark(false);
					return new InvalidLand(plugin, player, 4, "federalpark", 0);
				}

					
				for (int i = (int) rect.getMinX(); i < (int) rect.getMaxX(); i++) {
					
					// Makes the Progress Bar
					int done = (int) ((i - rect.getMinX() + 1)/(rect.getMaxX() - rect.getMinX() + 2) * 30);
					player.sendRawMessage(ChatColor.YELLOW + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + "Determining if selection is valid." + repeat(" ", 35));
					player.sendRawMessage(ChatColor.GRAY + " [" + ChatColor.GREEN + repeat("#",done) +
							ChatColor.GRAY + repeat("#", (int) (30 - done)) + "]");
					
					for (int j = (int) rect.getMinY(); j < (int)rect.getMaxY(); j++) {
						for (int l = PM.getPolygon().getYMin(); l <= PM.getPolygon().getYMax(); l++) {
							Location test = null;
							test = new Location(plugin.getServer().getWorld(PM.getPolygon().getWorld()), i, l, j);
							
							if ((!country.isIn(test)) && PM.getPolygon().isIn(test)) {
								PM.setBlocksBack();
								PM.federalPark(false);
								return new InvalidLand(plugin, player, 3, "federalpark", 0);
							}
							for (Park park : country.getParks()) {
								if ((PM.getPolygon().isIn(test) && park.isIn(test))) {
									PM.setBlocksBack();
									PM.federalPark(false);
									return new InvalidLand(plugin, player, 2, "federalpark", 0);
								}
							}
						}
					}
				}
				PM.setBlocksBack();
				PM.federalPark(false);
				country.addPark(new Park(plugin, PM.getPolygon(), country.getName(), -1, "Park " + (country.getParks().size()+1)));
			}
			else {
				if (PM.getCuboid().Volume() == 0) {
					PM.setBlocksBack();
					PM.federalPark(false);
					return new InvalidLand(plugin, player, 4, "federalpark");
				}
				for (int i = PM.getCuboid().getXmin(); i <= PM.getCuboid().getXmax(); i++) {
					
					// Makes the Progress Bar
					int done = (int) ((i - PM.getCuboid().getXmin()) * 30/(PM.getCuboid().getXmax() - PM.getCuboid().getXmin()));
					player.sendRawMessage(ChatColor.YELLOW + repeat(" ", plugin.getConfig().getInt("hud_pre_message_space")) + "Determining if selection is valid." + repeat(" ", 35));
					player.sendRawMessage(ChatColor.GRAY + " [" + ChatColor.GREEN + repeat("#", done) +
							ChatColor.GRAY + repeat("#", (int) (30 - done)) + "]");
					
					for (int j = PM.getCuboid().getYmin(); j <= PM.getCuboid().getYmax(); j++) {
						for (int l = PM.getCuboid().getZmin(); l <= PM.getCuboid().getZmax(); l++) {
							Location test = null;
							test = new Location(plugin.getServer().getWorld(PM.getCuboid().getWorld()), i, j, l);

							if ((!country.isIn(test))) {
								PM.setBlocksBack();
								PM.federalPark(false);
								return new InvalidLand(plugin, player, 3, "federalpark");
							}
							for (Park park : country.getParks()) {
								if ((PM.getCuboid().isIn(test) && park.isIn(test))) {
									PM.setBlocksBack();
									PM.federalPark(false);
									return new InvalidLand(plugin, player, 2, "federalpark", 0);
								}
							}
						}
					}
				}
				PM.setBlocksBack();
				PM.federalPark(false);
				country.addPark(new Park(plugin, PM.getCuboid(), country.getName(), -1, "Park " + country.getParks().size()+1));

			}
			
			return new CountryGovernmentRegions(plugin, player, 0);
		}
		
		// cancel
		if (arg.equalsIgnoreCase("cancel")) {
			PM.federalPark(false);
			PM.setBlocksBack();
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			return new CountryGovernmentRegions(plugin, player, 0);
		}
		return new CountryGovernmentRegions(plugin, player, 1);
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
