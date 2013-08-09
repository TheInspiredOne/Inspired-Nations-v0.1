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

import java.util.Vector;

import org.bukkit.Location;
import org.bukkit.World;

public class Cell {
	private Location spot;
	private String name;
	private boolean occupied = false;
	private Vector<String> occupant = new Vector<String>();
	
	public Cell(Location spottemp, String nametemp) {
		spot = spottemp;
		name = nametemp;
	}
	
	public void setOccupied(boolean isoccupied) {
		occupied = isoccupied;
	}
	
	public void addOccupant(String name) {
		occupant.add(name.toLowerCase());
	}
	
	public void removeOccupant(String name) {
		occupant.remove(name.toLowerCase());
	}
	
	public void setLocation(Location spottemp) {
		spot = spottemp;
	}
	
	public void setName(String nametemp) {
		name = nametemp;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public Vector<String> getOccupant() {
		return occupant;
	}
	
	public Location getSpot() {
		return spot;
	}
	
	public String getName() {
		return name;
	}
	
	public World getWorld() {
		return spot.getWorld();
	}
	
	public int getBlockX() {
		return spot.getBlockX();
	}
	
	public int getBlockY() {
		return spot.getBlockY();
	}
	
	public int getBlockZ() {
		return spot.getBlockZ();
	}
	
	public float getPitch() {
		return spot.getPitch();
	}
	
	public float getYaw() {
		return spot.getYaw();
	}
}
