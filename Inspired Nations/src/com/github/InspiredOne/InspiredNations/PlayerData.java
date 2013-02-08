package com.github.InspiredOne.InspiredNations;

import java.math.BigDecimal;
import java.util.Vector;

import org.bukkit.conversations.Conversation;

import com.github.InspiredOne.InspiredNations.Bank.Federal.FederalBank;
import com.github.InspiredOne.InspiredNations.Bank.Local.LocalBank;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Hall.Federal.FederalHall;
import com.github.InspiredOne.InspiredNations.Hall.Local.LocalHall;
import com.github.InspiredOne.InspiredNations.Hospital.Hospital;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Prison.Federal.FederalPrison;
import com.github.InspiredOne.InspiredNations.Prison.Local.LocalPrison;
import com.github.InspiredOne.InspiredNations.Road.Road;
import com.github.InspiredOne.InspiredNations.Town.Town;



public class PlayerData {
	
	// Initializing Variables
	// Locations Variables
	private InspiredNations plugin;
	private boolean onRoad = false;
	private Road roadOn = null;
	private boolean inHouse = false;
	private House houseIn = null;
	private boolean inTown = false;
	private Town townIn = null;
	private boolean inCapital = false;
	private boolean inCountry = false;
	private Country countryIn = null;
	private boolean inFederalPrison = false;
	private FederalPrison federalPrisonIn = null;
	private boolean inLocalPrison = false;
	private LocalPrison localPrisonIn = null;
	private boolean inFederalBank = false;
	private FederalBank federalBankIn = null;
	private boolean inLocalBank = false;
	private LocalBank localBankIn = null;
	private boolean inGoodBusiness = false;
	private GoodBusiness goodBusinessIn = null;
	private boolean inServiceBusiness = false;
	private ServiceBusiness serviceBusinessIn = null;
	private boolean inFederalHall = false;
	private FederalHall federalHallIn = null;
	private boolean inLocalHall = false;
	private LocalHall localHallIn = null;
	private boolean inHospital = false;
	private Hospital hospitalIn = null;
	private boolean inLocalPark = false;
	private Park localParkIn = null;
	private boolean inFederalPark = false;
	private Park federalParkIn = null;

	// Portfolio Variables
	private boolean isHouseOwner = false;
	private Vector<House> houseOwned = new Vector<House>();
	private boolean isTownMayor = false;
	private Town townMayor = null;
	private boolean isCountryRuler = false;
	private Country countryRuler = null;
	private boolean isGoodBusinessOwner = false;
	private Vector<GoodBusiness> goodBusinessOwned = new Vector<GoodBusiness>();
	private boolean isServiceBusinessOwner = false;
	private Vector<ServiceBusiness> serviceBusinessOwned = new Vector<ServiceBusiness>();
	private boolean isFederalPrisonJailed = false;
	private FederalPrison federalPrisonJailed = null;
	private boolean isLocalPrisonJailed = false;
	private LocalPrison localPrisonJailed = null;
	private boolean isHospitalized = false;
	private Hospital hospitalHospitalized = null;
	private boolean isTownResident = false;
	private Town townResides = null;
	private boolean isCountryResident = false;
	private Country countryResides = null;
	
	// Economy Variables
	private BigDecimal money = new BigDecimal(500);
	private BigDecimal moneyInBank = new BigDecimal(300);
	private BigDecimal moneyMultiplyer = new BigDecimal(1);
	private String pluralMoneyName = "coins";
	private String singularMoneyName = "coin";
	private double houseTax =0;
	private double goodBusinessTax = 0;
	private double serviceBusinessTax = 0;
	private BigDecimal loanAmount = new BigDecimal(0);
	private BigDecimal maxLoan = new BigDecimal(5000);
	
	private Conversation convo = null;

	// Location Setters
	public PlayerData(InspiredNations instance) {
		plugin = instance;
//		money = 500.0;
	//	moneyInBank = 100;
	}
	
	public void setOnRoad(boolean isOnRoad) {
		onRoad = isOnRoad;
	}
	
	public void setRoadOn(Road theRoadOn) {
		roadOn = theRoadOn;
	}
	
	public void setInHouse(boolean isInHouse) {
		inHouse = isInHouse;
	}
	
	public void setHouseIn(House theHouseIn) {
		houseIn = theHouseIn;
	}
	
	public void setInTown(boolean isInTown) {
		inTown = isInTown;
	}
	
	public void setTownIn(Town theTownIn) {
		townIn = theTownIn;
	}
	
	public void setInCapital(boolean isInCapital) {
		inCapital = isInCapital;
	}
	
	public void setInCountry(boolean isInCountry) {
		inCountry = isInCountry;
	}
	
	public void setCountryIn(Country theCountryIn) {
		countryIn = theCountryIn;
	}
	
	public void setInFederalPrison(boolean isInFederalPrison) {
		inFederalPrison = isInFederalPrison;
	}
	
	public void setFederalPrisonIn(FederalPrison theFederalPrisonIn) {
		federalPrisonIn = theFederalPrisonIn;
	}
	
	public void setInLocalPrison(boolean isInLocalPrison) {
		inLocalPrison = isInLocalPrison;
	}
	
	public void setLocalPrisonIn(LocalPrison theLocalPrisonIn) {
		localPrisonIn = theLocalPrisonIn;
	}
	
	public void setInFederalBank(boolean isInFederalBank) {
		inFederalBank = isInFederalBank;
	}
	
	public void setFederalBankIn(FederalBank theFederalBankIn) {
		federalBankIn = theFederalBankIn;
	}
	
	public void setInLocalBank(boolean isInLocalBank) {
		inLocalBank = isInLocalBank;
	}
	
	public void setLocalBankIn(LocalBank theLocalBankIn) {
		localBankIn = theLocalBankIn;
	}
	
	public void setInGoodBusiness(boolean isInGoodBusiness) {
		inGoodBusiness = isInGoodBusiness;
	}
	
	public void setGoodBusinessIn(GoodBusiness theGoodBusinessIn) {
		goodBusinessIn = theGoodBusinessIn;
	}
	
	public void setInServiceBusiness(boolean isInServiceBusiness) {
		inServiceBusiness = isInServiceBusiness;
	}
	
	public void setInFederalHall(boolean isInFederalHall) {
		inFederalHall = isInFederalHall;
	}
	
	public void setFederalHallIn(FederalHall theFederalHallIn) {
		federalHallIn = theFederalHallIn;
	}
	
	public void setInLocalHall(boolean isInLocalHall) {
		inLocalHall = isInLocalHall;
	}
	
	public void setLocalHallIn(LocalHall theLocalHallIn) {
		localHallIn = theLocalHallIn;
	}
	
	public void setInHospital(boolean isInHospital) {
		inHospital = isInHospital;
	}
	
	public void setHospitalIn(Hospital theHospitalIn) {
		hospitalIn = theHospitalIn;
	}
	
	public void setInLocalPark(boolean isInPark) {
		inLocalPark = isInPark;
	}
	
	public void setLocalParkIn(Park theParkIn) {
		localParkIn = theParkIn;
	}
	
	public void setInFederalPark(boolean isInFederalPark) {
		inFederalPark = isInFederalPark;
	}
	
	public void setFederalParkIn(Park theParkIn) {
		federalParkIn = theParkIn;
	}
	
	// Portfolio Setters
	public void setIsHouseOwner(boolean setIsHouseOwner) {
		isHouseOwner = setIsHouseOwner;
	}
	
	public void setHousesOwned(Vector<House> housesOwned) {
		houseOwned = housesOwned;
	}
	
	public void addHousesOwned(House house) {
		houseOwned.add(house);
	}
	
	public void removeHousesOwned(House house) {
		houseOwned.remove(house);
	}
	
	public void setIsTownMayor(boolean setIsTownMayor) {
		isTownMayor = setIsTownMayor;
	}
	
	public void setTownMayored(Town townMayored) {
		townMayor = townMayored;
	}
	
	public void setIsCountryRuler(boolean setIsCountryRuler) {
		isCountryRuler = setIsCountryRuler;
	}
	
	public void setCountryRuler(Country countryRuled) {
		countryRuler = countryRuled;
	}
	
	public void setIsGoodBusinessOwner(boolean setIsGoodBusinessOwner) {
		isGoodBusinessOwner = setIsGoodBusinessOwner;
	}
	
	public void setGoodBusinessOwned(Vector<GoodBusiness> goodBusinessesOwned) {
		goodBusinessOwned = goodBusinessesOwned;
	}
	
	public void addGoodBusinessOwned(GoodBusiness business) {
		goodBusinessOwned.add(business);
	}
	
	public void removeGoodBusinessOwned(GoodBusiness business) {
		goodBusinessOwned.remove(business);
	}
	
	public void setIsServiceBusinessOwner(boolean setIsServiceBusinessOwner) {
		isServiceBusinessOwner = setIsServiceBusinessOwner;
	}
	
	public void setServiceBusinessOwned(Vector<ServiceBusiness> serviceBusinessesOwned) {
		serviceBusinessOwned = serviceBusinessesOwned;
	}
	
	public void addServiceBusinessOwned(ServiceBusiness business) {
		serviceBusinessOwned.add(business);
	}
	
	public void removeServiceBusinessOwned(ServiceBusiness business) {
		serviceBusinessOwned.remove(business);
	}
	
	public void setServiceBusinessIn(ServiceBusiness theServiceBusinessIn) {
		serviceBusinessIn = theServiceBusinessIn;
	}
	
	public void setIsFederalPrisonJailed(boolean setIsFederalPrisonJailed) {
		isFederalPrisonJailed = setIsFederalPrisonJailed;
	}
	
	public void setFederalPrisonJailed(FederalPrison theFederalPrisonJailed) {
		federalPrisonJailed = theFederalPrisonJailed;
	}
	
	public void setIsLocalPrisonJailed(boolean setIsLocalPrisonJailed) {
		isLocalPrisonJailed = setIsLocalPrisonJailed;
	}
	
	public void setLocalPrisonJailed(LocalPrison theLocalPrisonJailed) {
		localPrisonJailed = theLocalPrisonJailed;
	}
	
	public void setIsHospitalized(boolean setIsHospitalized) {
		isHospitalized = setIsHospitalized;
	}
	
	public void setHospitalHospitalized(Hospital theHospitalHospitalized) {
		hospitalHospitalized = theHospitalHospitalized;
	}
	
	public void setIsTownResident(boolean setIsTownResident) {
		isTownResident = setIsTownResident;
	}
	
	public void setTownResides(Town theTownResides) {
		townResides = theTownResides;
	}
	
	public void setIsCountryResident(boolean setIsCountryResident) {
		isCountryResident = setIsCountryResident;
	}
	
	public void setCountryResides(Country theCountryResides) {
		countryResides = theCountryResides;
	}
	
	// Economy Setters
	public void setMoney(double onHandtemp) {
		BigDecimal onHand = new BigDecimal(onHandtemp);
		money = onHand.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setMoney(BigDecimal onHand) {
		money = onHand.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setRawMoney(BigDecimal onHand) {
		money = onHand;
	}
	
	public void removeMoney(double taketemp) {
		BigDecimal take = new BigDecimal(taketemp);
		money = money.subtract(take.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN));
	}
	
	public void removeMoney(BigDecimal take) {
		money = money.subtract(take.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN));
	}
	
	public void addMoney(double givetemp) {
		BigDecimal give = new BigDecimal(givetemp);
		money = money.add(give.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN));
	}
	
	public void addMoney(BigDecimal give) {
		money = money.add(give.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN));
	}
	
	public void transferMoney(double amounttemp, String targetname) {
		BigDecimal amount = new BigDecimal(amounttemp);
		money = money.subtract((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
		PlayerData targetPDI = plugin.playerdata.get(targetname.toLowerCase());
		targetPDI.addMoney(amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN).multiply(targetPDI.getMoneyMultiplyer()));
	}
	
	public void transferMoney(BigDecimal amount, String targetname) {
		money = money.subtract((amount.divide(moneyMultiplyer)));
		PlayerData targetPDI = plugin.playerdata.get(targetname.toLowerCase());
		targetPDI.addMoney(amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN).multiply(targetPDI.getMoneyMultiplyer()));
	}
	
	public void setMoneyInBank(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		moneyInBank = amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setMoneyInBank(BigDecimal amount) {
		moneyInBank = amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setRawMoneyInBank(BigDecimal amount) {
		moneyInBank = amount;
	}
	
	public void addMoneyInBank(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		moneyInBank = moneyInBank.add((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void addMoneyInBank(BigDecimal amount) {
		moneyInBank = moneyInBank.add((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void transferMoneyToBank(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		moneyInBank = moneyInBank.add((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void transferMoneyToBank(BigDecimal amount) {
		moneyInBank = moneyInBank.add((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void setMoneyMultiplyer(double multiplyer) {
		BigDecimal multiplyertemp = new BigDecimal(multiplyer);
		moneyMultiplyer = multiplyertemp;
	}
	
	public void setMoneyMultiplyer(BigDecimal multiplyer) {
		moneyMultiplyer = multiplyer;
	}
	
	public void setPluralMoney(String plural) {
		pluralMoneyName = plural;
	}
	
	public void setSingularMoney(String single) {
		singularMoneyName = single;
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
	
	public void setLoanAmount(double amount) {
		BigDecimal amounttemp = new BigDecimal(amount);
		loanAmount = amounttemp.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setLoanAmount(BigDecimal amount) {
		loanAmount = amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setRawLoanAmount(BigDecimal amount){
		loanAmount = amount;
	}
	
	public void addLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		loanAmount = loanAmount.add((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void addLoan(BigDecimal amount) {
		loanAmount = loanAmount.add((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void removeLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		loanAmount = loanAmount.subtract((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void removeLoan(BigDecimal amount) {
		loanAmount = loanAmount.subtract((amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN)));
	}
	
	public void setMaxLoan(double amounttemp) {
		BigDecimal amount = new BigDecimal(amounttemp);
		maxLoan = amount;
	}
	
	public void setMaxLoan(BigDecimal amount) {
		maxLoan = amount.divide(moneyMultiplyer, 25, BigDecimal.ROUND_DOWN);
	}
	
	public void setRawMaxLoan(BigDecimal amount) {
		maxLoan = amount;
	}
	
	public void setConversation(Conversation con) {
		convo = con;
	}
	
	// Location getters
	public InspiredNations getPlugin() {
		return plugin;
	}
	
	public boolean getIsOnRoad() {
		return onRoad;
	}
	
	public Road getRoadOn() {
		return roadOn;
	}
	
	public boolean getIsInHouse() {
		return inHouse;
	}
	
	public House getHouseIn() {
		return houseIn;
	}
	
	public boolean getIsInTown() {
		return inTown;
	}
	
	public Town getTownIn() {
		return townIn;
	}
	
	public boolean getIsInCapital() {
		return inCapital;
	}
	
	public boolean getIsInCountry() {
		return inCountry;
	}
	
	public Country getCountryIn() {
		return countryIn;
	}
	
	public boolean getIsInFederalPrison() {
		return inFederalPrison;
	}
	
	public FederalPrison getFederalPrisonIn() {
		return federalPrisonIn;
	}
	
	public boolean getIsInLocalPrison() {
		return inLocalPrison;
	}
	
	public LocalPrison getLocalPrisonIn() {
		return localPrisonIn;
	}
	
	public boolean getIsInFederalBank() {
		return inFederalBank;
	}
	
	public FederalBank getFederalBankIn() {
		return federalBankIn;
	}
	
	public boolean getIsInLocalBank() {
		return inLocalBank;
	}
	
	public LocalBank getLocalBankIn() {
		return localBankIn;
	}
	
	public boolean getIsInGoodBusiness() {
		return inGoodBusiness;
	}
	
	public GoodBusiness getGoodBusinessIn() {
		return goodBusinessIn;
	}
	
	public boolean getIsInServiceBusiness() {
		return inServiceBusiness;
		}
	
	public ServiceBusiness getServiceBusinessIn() {
		return serviceBusinessIn;
	}
	
	public boolean getIsInFederalHall() {
		return inFederalHall;
	}
	
	public FederalHall getFederalHallIn() {
		return federalHallIn;
	}
	
	public boolean getIsInLocalHall() {
		return inLocalHall;
	}
	
	public LocalHall getLocalHallIn() {
		return localHallIn;
	}
	
	public boolean getIsInHospital() {
		return inHospital;
	}
	
	public Hospital getHospitalIn() {
		return hospitalIn;
	}
	
	public boolean getIsInLocalPark() {
		return inLocalPark;
	}
	
	public Park getLocalParkIn() {
		return localParkIn;
	}
	
	public boolean getIsInFederalPark() {
		return inFederalPark;
	}
	
	public Park getFederalParkIn() {
		return federalParkIn;
	}
	
	// Portfolio Getters
	public boolean getIsHouseOwner() {
		return isHouseOwner;
	}
	
	public Vector<House> getHouseOwned() {
		return houseOwned;
	}
	
	public boolean getIsTownMayor() {
		return isTownMayor;
	}
	
	public Town getTownMayored() {
		return townMayor;
	}
	
	public boolean getIsCountryRuler() {
		return isCountryRuler;
	}
	
	public Country getCountryRuled() {
		return countryRuler;
	}
	
	public boolean getIsGoodBusinessOwner() {
		return isGoodBusinessOwner;
	}
	
	public Vector<GoodBusiness> getGoodBusinessOwned() {
		return goodBusinessOwned;
	}
	
	public boolean getIsServiceBusinessOwner() {
		return isServiceBusinessOwner;
	}
	
	public Vector<ServiceBusiness> getServiceBusinessOwned() {
		return serviceBusinessOwned;
	}
	
	public boolean getIsFederalPrisonJailed() {
		return isFederalPrisonJailed;
	}
	
	public FederalPrison getFederalPrisonJailed() {
		return federalPrisonJailed;
	}
	
	public boolean getIsLocalPrisonJailed() {
		return isLocalPrisonJailed;
	}
	
	public LocalPrison getLocalPrisonJailed() {
		return localPrisonJailed;
	}
	
	public boolean getIsHospitalized() {
		return isHospitalized;
	}
	
	public Hospital getHospitalHospitalized() {
		return hospitalHospitalized;
	}
	
	public boolean getIsTownResident() {
		return isTownResident;
	}
	
	public Town getTownResides() {
		return townResides;
	}
	
	public boolean getIsCountryResident() {
		return isCountryResident;
	}
	
	public Country getCountryResides() {
		return countryResides;
	}
	
	// Economy Getters
	public BigDecimal getMoney() {
		return cut(money.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawMoney() {
		return money;
	}
	
	public BigDecimal getMoneyInBank() {
		return cut(moneyInBank.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawMoneyInBank() {
		return moneyInBank;
	}
	
	public BigDecimal getMoneyOnHand() {
		return cut((money.subtract(moneyInBank)).multiply(moneyMultiplyer));
	}
	
	public BigDecimal getMoneyMultiplyer() {
		return moneyMultiplyer;
	}
	
	public String getPluralMoney() {
		return pluralMoneyName;
	}
	
	public String getSingularMoney() {
		return singularMoneyName;
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
	
	public BigDecimal getLoanAmount() {
		return cut(loanAmount.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawLoanAmount() {
		return loanAmount;
	}
	
	public BigDecimal getMaxLoan() {
		return cut(maxLoan.multiply(moneyMultiplyer));
	}
	
	public BigDecimal getRawMaxLoan() {
		return maxLoan;
	}
	
	public Conversation getConversation() {
		return convo;
	}
	
	// A method to cut off decimals greater than the hundredth place;
	public BigDecimal cut(BigDecimal x) {
		return x.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN);
	}
	
	// A method to determine if player is a co-ruler
	public boolean isCoRuler(String playername) {
		if (isCountryRuler) {
			if (!countryRuler.getRuler().equalsIgnoreCase(playername)) {
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	// A method to determine if player is a co-mayor
	public boolean isCoMayor(String playername) {
		if (isTownMayor) {
			if (!townMayor.getMayor().equalsIgnoreCase(playername)) {
				return true;
			}
			else return false;
		}
		else return false;
	}
}
