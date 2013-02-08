package com.github.InspiredOne.InspiredNations.Road;

import java.util.HashMap;


import org.bukkit.Location;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.Regions.flatArea;

public class Road {

	private InspiredNations plugin;
	private flatArea road;
	private String name;
	
	
	public Road(InspiredNations instance) {
		plugin = instance;
	}
	
	public Road(InspiredNations instance, Location tile, String nametemp) {
		plugin = instance;
		road.setArea(tile);
		name = nametemp;
	}
	
	public Road(InspiredNations instance, HashMap<Location, Integer> areatemp, String nametemp) {
		plugin = instance;
		road = new flatArea(areatemp);
		name = nametemp;
	}
	
	public void setArea(Location tile) {
		road.setArea(tile);
		return;
	}
	
	public InspiredNations getPlugin() {
		return plugin;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getIsIn(Location tile) {
		Location temp1 = new Location(tile.getWorld(), tile.getX(), tile.getY() - 1, tile.getZ());
		Location temp2 = new Location(tile.getWorld(), tile.getX(), tile.getY() - 2, tile.getZ());
		if (road.isIn(tile) || road.isIn(temp1) || road.isIn(temp2)) {
			return true;
		}
		else return false;
	}
	
	public int size() {
		return road.size();
	}
	
	public HashMap<Location, Integer> getBlocks() {
		return road.getArea();
	}
}
