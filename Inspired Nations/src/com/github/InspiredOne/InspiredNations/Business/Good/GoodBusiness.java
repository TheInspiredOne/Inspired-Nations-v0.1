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
package com.github.InspiredOne.InspiredNations.Business.Good;

import java.awt.Rectangle;
import java.util.Vector;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.Regions.ChestShop;
import com.github.InspiredOne.InspiredNations.Regions.Cuboid;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class GoodBusiness {
	
	private String country;
	private int town;
	private InspiredNations plugin;
	private polygonPrism polyspace = null;
	private Cuboid cubespace = null;
	private Vector<String> owners = new Vector<String>();
	private String name = "";
	private Vector<String> builders = new Vector<String>();
	private Vector<ChestShop> shops = new Vector<ChestShop>();
	private Vector<String> employmentrequest = new Vector<String>();
	private Vector<String> employmentoffers = new Vector<String>();
	private Vector<String> ownerrequest = new Vector<String>();
	private Vector<String> owneroffers = new Vector<String>();
	private Vector<String> employees = new Vector<String>();
	private int protectionLevel = 1;
	private int futureprotectionlevel = 1;
	
	public GoodBusiness(InspiredNations instance, Cuboid space, Player owner, String countrytemp, int towntemp, String nametemp) {
		plugin = instance;
		cubespace = space;
		owners.add(owner.getName());
		country = countrytemp;
		town = towntemp;
		name = nametemp;
	}
	
	public GoodBusiness(InspiredNations instance, Cuboid space, Vector<String> owner, String countrytemp, int towntemp, String nametemp) {
		plugin = instance;
		cubespace = space;
		owners = owner;
		country = countrytemp;
		town = towntemp;
		name = nametemp;
	}
	
	public GoodBusiness(InspiredNations instance, polygonPrism space, Player owner, String countrytemp, int towntemp, String nametemp) {
		plugin = instance;
		polyspace = space;
		owners.add(owner.getName());
		country = countrytemp;
		town = towntemp;
		name = nametemp;
	}
	
	public GoodBusiness(InspiredNations instance, polygonPrism space, Vector<String> owner, String countrytemp, int towntemp, String nametemp) {
		plugin = instance;
		polyspace = space;
		owners = owner;
		country = countrytemp;
		town = towntemp;
		name = nametemp;
	}
	
	public void setAddress(String countrytemp, int towntemp) {
		country = countrytemp;
		town = towntemp;
	}
	
	public void setPolySpace(polygonPrism space) {
		polyspace = space;
		cubespace = null;
	}
	
	public void setCubeSpace(Cuboid space) {
		polyspace = null;
		cubespace = space;
	}
	
	public polygonPrism getPolySpace() {
		return polyspace;
	}
	
	public Cuboid getCubeSpace() {
		return cubespace;
	}
	
	public void setOwner(Player owner) {
		owners.add(owner.getName());
	}
	
	public void setOwners(Vector<String> owner) {
		owners = owner;
	}
	
	public void addOwner(Player owner) {
		owners.add(owner.getName());
	}
	
	public void removeOwner(Player owner) {
		owners.remove(owner.getName());
	}
	
	public void addChestShop(ChestShop shop) {
		shops.add(shop);
	}
	public void removeChestShop(ChestShop shop) {
		shops.remove(shop);
	}
	
	public Vector<ChestShop> getChestShop() {
		return shops;
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
	
	public void setEmployOffers(Vector<String> employoffers) {
		employmentoffers = employoffers;
	}
	
	public void addEmployOffer(String employname) {
		employmentoffers.add(employname);
	}
	
	public void removeEmployOffer(String employname) {
		employmentoffers.remove(employname);
	}
	
	public void setEmployRequests(Vector<String> employrequests) {
		employmentrequest = employrequests;
	}
	
	public void addEmployRequest(String employname) {
		employmentrequest.add(employname);
	}
	
	public void removeEmployRequest(String employname) {
		employmentrequest.remove(employname);
	}
	
	public void setOwnerOffers(Vector<String> ownoffers) {
		owneroffers = ownoffers;
	}
	
	public void addOwnerOffer(String ownername) {
		owneroffers.add(ownername);
	}
	
	public void removeOwnerOffer(String ownername) {
		owneroffers.remove(ownername);
	}
	
	public void setOwnerRequests(Vector<String> ownrequests) {
		ownerrequest = ownrequests;
	}
	
	public void addOwnerRequest(String ownername) {
		ownerrequest.add(ownername);
	}
	
	public void removeOwnerRequest(String ownername) {
		ownerrequest.remove(ownername);
	}
	
	public void setEmployees(Vector<String> employeestemp) {
		employees = employeestemp;
	}
	
	public void addEmployee(String employeename) {
		employees.add(employeename);
	}
	
	public void removeEmployee(String employeename) {
		employees.remove(employeename);
	}
	
	public Vector<String> getEmployOffers() {
		return employmentoffers;
	}
	
	public Vector<String> getEmployRequest() {
		return employmentrequest;
	}
	
	public Vector<String> getOwnerOffers() {
		return owneroffers;
	}
	
	public Vector<String> getOwnerRequest() {
		return ownerrequest;
	}
	
	public Vector<String> getEmployees() {
		return employees;
	}
	
	public void setName(String nametemp) {
		name = nametemp;
	}
	
	public InspiredNations getPlugin() {
		return plugin;
	}
	
	public String getCountry() {
		return country;
	}
	
	public int getTown() {
		return town;
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
	public void removeOutsideShops() {
		for (ChestShop shop: shops) {
			if (shop.getDoubleChest()) {
				if (!(isIn(shop.getSpot()[0]) && isIn(shop.getSpot()[1]) && isIn(shop.getSpot()[2]) && isIn(shop.getSpot()[3]))) {
					removeChestShop(shop);
				}
			}
			else {
				if (!(isIn(shop.getSpot()[0]) && isIn(shop.getSpot()[2])&& isIn(shop.getSpot()[3]))) {
					removeChestShop(shop);
				}
			}
		}
	}
	public boolean isInTown() {
		Town townIn = plugin.countrydata.get(country.toLowerCase()).getTowns().get(town);
		if (isPolySpace()) {
			Rectangle rect = polyspace.getPolygon().getBounds();
			for (int i = (int) rect.getMinX(); i < (int) rect.getMaxX(); i++) {
				for (int j = (int) rect.getMinY(); j < (int)rect.getMaxY(); j++) {
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
	
	public boolean isOwner(Player player) {
		if (owners.contains(player.getName())) return true;
		else return false;
	}
	
	public String getName() {
		return name;
	}
	
	public Vector<String> getOwners() {
		return owners;
	}
}
