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
package com.github.InspiredOne.InspiredNations.Country;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Vector;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.TownMethods;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;
import com.github.InspiredOne.InspiredNations.Road.Road;
import com.github.InspiredOne.InspiredNations.Town.Town;


public class Country {

	private InspiredNations plugin;
	private String name;
	private String ruler;
	private Vector<String> coRulers = new Vector<String>();
	private Vector<Town> towns = new Vector<Town>();
	private Vector<Road> roads = new Vector<Road>();
	private Vector<String> residents = new Vector<String>();
	private Vector<Park> parks = new Vector<Park>();
	private Vector<String> request = new Vector<String>();
	private Vector<String> offer = new Vector<String>();
	private int population = 0;
	private Chunks area = new Chunks();
	private String pluralMoneyName = "";
	private String singularMoneyName = "";
	private double taxRate = 1.0;
	private BigDecimal moneyMultiplyer = new BigDecimal(Math.PI);
	private BigDecimal money;
	private BigDecimal loan;
	private BigDecimal maxLoan;
	private int protectionLevel = 1;
	private int futureprotectionlevel = 1;
	private MathContext mc = new MathContext(100, RoundingMode.UP);
	private MathContext mc2 = new MathContext(100, RoundingMode.DOWN);
	
	public Country(InspiredNations instance, Chunks areatemp, String nametemp, String rulertemp) {
		plugin = instance;
		area = areatemp;
		name = nametemp;
		ruler = rulertemp;
		money = new BigDecimal(plugin.getConfig().getString("country_start_loan"));
		loan = new BigDecimal(plugin.getConfig().getString("country_start_loan"));
		maxLoan =  new BigDecimal(plugin.getConfig().getDouble("country_start_loan"));
	}
	
	public Country(InspiredNations instance, String nametemp, String rulertemp) {
		plugin = instance;
		name = nametemp;
		ruler = rulertemp;
		money = new BigDecimal(plugin.getConfig().getString("country_start_loan"));
		loan = new BigDecimal(plugin.getConfig().getString("country_start_loan"));
		new BigDecimal(plugin.getConfig().getDouble("country_start_loan"));
	}
	
	public void setName(String nametemp) {
		name = nametemp;
	}
	
	public void setRuler(String rulername) {
		ruler = rulername;
	}
	
	public void setCoRulers(Vector<String> rulernames) {
		coRulers = rulernames;
	}
	
	public void addCoRuler(Player rulertemp) {
		coRulers.add(rulertemp.getName().toLowerCase());
	}
	
	public void addCoRuler(String rulertemp) {
		coRulers.add(rulertemp.toLowerCase());
	}
	public void removeCoRuler(Player rulertemp) {
		coRulers.remove(rulertemp.getName().toLowerCase());
	}
	
	public void removeCoRuler(String rulertemp) {
		coRulers.remove(rulertemp);
	}
	
	public void setTowns(Vector<Town> townlist) {
		towns = townlist;
	}
	
	public void addTown(Town town) {
		towns.add(town);
	}
	
	public void removeTown(Town town) {
		towns.remove(town);
	}
	
	public void setParks(Vector<Park> parklist) {
		parks = parklist;
	}
	
	public void addPark(Park park) {
		parks.add(park);
	}
	
	public void removePark(Park park) {
		parks.remove(park);
	}
	
	public void setRoads(Vector<Road> roadlist) {
		roads = roadlist;
	}
	
	public void addRoad(Road road) {
		roads.add(road);
	}
	
	public void removeRoad(Road road) {
		roads.remove(road);
	}
	
	public void setResidents(Vector<String> people) {
		residents = people;
	}
	
	public void addResident(String person) {
		residents.add(person);
	}
	
	public void removeResident(String person) {
		residents.remove(person);
	}
	
	public void addRequest(String person) {
		request.add(person);
	}
	
	public void removeRequest(String person) {
		request.remove(person);
	}
	
	public void setRequest(Vector<String> list) {
		request = list;
	}
	
	public void addOffer(String person) {
		offer.add(person);
	}
	
	public void removeOffer(String person) {
		offer.remove(person);
	}
	
	public void setOffer(Vector<String> list) {
		offer = list;
	}
	
	public Vector<String> getRequests() {
		return request;
	}
	
	public Vector<String> getOffers() {
		return offer;
	}
	public void setPopulation(int number) {
		population = number;
		if (population > plugin.getConfig().getInt("min_population")) {
			maxLoan = new BigDecimal((population - plugin.getConfig().getInt("min_population")) * plugin.getConfig().getDouble("loan_per_country_resident")).add(cut(new BigDecimal(plugin.getConfig().getDouble("country_start_loan"))));
		}
		else {
			maxLoan = new BigDecimal(plugin.getConfig().getDouble("country_start_loan"));
		}
	}
	public void setArea(Chunks newarea) {
		area = newarea;
	}
	
	public void setPluralMoney(String moneyname) {
		pluralMoneyName = moneyname;
	}
	
	public void setSingularMoney(String moneyname) {
		singularMoneyName = moneyname;
	}
	
	public void setTaxRate(double tax) {
		taxRate = tax;
	}
	
	public void setMoneyMultiplyer(double multiplyertemp) {
		BigDecimal multiplyer = new BigDecimal(multiplyertemp, mc);
		moneyMultiplyer = multiplyer;
	}
	
	public void setMoneyMultiplyer(BigDecimal multiplyer) {
		moneyMultiplyer = multiplyer;
	}
	
	public void setMoney(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		money = amount.divide(moneyMultiplyer, mc);
	}
	
	public void setMoney(BigDecimal amount) {
		money = amount.divide(moneyMultiplyer, mc);
	}
	
	public void setRawMoney(BigDecimal amount) {
		money = amount;
	}
	
	public void addMoney(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		money = money.add((amount.divide(moneyMultiplyer, mc)));
	}
	
	public void addMoney(BigDecimal amount) {
		money = money.add((amount.divide(moneyMultiplyer, mc)));
	}
	
	public void removeMoney(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		money = money.subtract((amount.divide(moneyMultiplyer, mc2)));
	}
	
	public void removeMoney(BigDecimal amount) {
		money = money.subtract((amount.divide(moneyMultiplyer, mc2)));
	}
	
	public void setLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		loan = amount.divide(moneyMultiplyer, mc);
	}
	
	public void setLoan(BigDecimal amount) {
		loan = amount.divide(moneyMultiplyer, mc);
	}
	
	public void setRawLoan(BigDecimal amount) {
		loan = amount;
	}
	
	public void addLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		loan = loan.add((amount.divide(moneyMultiplyer, mc)));
	}
	
	public void addLoan(BigDecimal amount) {
		loan = loan.add((amount.divide(moneyMultiplyer, mc)));
	}
	
	public void removeLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		loan = loan.subtract((amount.divide(moneyMultiplyer, mc2)));
	}
	
	public void removeLoan(BigDecimal amount) {
		loan = loan.subtract((amount.divide(moneyMultiplyer, mc2)));
	}
	
	public void setMaxLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		maxLoan = amount.divide(moneyMultiplyer,mc);
	}
	
	public void setMaxLoan(BigDecimal amount) {
		maxLoan = amount.divide(moneyMultiplyer,mc);
	}
	
	public void setRawMaxLoan(BigDecimal amount) {
		maxLoan = amount;
	}
	
	public void setProtectionLevel(int level) {
		protectionLevel = level;
	}
	
	public void setFutureProtectionLevel(int level) {
		futureprotectionlevel = level;
	}
	
	// Getters
	public InspiredNations getPlugin() {
		return plugin;
	}
	public String getName() {
		return name;
	}
	
	public String getRuler() {
		return ruler;
	}
	
	public Vector<String> getCoRulers() {
		return coRulers;
	}
	
	public Vector<Town> getTowns() {
		return towns;
	}
	
	public Vector<Park> getParks() {
		return parks;
	}
	
	public Vector<Road> getRoad() {
		return roads;
	}
	
	public Vector<String> getResidents() {
		return residents;
	}
	
	public boolean isResident(String person) {
		if (residents.contains(person)) return true;
		else return false;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public void addPopulation(int people) {
		population = population + people;
		if (population > plugin.getConfig().getInt("min_population")) {
			maxLoan = new BigDecimal((population - plugin.getConfig().getInt("min_population")) * plugin.getConfig().getDouble("loan_per_country_resident")).add(cut(new BigDecimal(plugin.getConfig().getDouble("country_start_loan"))));
		}
		else {
			maxLoan = cut(new BigDecimal(plugin.getConfig().getDouble("country_start_loan")));
		}
	}
	
	public void removePopulation(int people) {
		population = population - people;
		if (population > plugin.getConfig().getInt("min_population")) {
			maxLoan = new BigDecimal((population - plugin.getConfig().getInt("min_population")) * plugin.getConfig().getDouble("loan_per_country_resident")).add(cut(new BigDecimal(plugin.getConfig().getDouble("country_start_loan"))));
		}
		else {
			maxLoan = cut(new BigDecimal(plugin.getConfig().getDouble("country_start_loan")));
		}
	}
	
	public Chunks getArea() {
		return area;
	}
	
	public boolean isIn(Location tile) {
		return area.isIn(tile);
	}
	
	public String getPluralMoney() {
		return pluralMoneyName;
	}
	
	public String getSingularMoney() {
		return singularMoneyName;
	}
	
	public double getTaxRate() {
		return taxRate;
	}
	
	public BigDecimal getMoneyMultiplyer() {
		return moneyMultiplyer;
	}
	
	public BigDecimal getMoney() {
		return cut(money.multiply(moneyMultiplyer, mc));
	}
	
	public BigDecimal getRawMoney() {
		return money;
	}
	
	public int size() {
		if (area == null) return 0;
		else return area.Area()/256;
	}
	
	public BigDecimal getLoanAmount() {
		return cut(loan.multiply(moneyMultiplyer, mc));
	}
	
	public BigDecimal getRawLoanAmount() {
		return loan;
	}
	
	public BigDecimal getMaxLoan() {
		return cut(maxLoan.multiply(moneyMultiplyer, mc));
	}
	
	public BigDecimal getRawMaxLoan() {
		return maxLoan;
	}
	
	public BigDecimal getRevenue() {
		BigDecimal taxRevenue = new BigDecimal(0);
		for (int i = 0; i < towns.size(); i++) {
			TownMethods TMI = new TownMethods(plugin, towns.get(i));
			taxRevenue = taxRevenue.add(TMI.getTaxAmount());
		}
		return taxRevenue;
	}
	
	public boolean hasCapital() {
		for(Iterator<Town> i = towns.iterator(); i.hasNext();) {
			Town town = i.next();
			if (town.isCapital()) return true;
		}
		return false;
	}
	
	public Town getCapital() {
		for(Iterator<Town> i = towns.iterator(); i.hasNext();) {
			Town town = i.next();
			if (town.isCapital()) return town;
		}
		return null;
	}
	
	public int getProtectionLevel() {
		return protectionLevel;
	}
	
	public int getFutureProtectionLevel() {
		return futureprotectionlevel;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
	}
}
