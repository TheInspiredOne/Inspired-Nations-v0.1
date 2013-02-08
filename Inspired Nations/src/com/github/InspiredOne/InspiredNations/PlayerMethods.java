package com.github.InspiredOne.InspiredNations;

import java.util.Vector;


import org.bukkit.entity.Player;

import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Town.Town;

public class PlayerMethods {

	InspiredNations plugin;
	Player player;
	PlayerData PDI;
	String playername;
	
	public PlayerMethods(InspiredNations instance, Player playertemp) {
		plugin = instance;
		player = playertemp;
		PDI = plugin.playerdata.get(player.getName().toLowerCase());
		playername = player.getName().toLowerCase();
	}
	
	public PlayerMethods(InspiredNations instance, String playernametemp) {
		plugin = instance;
		playername = playernametemp;
		PDI = plugin.playerdata.get(playername);
	}
	
	public double taxAmount() {
		return (houseTax()+goodBusinessTax()+serviceBusinessTax());
	}
	public double taxAmount(String town){
		return (houseTax(town) + goodBusinessTax(town) + serviceBusinessTax(town));
	}
	
	public double houseTax() {
		Vector<House> Houses = PDI.getHouseOwned();
		Country country = PDI.getCountryResides();
		double amount = 0;
		for (int i=0; i<Houses.size(); i++) {
			House house = Houses.get(i);
			amount += country.getTowns().get(house.getTown()).getHouseTax()*house.Volume()*house.getProtectionLevel()/(100.0*house.getOwners().size());
		}
		return amount;
	}
	public double houseTax(House house) {
		Country country = PDI.getCountryResides();
		double amount = 0;
		amount = country.getTowns().get(house.getTown()).getHouseTax()*house.Volume()*house.getProtectionLevel()/(100.0*house.getOwners().size());
		return amount;
	}
	public double houseTax(House house, int level) {
		Country country = PDI.getCountryResides();
		double amount = 0;
		amount = country.getTowns().get(house.getTown()).getHouseTax()*house.Volume()*level/(100.0*house.getOwners().size());
		return amount;
	}
	public double houseTax(String town) {
		Vector<House> Houses = PDI.getHouseOwned();
		Country country = PDI.getCountryResides();
		double amount = 0;
		for (int i=0; i<Houses.size(); i++) {
			House house = Houses.get(i);
			if (PDI.getCountryResides().getTowns().get(house.getTown()).getName().equalsIgnoreCase(town)){
				amount += country.getTowns().get(house.getTown()).getHouseTax()*house.Volume()*house.getProtectionLevel()/(house.getOwners().size()*100.0);
			}
		}
		return amount;
	}
	
	public double goodBusinessTax() {
		Vector<GoodBusiness> businesses = PDI.getGoodBusinessOwned();
		Country country = PDI.getCountryResides();
		double amount = 0;
		for (int i=0; i<businesses.size(); i++) {
			GoodBusiness business = businesses.get(i);
			amount += country.getTowns().get(business.getTown()).getGoodBusinessTax()*business.Volume()*business.getProtectionLevel()/(business.getOwners().size()*100.0);
		}
		return amount;
	}
	public double goodBusinessTax(GoodBusiness business) {
		Country country = PDI.getCountryResides();
		double amount = 0;
		amount = country.getTowns().get(business.getTown()).getGoodBusinessTax()*business.Volume()*business.getProtectionLevel()/(business.getOwners().size()*100.0);
		return amount;
	}
	public double goodBusinessTax(GoodBusiness business, int level) {
		Country country = PDI.getCountryResides();
		double amount = 0;
		amount = country.getTowns().get(business.getTown()).getGoodBusinessTax()*business.Volume()*level/(business.getOwners().size()*100.0);
		return amount;
	}
	public double goodBusinessTax(String town) {
		Vector<GoodBusiness> businesses = PDI.getGoodBusinessOwned();
		Country country = PDI.getCountryResides();
		double amount = 0;
		for (int i=0; i<businesses.size(); i++) {
			GoodBusiness business = businesses.get(i);
			if (PDI.getCountryResides().getTowns().get(business.getTown()).getName().equalsIgnoreCase(town)){
				amount += country.getTowns().get(business.getTown()).getGoodBusinessTax()*business.Volume()*business.getProtectionLevel()/(business.getOwners().size()*100.0);
			}
		}
		return amount;
	}
	
	public double serviceBusinessTax() {
		Vector<ServiceBusiness> businesses = PDI.getServiceBusinessOwned();
		Country country = PDI.getCountryResides();
		double amount = 0;
		for (int i=0; i<businesses.size(); i++) {
			ServiceBusiness business = businesses.get(i);
			amount += country.getTowns().get(business.getTown()).getServiceBusinessTax()*business.Volume()/(business.getOwners().size()*100.0);
		}
		return amount;
	}
	public double serviceBusinessTax(ServiceBusiness business) {
		Country country = PDI.getCountryResides();
		double amount = 0;
		amount = country.getTowns().get(business.getTown()).getGoodBusinessTax()*business.Volume()*business.getProtectionLevel()/(business.getOwners().size()*100.0);
		return amount;
	}
	public double serviceBusinessTax(ServiceBusiness business, int level) {
		Country country = PDI.getCountryResides();
		double amount = 0;
		amount = country.getTowns().get(business.getTown()).getGoodBusinessTax()*business.Volume()*level/(business.getOwners().size()*100.0);
		return amount;
	}
	public double serviceBusinessTax(String town) {
		Vector<ServiceBusiness> businesses = PDI.getServiceBusinessOwned();
		Country country = PDI.getCountryResides();
		double amount = 0;
		for (int i=0; i<businesses.size(); i++) {
			ServiceBusiness business = businesses.get(i);
			if (PDI.getCountryResides().getTowns().get(business.getTown()).getName().equalsIgnoreCase(town)){
				amount += country.getTowns().get(business.getTown()).getServiceBusinessTax()*business.Volume()/(business.getOwners().size()*100.0);
			}
		}
		return amount;
	}
	
	public int protectedVolume() {
		return houseVolume() + goodBusinessVolume() + serviceBusinessVolume();
	}
	
	public int houseVolume() {
		Vector<House> Houses = PDI.getHouseOwned();
		int HouseVolume = 0;
		for (int i = 0; i < Houses.size(); i++) {
			HouseVolume = HouseVolume + Houses.get(i).Volume();
		}
		return HouseVolume;
	}
	
	public int goodBusinessVolume() {
		Vector<GoodBusiness> GoodBusinesses = PDI.getGoodBusinessOwned();
		int GoodBusinessVolume = 0;
		for (int i = 0; i < GoodBusinesses.size(); i++) {
			GoodBusinessVolume = GoodBusinessVolume + GoodBusinesses.get(i).Volume();
		}
		return GoodBusinessVolume;
	}
	
	public int serviceBusinessVolume() {
		Vector<ServiceBusiness> ServiceBusinesses = PDI.getServiceBusinessOwned();
		int ServiceBusinessVolume = 0;
		for (int i = 0; i < ServiceBusinesses.size(); i++) {
			ServiceBusinessVolume = ServiceBusinessVolume + ServiceBusinesses.get(i).Volume();
		}
		return ServiceBusinessVolume;
	}
	
	public int protectedArea() {
		return houseArea() + goodBusinessArea() + serviceBusinessArea();
	}
	
	public int houseArea() {
		Vector<House> Houses = PDI.getHouseOwned();
		int HouseArea = 0;
		for (int i = 0; i < Houses.size(); i++) {
			HouseArea = HouseArea + Houses.get(i).Area();
		}
		return HouseArea;
	}
	
	public int goodBusinessArea() {
		Vector<GoodBusiness> GoodBusinesses = PDI.getGoodBusinessOwned();
		int GoodBusinessArea = 0;
		for (int i = 0; i < GoodBusinesses.size(); i++) {
			GoodBusinessArea = GoodBusinessArea + GoodBusinesses.get(i).Area();
		}
		return GoodBusinessArea;
	}
	
	public int serviceBusinessArea() {
		Vector<ServiceBusiness> ServiceBusinesses = PDI.getServiceBusinessOwned();
		int ServiceBusinessArea = 0;
		for (int i = 0; i < ServiceBusinesses.size(); i++) {
			ServiceBusinessArea = ServiceBusinessArea + ServiceBusinesses.get(i).Area();
		}
		return ServiceBusinessArea;
	}
	
	public boolean leaveCountry() {
		if (PDI.getIsCountryRuler() || PDI.getIsTownMayor() || !PDI.getIsCountryResident()) return false;
		Country countryFrom = PDI.getCountryResides();
		PDI.setIsCountryResident(false);
		PDI.setCountryResides(null);
		if (PDI.getIsTownResident()) {
			leaveTown();
		}
		countryFrom.removeResident(playername);
		countryFrom.removePopulation(1);
		if (countryFrom.getCoRulers().contains(playername)) {
			countryFrom.removeCoRuler(playername);
			PDI.setIsCountryRuler(false);
		}
		return true;
	}
	
	public boolean leaveTown() {
		if (PDI.getIsTownMayor() || !PDI.getIsTownResident()) return false;
		Town townFrom = PDI.getTownResides();
		PDI.setIsTownResident(false);
		PDI.setTownResides(null);
		if (PDI.getIsHouseOwner()) {
			for (int i = 0; i < PDI.getHouseOwned().size(); i++) {
				if (PDI.getHouseOwned().get(i).getOwners().size() == 1) {
					townFrom.removeHouse(PDI.getHouseOwned().get(i));
				}
				else {
					PDI.getHouseOwned().get(i).removeOwner(player);
				}

			}
			PDI.setHousesOwned(new Vector<House>());
			PDI.setIsHouseOwner(false);
		}
		if (PDI.getIsGoodBusinessOwner()) {
			for (int i = 0; i < PDI.getGoodBusinessOwned().size(); i++) {
				if (PDI.getGoodBusinessOwned().get(i).getOwners().size() == 1) {
					townFrom.removeGoodBusiness(PDI.getGoodBusinessOwned().get(i));
				}
				else {
					PDI.getGoodBusinessOwned().get(i).removeOwner(player);
				}

			}
			PDI.setGoodBusinessOwned(new Vector<GoodBusiness>());
			PDI.setIsGoodBusinessOwner(false);
		}
		if (PDI.getIsServiceBusinessOwner()) {
			for (int i = 0; i < PDI.getServiceBusinessOwned().size(); i++) {
				if (PDI.getServiceBusinessOwned().get(i).getOwners().size() == 1) {
					townFrom.removeServiceBusiness(PDI.getServiceBusinessOwned().get(i));
				}
				else {
					PDI.getServiceBusinessOwned().get(i).removeOwner(player);
				}
			}
			PDI.setServiceBusinessOwned(new Vector<ServiceBusiness>());
			PDI.setIsServiceBusinessOwner(false);
		}
		townFrom.removeResident(playername);
		return true;
	}
	
	public boolean joinTown(Town townTo) {
		if (PDI.getIsTownResident() || !PDI.getIsCountryResident()) return false;
		PDI.setIsTownResident(true);
		PDI.setTownResides(townTo);
		PDI.setHouseTax(townTo.getHouseTax());
		PDI.setGoodBusinessTax(townTo.getGoodBusinessTax());
		PDI.setServiceBusinessTax(townTo.getServiceBusinessTax());
		townTo.addResident(playername);
		return true;
	}
	
	public boolean joinCountry(Country countryTo) {
		if (PDI.getIsCountryResident() || PDI.getIsCountryRuler() || PDI.getIsTownMayor()) return false;
		PDI.setIsCountryResident(true);
		PDI.setCountryResides(countryTo);
		PDI.setPluralMoney(countryTo.getPluralMoney());
		PDI.setSingularMoney(countryTo.getSingularMoney());
		PDI.setMoneyMultiplyer(countryTo.getMoneyMultiplyer());
		PDI.setIsTownMayor(false);
		PDI.setIsTownResident(false);
		countryTo.addPopulation(1);
		countryTo.addResident(playername);
		return true;
	}
	
	public boolean transferTown(Town townTo) {
		leaveTown();
		return joinTown(townTo);
	}
	
	public boolean transferCountry(Country countryTo) {
		leaveCountry();
		return joinCountry(countryTo);
	}
}