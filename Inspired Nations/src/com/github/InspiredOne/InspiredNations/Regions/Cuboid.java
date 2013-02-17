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

import org.bukkit.Location;

public class Cuboid {

	private int xmin = 1;
	private int ymin = 1;
	private int zmin = 1;
	private int xmax = 0;
	private int ymax = 0;
	private int zmax = 0;
	private String world;
	
	// Setters
	public Cuboid(String worldname) {
		xmin = 1;
		ymin = 1;
		zmin = 1;
		xmax = 0;
		ymax = 0;
		zmax = 0;
		world = worldname;
	}
	
	public Cuboid(Location one, Location two) {
		int xonetemp = one.getBlockX();
		int yonetemp = one.getBlockY();
		int zonetemp = one.getBlockZ();
		int xtwotemp = two.getBlockX();
		int ytwotemp = two.getBlockY();
		int ztwotemp = two.getBlockZ();
		world = one.getWorld().getName();
		xmin = Math.min(xonetemp, xtwotemp);
		ymin = Math.min(yonetemp, ytwotemp);
		zmin = Math.min(zonetemp, ztwotemp);
		xmax = Math.max(xonetemp, xtwotemp);
		ymax = Math.max(yonetemp, ytwotemp);
		zmax = Math.max(zonetemp, ztwotemp);
	}
	
	public Cuboid(int xmintemp, int ymintemp, int zmintemp, int xmaxtemp, int ymaxtemp, int zmaxtemp, String worldtemp) {
		xmin = xmintemp;
		ymin = ymintemp;
		zmin = zmintemp;
		xmax = xmaxtemp;
		ymax = ymaxtemp;
		zmax = zmaxtemp;
		world = worldtemp;
	}
	
	public void setXmin(int xmintemp) {
		xmin = xmintemp;
	}
	
	public void setYmin(int ymintemp) {
		ymin = ymintemp;
	}
	
	public void setZmin(int zmintemp) {
		zmin = zmintemp;
	}
	
	public void setXmax(int xmaxtemp) {
		xmax = xmaxtemp;
	}
	
	public void setYmax(int ymaxtemp) {
		ymax = ymaxtemp;
	}
	
	public void setZmax(int zmaxtemp) {
		zmax = zmaxtemp;
	}
	
	public void setWorld(String worldtemp) {
		world = worldtemp;
	}
	
	// Getters
	public int getXmin() {
		return xmin;
	}
	
	public int getYmin() {
		return ymin;
	}
	
	public int getZmin() {
		return zmin;
	}
	
	public int getXmax() {
		return xmax;
	}

	public int getYmax() {
		return ymax;
	}
	
	public int getZmax() {
		return zmax;
	}
	
	public String getWorld() {
		return world;
	}
	
	// Gets if location is in cuboid
	public boolean isIn(Location place) {
		if (place.getBlockX() >= xmin && place.getBlockX() <= xmax && place.getWorld().getName().equals(world)) {
			if (place.getBlockY() >= ymin && place.getBlockY() <= ymax) {
				if (place.getBlockZ() >= zmin && place.getBlockZ() <= zmax) {
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}
	
	// Gets volume of the cuboid
	public int Volume() {
		int length = xmax - xmin + 1;
		int width = zmax - zmin + 1;
		int hight = ymax - ymin + 1;
		int volume = length*width*hight;
		return volume;
	}
	
	// Gets the base area of the cuboid
	public int Area() {
		int length = xmax - xmin + 1;
		int width = zmax - zmin + 1;
		int base = length*width;
		return base;
	}
	
	// Gets the perimeter of the base of the cuboid
	public int Perimeter() {
		int length = xmax - xmin + 1;
		int width = zmax - zmin +1;
		int perimeter = 2*length + 2*width;
		return perimeter;
	}
}
