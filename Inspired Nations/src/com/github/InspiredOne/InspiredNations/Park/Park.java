package com.github.InspiredOne.InspiredNations.Park;

import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.Vector;


import org.bukkit.Location;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class Park {
	private String country;
	private int town;
	private InspiredNations plugin;
	private polygonPrism polyspace = null;
	private Cuboid cubespace = null;
	private Vector<String> builders = new Vector<String>();
	private String name = "Park";
	private int protectionLevel = 1;
	private int futureprotectionlevel = 1;
	
	public Park(InspiredNations instance, Cuboid space, String countrytemp, int towntemp, String nametemp) {
		plugin = instance;
		cubespace = space;
		country = countrytemp;
		town = towntemp;
		name = nametemp;
	}
	
	public Park(InspiredNations instance, polygonPrism space, String countrytemp, int towntemp, String nametemp) {
		plugin = instance;
		polyspace = space;
		country = countrytemp;
		town = towntemp;
		name = nametemp;
	}
	
	public BigDecimal getTaxAmount() {
		if (town == -1) {
			return plugin.countrydata.get(country.toLowerCase()).getMoneyMultiplyer().multiply(new BigDecimal(Volume() * getProtectionLevel() * plugin.getConfig().getDouble("federal_park_base_cost")).multiply(plugin.countrydata.get(country.toLowerCase()).getMoneyMultiplyer()));
		}
		else {
			return plugin.countrydata.get(country.toLowerCase()).getTowns().get(town).getMoneyMultiplyer().multiply((new BigDecimal(Volume() * getProtectionLevel() * plugin.countrydata.get(country.toLowerCase()).getTaxRate()/10000 * plugin.getConfig().getDouble("park_tax_multiplyer") *
					plugin.countrydata.get(country.toLowerCase()).getTowns().get(town).getProtectionLevel())));
		}
	}
	
	public BigDecimal getTaxAmount(int level) {
		if (town == -1) {
			return plugin.countrydata.get(country.toLowerCase()).getMoneyMultiplyer().multiply(new BigDecimal(Volume() * level * plugin.getConfig().getDouble("federal_park_base_cost")).multiply(plugin.countrydata.get(country.toLowerCase()).getMoneyMultiplyer()));
		}
		else {
			return plugin.countrydata.get(country.toLowerCase()).getTowns().get(town).getMoneyMultiplyer().multiply((new BigDecimal(Volume() * level * plugin.countrydata.get(country.toLowerCase()).getTaxRate()/10000 * plugin.getConfig().getDouble("park_tax_multiplyer") *
					plugin.countrydata.get(country.toLowerCase()).getTowns().get(town).getProtectionLevel())));
		}
	}
	
	public void setAddress(String countrytemp, int towntemp) {
		country = countrytemp;
		town = towntemp;
	}
	public void setPolySpace(polygonPrism space) {
		polyspace = space;
	}
	
	public void setCubeSpace(Cuboid space) {
		cubespace = space;
	}
	
	public InspiredNations getPlugin() {
		return plugin;
	}
	
	public void setProtectionLevel(int level) {
		protectionLevel = level;
	}
	
	public void setFutureProtectionLevel(int level) {
		futureprotectionlevel = level;
	}
	
	public int getProtectionLevel() {
		return protectionLevel;
	}
	
	public int getFutureProtectionLevel() {
		return futureprotectionlevel;
	}
	
	public Boolean isPolySpace() {
		try {	
			if (cubespace.equals(null)) {
				return true;
			}
			else return false;
		}
		catch (Exception ex) {
			return true;
		}
	}
	
	public Boolean isCubeSpace() {
		try {	
			if (polyspace.equals(null)) {
				return true;
			}
			else return false;
		}
		catch (Exception ex) {
			return true;
		}
	}
	
	public void setBuilders(Vector<String> builderstemp) {
		builders = builderstemp;
	}
	
	public void addBuilder(String builder) {
		builders.add(builder);
	}
	
	public void removeBuilder(String builder) {
		builders.remove(builder);
	}
	
	public Vector<String> getBuilders() {
		return builders;
	}
	
	public boolean isBuilder(String builder) {
		return builders.contains(builder.toLowerCase());
	}
	
	public void setName(String nametemp) {
		name = nametemp;
	}
	
	public String getName() {
		return name;
	}
	
	public Boolean isIn(Location tile) {
		if (isPolySpace()) {
			return polyspace.isIn(tile);
		}
		else if (isCubeSpace()) {
			return cubespace.isIn(tile);
		}
		else return false;
	}
	
	public int Area() {
		if (isPolySpace()) {
			return polyspace.Area();
		}
		else if (isCubeSpace()) {
			return cubespace.Area();
		}
		else return 0;
	}
	
	public int Volume() {
		if (isPolySpace()) {
			return polyspace.Volume();
		}
		else if (isCubeSpace()) {
			return cubespace.Volume();
		}
		else return 0;
	}
	
	public int Perimeter() {
		if (isPolySpace()) {
			return polyspace.Perimeter();
		}
		else if (isCubeSpace()) {
			return cubespace.Perimeter();
		}
		else return 0;
	}
	
	public String getCountry() {
		return country;
	}
	
	public int getTown() {
		return town;
	}
	
	public Cuboid getCubeSpace() {
		return cubespace;
	}
	
	public polygonPrism getPolySpace() {
		return polyspace;
	}
	
	public boolean isInTown() {
		Town townIn = plugin.countrydata.get(country.toLowerCase()).getTowns().get(town);
		if (isPolySpace()) {
			Rectangle rect = polyspace.getPolygon().getBounds();
			for (int i = (int) rect.getMinX(); i < (int) rect.getMaxX() + 1; i++) {
				for (int j = (int) rect.getMinY(); j < (int)rect.getMaxY() + 1; j++) {
					for (int l = polyspace.getYMin(); l < polyspace.getYMax(); l++) {
						Location test = new Location(plugin.getServer().getWorld(polyspace.getWorld()), i, l, j);
						if ((!townIn.isIn(test)) && polyspace.isIn(test)) {
							return false;
						}
					}
				}
			}
		}
		if (isCubeSpace()) {
			for (int i = cubespace.getXmin(); i < cubespace.getXmax(); i++) {
				for (int j = cubespace.getYmin(); j < cubespace.getYmax(); j++) {
					for (int l = cubespace.getZmin(); l < cubespace.getZmax(); l++) {
						Location test = new Location(plugin.getServer().getWorld(cubespace.getWorld()), i, j, l);
						if ((!townIn.isIn(test))) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
