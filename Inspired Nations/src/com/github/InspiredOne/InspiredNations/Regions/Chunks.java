package com.github.InspiredOne.InspiredNations.Regions;

import java.awt.Point;
import java.util.Vector;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class Chunks {

	public Vector<Point> Chunks = new Vector<Point>();
	String world = "";
	
	public Chunks() {
		world = " ";
	}
	
	public Chunks(Vector<Chunk> tiles) {
		Vector<Point> temp = new Vector<Point>();
		for (int i = 0; i < tiles.size(); i++) {
			temp.add(new Point(tiles.get(i).getX(), tiles.get(i).getZ()));
		}
		Chunks.addAll(temp);
		world = tiles.get(0).getWorld().getName();
	}
	
	public Chunks(Vector<Point> tiles, String worldtemp) {
		Chunks.addAll(tiles);
		world = worldtemp;
	}
	
	public void addChunks(Vector<Chunk> tiles) {
		Vector<Point> temp = new Vector<Point>();
		for (int i = 0; i < tiles.size(); i++) {
			temp.add(new Point(tiles.get(i).getX(), tiles.get(i).getZ()));
		}
		Chunks.addAll(temp);
	}
	
	public void addChunk(Point tile) {
		Chunks.add(tile);
	}
	
	public void addChunk(Chunk tile) {
		Chunks.add(new Point(tile.getX(), tile.getZ()));
	}
	
	public void removeChunk(Chunk tile) {
		Chunks.remove(new Point(tile.getX(), tile.getZ()));
	}
	
	public void removeChunk(Point tile) {
		Chunks.remove(tile);
	}
	
	public void removeLast() {
		Chunks.remove(Chunks.size() - 1);
	}

	public boolean isIn(Location spot) {
		Chunk temp = spot.getChunk();
		if (Chunks.contains(new Point(temp.getX(), temp.getZ())) && spot.getWorld().getName().equals(world)) return true;
		else return false;
	}
	
	public boolean isIn(Point spot, String worldname) {
		if (Chunks.contains(spot) && world.equals(worldname)) return true;
		else return false;
	}
	
	public int Area() {
		return Chunks.size()*256;
	}
	
	public int Volume() {
		return Area()*256;
	}
	
	public String getWorld() {
		return world;
	}
	
	public void setWorld(String name) {
		world = name;
	}
}
