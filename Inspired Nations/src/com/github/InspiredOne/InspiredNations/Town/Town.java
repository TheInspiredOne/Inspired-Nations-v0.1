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
package com.github.InspiredOne.InspiredNations.Town;

import java.awt.Point;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Vector;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.InspiredNations;
import com.github.InspiredOne.InspiredNations.PlayerMethods;
import com.github.InspiredOne.InspiredNations.Bank.Local.LocalBank;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Hall.Local.LocalHall;
import com.github.InspiredOne.InspiredNations.Hospital.Hospital;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Prison.Local.LocalPrison;
import com.github.InspiredOne.InspiredNations.Regions.Chunks;
import com.github.InspiredOne.InspiredNations.Regions.polygonPrism;



public class Town {

	private String country;
	private InspiredNations plugin;
	private String name = "";
	private String mayor = "";
	private Vector<String> coMayors = new Vector<String>();
	private Vector<House> houses = new Vector<House>();
	private Vector<GoodBusiness> goodBusinesses = new Vector<GoodBusiness>();
	private Vector<ServiceBusiness> serviceBusinesses = new Vector<ServiceBusiness>();
	private Vector<Park> parks = new Vector<Park>();
	private Vector<String> residents = new Vector<String>();
	private Vector<String> request = new Vector<String>();
	private Vector<String> offer = new Vector<String>();
	private Hospital hospital = null;
	private LocalHall townHall = null;
	private LocalPrison prison = null;
	private LocalBank bank = null;
	private Chunks area2 = new Chunks();
	private double nationTax = 1;
	private double houseTax = 1;
	private double goodBusinessTax = 1;
	private double serviceBusinessTax = 1;
	private String pluralMoneyName = "";
	private String singularMoneyName = "";
	private BigDecimal money = new BigDecimal(0);
	private BigDecimal moneyMultiplyer = new BigDecimal(1);
	private BigDecimal loan = new BigDecimal(0);
	private BigDecimal maxLoan;
	private boolean isCapital = false;
	private int protectionLevel = 1;
	private int futureprotectionlevel = 1;
	private MathContext mc = new MathContext(100, RoundingMode.UP);
	private MathContext mc2 = new MathContext(100, RoundingMode.DOWN);
	
	public Town(InspiredNations instance, Chunks areatemp, String nametemp, String mayortemp, String countrytemp) {
		plugin = instance;
		name = nametemp;
		mayor = mayortemp;
		maxLoan =  new BigDecimal(plugin.getConfig().getDouble("town_start_loan"));
		townHall = null;
		country = countrytemp;
		area2 = areatemp;
	}
	
	public Town(InspiredNations instance, String nametemp, String mayortemp, String countrytemp) {
		plugin = instance;
		name = nametemp;
		mayor = mayortemp;
		maxLoan =  new BigDecimal(plugin.getConfig().getDouble("town_start_loan"));
		townHall = null;
		country = countrytemp;
	}
	
	public void setCountry(String countryname) {
		country = countryname;
	}
	
	public void setName(String nametemp) {
		name = nametemp;
	}
	
	public void setMayor(String mayortemp) {
		mayor = mayortemp;
	}
	
	public void setCoMayors(Vector<String> coMayorsTemp) {
		coMayors = coMayorsTemp;
	}
	
	public void addCoMayor(Player player) {
		coMayors.add(player.getName().toLowerCase());
	}
	
	public void addCoMayor(String playername) {
		coMayors.add(playername);
	}
	
	public void removeCoMayor(Player player) {
		coMayors.remove(player.getName().toLowerCase());
	}
	
	public void removeCoMayor(String playername) {
		coMayors.remove(playername);
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
	
	public void setArea(Chunks newarea) {
		area2 = newarea;
	}
	
	public void setHouses(Vector<House> housestemp) {
		houses = housestemp;
	}
	
	public void addHouse(House house) {
		houses.add(house);
	}
	
	public void removeHouse(House house) {
		houses.remove(house);
	}
	
	public void setGoodBusinesses(Vector<GoodBusiness> business) {
		goodBusinesses = business;
	}
	
	public void addGoodBusiness(GoodBusiness business) {
		goodBusinesses.add(business);
	}
	
	public void removeGoodBusiness(GoodBusiness business) {
		goodBusinesses.remove(business);
	}
	
	public void setServiceBusinesses(Vector<ServiceBusiness> business) {
		serviceBusinesses = business;
	}
	
	public void addServiceBusiness(ServiceBusiness business) {
		serviceBusinesses.add(business);
	}
	
	public void removeServiceBusiness(ServiceBusiness business) {
		serviceBusinesses.remove(business);
	}
	
	public void setParks(Vector<Park> park) {
		parks = park;
	}
	
	public void addPark(Park park) {
		parks.add(park);
	}
	
	public void removePark(Park park) {
		parks.remove(park);
	}
	
	public void setResidents(Vector<String> people) {
		residents = people;
		if (residents.size() > plugin.getConfig().getInt("min_comayors")) {
			maxLoan = new BigDecimal((residents.size() - plugin.getConfig().getInt("min_comayors")) * plugin.getConfig().getDouble("Loan_per_town_resident")).add(cut(new BigDecimal(plugin.getConfig().getDouble("town_start_loan"))));
		}
		else {
			maxLoan = cut(new BigDecimal(plugin.getConfig().getDouble("town_start_loan")));
		}
	}
	
	public void addResident(String person) {
		residents.add(person);
		if (residents.size() > plugin.getConfig().getInt("min_comayors")) {
			maxLoan = new BigDecimal((residents.size() - plugin.getConfig().getInt("min_comayors")) * plugin.getConfig().getDouble("Loan_per_town_resident")).add(cut(new BigDecimal(plugin.getConfig().getDouble("town_start_loan"))));
		}
		else {
			maxLoan = cut(new BigDecimal(plugin.getConfig().getDouble("town_start_loan")));
		}
	}
	
	public void removeResident(String person) {
		residents.remove(person);
		if (residents.size() > plugin.getConfig().getInt("min_comayors")) {
			maxLoan = new BigDecimal((residents.size() - plugin.getConfig().getInt("min_comayors")) * plugin.getConfig().getDouble("Loan_per_town_resident")).add(cut(new BigDecimal(plugin.getConfig().getDouble("town_start_loan"))));
		}
		else {
			maxLoan = cut(new BigDecimal(plugin.getConfig().getDouble("town_start_loan")));
		}
	}
	
	public void setHospital(Hospital building) {
		hospital = building;
	}
	
	public void setTownHall(LocalHall building) {
		townHall = building;
	}
	
	public void setPrison(LocalPrison building) {
		prison = building;
	}
	
	public void setBank(LocalBank building) {
		bank = building;
	}
	
	public void setNationTax(double tax) {
		nationTax = tax;
	}
	
	public void setHouseTax(double tax) {
		houseTax = tax;
	}
	
	public void setGoodBusinessTax(double tax) {
		goodBusinessTax = tax;
	}
	
	public void setServiceBusinessTax(double tax) {
		serviceBusinessTax = tax;
	}
	public void setIsCapital(boolean isTheCapital) {
		isCapital = isTheCapital;
	}
	
	// Economy Setters
	public void setPluralMoney(String name) {
		pluralMoneyName = name;
	}
	
	public void setSingularMoney(String name) {
		singularMoneyName = name;
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
	
	public void setMoneyMultiplyer(double multiplyertemp) {
		BigDecimal multiplyer = new BigDecimal(multiplyertemp);
		moneyMultiplyer = multiplyer;
	}
	
	public void setMoneyMultiplyer(BigDecimal multiplyer) {
		moneyMultiplyer = multiplyer;
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
		maxLoan = amount.divide(moneyMultiplyer, mc);
	}
	
	public void setMaxLoan(BigDecimal amount) {
		maxLoan = amount.divide(moneyMultiplyer, mc);
	}
	
	public void setRawMaxLoan(BigDecimal amount) {
		maxLoan = amount;
	}
	
	public void setProtectionLevel(int protection) {
		protectionLevel = protection;
	}
	
	public void setFutureProtectionLevel(int protection) {
		futureprotectionlevel = protection;
	}
	
	// Getters
	public InspiredNations getPlugin() {
		return plugin;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMayor() {
		return mayor;
	}
	
	public Vector<String> getCoMayors() {
		return coMayors;
	}
	
	public Vector<House> getHouses() {
		return houses;
	}
	
	public Vector<GoodBusiness> getGoodBusinesses() {
		return goodBusinesses;
	}
	
	public Vector<ServiceBusiness> getServiceBusinesses() {
		return serviceBusinesses;
	}
	
	public Vector<Park> getParks() {
		return parks;
	}
	
	public Vector<String> getResidents() {
		return residents;
	}
	
	public boolean isResident(Player person) {
		if (residents.contains(person.getName().toLowerCase())) return true;
		else return false;
	}
	
	public boolean isResident(String person) {
		if (residents.contains(person.toLowerCase())) return true;
		else return false;
	}
	
	public int population() {
		return residents.size();
	}
	
	public Hospital getHospital() {
		return hospital;
	}
	
	public LocalHall getTownHall() {
		return townHall;
	}
	
	public LocalPrison getPrison() {
		return prison;
	}
	
	public LocalBank getBank() {
		return bank;
	}
	
	public Chunks getRegion() {
		return area2;
	}
	
	public boolean isIn(Location tile) {
		return area2.isIn(tile);
	}
	
	public boolean isIn(Point spot, String world) {
		return area2.isIn(spot, world);
	}
	
	public int getArea() {
		return area2.Area();
	}
	
	public int getVolume() {
		return area2.Volume();
	}
	
	public double getNationTax() {
		return nationTax;
	}
	
	public double getHouseTax() {
		return houseTax;
	}
	
	public double getGoodBusinessTax() {
		return goodBusinessTax;
	}
	
	public double getServiceBusinessTax() {
		return serviceBusinessTax;
	}
	
	public String getPluralMoney() {
		return pluralMoneyName;
	}
	
	public String getSingularMoney() {
		return singularMoneyName;
	}
	
	public BigDecimal getMoney() {
		return cut(money.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawMoney() {
		return money;
	}
	
	public BigDecimal getMoneyMultiplyer() {
		return moneyMultiplyer;
	}
	
	public BigDecimal getLoan() {
		return cut(loan.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawLoan() {
		return loan;
	}
	
	public BigDecimal getMaxLoan() {
		return cut(maxLoan.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawMaxLoan() {
		return maxLoan;
	}
	
	public BigDecimal getRevenue() {
		BigDecimal taxRevenue = BigDecimal.ZERO;
		for (String playername:plugin.countrydata.get(country.toLowerCase()).getResidents()) {
			PlayerMethods PMI = new PlayerMethods(plugin, playername);
			taxRevenue = taxRevenue.add(PMI.taxAmount(name));
		}
		return taxRevenue;
	}
	
	public int getProtectionLevel() {
		return protectionLevel;
	}
	
	public int getFutureProtectionLevel() {
		return futureprotectionlevel;
	}
	
	public boolean isCapital() {
		return isCapital;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public double cut(double x) {
		int y;
		y = (int) Math.round((x*100));
		return y/100.0;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
	}
	
	public boolean hasTownHall() {
		try {
			if (!townHall.equals(null)) return true;
			else return false;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public boolean hasPrison() {
		try {
			if (!prison.equals(null)) return true;
			else return false;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public boolean hasHospital() {
		try {
			if (!hospital.equals(null)) return true;
			else return false;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public boolean hasBank() {
		try {
			if (!bank.equals(null)) return true;
			else return false;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public void removeClaimedRegions() {
		prison = null;
		bank = null;
		townHall = null;
		hospital = null;
		area2 = new Chunks();
		for (House house: houses) {
			for (String owner: house.getOwners()) {
				plugin.playerdata.get(owner.toLowerCase()).removeHousesOwned(house);
			}
			this.removeHouse(house);
		}
		for (GoodBusiness business: goodBusinesses) {
			for (String owner: business.getOwners()) {
				plugin.playerdata.get(owner.toLowerCase()).removeGoodBusinessOwned(business);
			}
			this.removeGoodBusiness(business);
		}
		for (ServiceBusiness business: serviceBusinesses) {
			for (String owner: business.getOwners()) {
				plugin.playerdata.get(owner.toLowerCase()).removeServiceBusinessOwned(business);
			}
			this.removeServiceBusiness(business);
		}
		for (Park park: parks) {
			this.removePark(park);
		}
	}
	
	public void removeCutOutRegions() {
		// Check claimed regions to see if anything got cut out.
		Town town = this;
		if (town.hasTownHall()) {
			if (!town.getTownHall().isInTown()) {
				town.setTownHall(null);
			}
		}
		if (town.hasBank()) {
			if (!town.getBank().isInTown()) {
				town.setBank(null);
			}
		}
		if (town.hasHospital()) {
			if (!town.getHospital().isInTown()) {
				town.setHospital(null);
			}
		}
		if (town.hasPrison()) {
			if (!town.getPrison().isInTown()) {
				town.setPrison(null);
			}
		}
		for (House house: town.getHouses()) {
			if (!house.isInTown()) {
				for (String owner: house.getOwners()) {
					plugin.playerdata.get(owner.toLowerCase()).removeHousesOwned(house);
				}
				town.removeHouse(house);
			}
		}
		for (GoodBusiness business: town.getGoodBusinesses()) {
			if (!business.isInTown()) {
				for (String owner: business.getOwners()) {
					plugin.playerdata.get(owner.toLowerCase()).removeGoodBusinessOwned(business);
				}
				town.removeGoodBusiness(business);
			}
		}
		for (ServiceBusiness business: town.getServiceBusinesses()) {
			if (!business.isInTown()) {
				for (String owner: business.getOwners()) {
					plugin.playerdata.get(owner.toLowerCase()).removeServiceBusinessOwned(business);
				}
				town.removeServiceBusiness(business);
			}
		}
		for (Park park: town.getParks()) {
			if (!park.isInTown()) {
				town.removePark(park);
			}
		}
	}
}
