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
package com.github.InspiredOne.InspiredNations.Regions;

import java.util.HashMap;

import org.bukkit.Location;

public class flatArea {
	
	private HashMap<Location, Integer> blocks;

	// Creates the area
	public flatArea(Location tile) {
		blocks.put(tile, 0);
	}
	
	public flatArea(HashMap<Location, Integer> areatemp) {
		blocks = areatemp;
	}
	
	public flatArea() {
	}

	public void setArea(Location tile) {
		tile.setX(tile.getBlockX());
		tile.setY(tile.getBlockY());
		tile.setZ(tile.getBlockZ());
		blocks.put(tile, 0);
		return;
	}

	// Getters
	public HashMap<Location, Integer> getArea() {
		return blocks;
	}
	
	// Returns if location is in the flat area
	public boolean isIn(Location tile) {
		tile.setX(tile.getBlockX());
		tile.setY(tile.getBlockY());
		tile.setZ(tile.getBlockZ());
		if (blocks.containsKey(tile)) return true;
		else return false;
	}
	
	// Returns the size of the flat area
	public Integer size() {
		return blocks.size();
	}
}
