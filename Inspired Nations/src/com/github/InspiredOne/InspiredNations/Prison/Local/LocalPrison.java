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
package com.github.InspiredOne.InspiredNations.Prison.Local;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


import org.bukkit.Location;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class LocalPrison {
	private String country;
	private int town;
	private InspiredNations plugin;
	private polygonPrism polyspace = null;
	private Cuboid cubespace = null;
	HashMap<String, Cell> cells = new HashMap<String, Cell>();
	private Vector<String> builders = new Vector<String>();
	
	public LocalPrison(InspiredNations instance, Cuboid space, String countrytemp, int towntemp) {
		plugin = instance;
		cubespace = space;
		country = countrytemp;
		town = towntemp;
	}
	
	public LocalPrison(InspiredNations instance, polygonPrism space, String countrytemp, int towntemp) {
		plugin = instance;
		polyspace = space;
		country = countrytemp;
		town = towntemp;
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
	
	public HashMap<String, Cell> getCells() {
		return cells;
	}
	
	public void addCell(String name, Cell spot) {
		cells.put(name, spot);
	}
	
	public void removeCell(String name) {
		cells.remove(name);
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
		return builders.contains(builder);
	}
	
	public Vector<String> getOccupants() {
		Vector<String> occupants = new Vector<String>();
		for (Iterator<String> i = cells.keySet().iterator(); i.hasNext();) {
			occupants.addAll(cells.get(i.next()).getOccupant());
		}
		return occupants;
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
