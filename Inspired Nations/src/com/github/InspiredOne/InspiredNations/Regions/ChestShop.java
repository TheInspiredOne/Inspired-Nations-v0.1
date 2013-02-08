package com.github.InspiredOne.InspiredNations.Regions;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.InspiredOne.InspiredNations.InspiredNations;

public class ChestShop {

	private Inventory items;
	private ItemStack itemtype;
	private double price;
	Location[] chests;
	private boolean doubleChest = false;
	private int quantity;
	
	// 1 and 2 for chests, 3 for sign, 4 for block sign is on.
	public ChestShop(Inventory item, ItemStack itemtypetemp, double cost,int quant, Location[] spots, boolean doublechesttemp) {
		items = item;
		itemtype = itemtypetemp;
		price = cost;
		chests = spots.clone();
		doubleChest = doublechesttemp;
		quantity = quant;
	}
	
	public void setMaterial(ItemStack material) {
		itemtype = material;
	}
	
	public ItemStack getMaterial() {
		return itemtype;
	}
	
	public void setPrice(double pricetemp) {
		price = pricetemp;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setInventory(Inventory itemstemp) {
		items = itemstemp;
	}
	
	public Inventory getInventory() {
		return items;
	}
	
	public Location[] getSpot() {
		return chests;
	}
	
	public void setDoubleChest(boolean isit) {
		doubleChest = isit;
	}
	
	public boolean getDoubleChest() {
		return doubleChest;
	}
	
	public void setQuantity(int quant) {
		quantity = quant;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public boolean isIn(Location spot) {
		if (doubleChest) {
			if (spot.getBlock().getLocation().equals(chests[0])) {
				return true;
			}
			if (spot.getBlock().getLocation().equals(chests[1])) {

				return true;
			}
			if (spot.getBlock().getLocation().equals(chests[2])) {
				return true;
			}
			if (spot.getBlock().getLocation().equals(chests[3])) {
				return true;
			}
			return false;
		}
		else {
			if (spot.getBlock().getLocation().equals(chests[0])) {
				return true;
			}
			if (spot.getBlock().getLocation().equals(chests[2])) {
				return true;
			}
			if (spot.getBlock().getLocation().equals(chests[3])) {
				return true;
			}
			return false;
		}
	}
	
}
