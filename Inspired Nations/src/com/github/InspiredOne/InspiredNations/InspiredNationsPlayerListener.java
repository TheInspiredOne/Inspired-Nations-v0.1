package com.github.InspiredOne.InspiredNations;


import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.InspiredOne.InspiredNations.Bank.Local.ClaimLocalBankPlayerListener;
import com.github.InspiredOne.InspiredNations.Business.Good.ClaimGoodBusinessPlayerListener;
import com.github.InspiredOne.InspiredNations.Business.Good.GoodBusiness;
import com.github.InspiredOne.InspiredNations.Business.Service.ClaimServiceBusinessPlayerListener;
import com.github.InspiredOne.InspiredNations.Business.Service.ServiceBusiness;
import com.github.InspiredOne.InspiredNations.Country.ClaimCountryLandPlayerListener;
import com.github.InspiredOne.InspiredNations.Country.Country;
import com.github.InspiredOne.InspiredNations.Country.UnclaimCountryLandPlayerListener;
import com.github.InspiredOne.InspiredNations.Hall.Local.ClaimLocalHallPlayerListener;
import com.github.InspiredOne.InspiredNations.Hospital.ClaimHospitalPlayerListener;
import com.github.InspiredOne.InspiredNations.House.ClaimHousePlayerListener;
import com.github.InspiredOne.InspiredNations.House.House;
import com.github.InspiredOne.InspiredNations.Park.ClaimFederalParkPlayerListener;
import com.github.InspiredOne.InspiredNations.Park.ClaimParkPlayerListener;
import com.github.InspiredOne.InspiredNations.Park.Park;
import com.github.InspiredOne.InspiredNations.Prison.Local.ClaimLocalPrisonPlayerListener;
import com.github.InspiredOne.InspiredNations.Regions.ChestShopPlayerListener;
import com.github.InspiredOne.InspiredNations.Town.ClaimTownLandPlayerListener;
import com.github.InspiredOne.InspiredNations.Town.Town;
import com.github.InspiredOne.InspiredNations.Town.UnclaimTownLandPlayerListener;

public class InspiredNationsPlayerListener implements Listener {

	// Grabbing instance of plugin
	InspiredNations plugin;
	public InspiredNationsPlayerListener(InspiredNations instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName().toLowerCase();
		if (!plugin.playerdata.containsKey(playername)) {
			plugin.playerdata.put(playername, new PlayerData(plugin));
		}
		plugin.playermodes.put(playername, new PlayerModes(plugin));
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event) {
		ChestShopPlayerListener CSPL = new ChestShopPlayerListener(plugin, event);
		CSPL.onCloseInventory();
	}
	
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event) {
		ChestShopPlayerListener CSPL = new ChestShopPlayerListener(plugin, event);
		CSPL.onOpenInventory();
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		ChestShopPlayerListener CSPL = new ChestShopPlayerListener(plugin, event);
		CSPL.onSignChangeEvent();
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		ChestShopPlayerListener CSPL = new ChestShopPlayerListener(plugin, event);
		CSPL.onBlockPlace();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName().toLowerCase();
		PlayerData PDI = plugin.playerdata.get(playername);
		PlayerModes PM = plugin.playermodes.get(playername);
		
		// put this under onPlayerInteract
		if (!player.isConversing() && (PM.preCountrySelect() || PM.countrySelect() || PM.townSelect() || PM.preTownSelect() || PM.preDeselectCountry() || PM.countryDeselect()
				|| PM.localBankSelect() || PM.localHallSelect() || PM.parkSelect() || PM.hospitalSelect() || PM.localPrisonSelect() || PM.federalParkSelect() ||
				PM.houseSelect() || PM.serviceBusinessSelect() || PM.goodBusinessSelect() || PM.getPlaceItem() || PM.placesign)) {
			PM.preCountry(false);
			PM.predecountry(false);
			PM.country(false);
			PM.decountry(false);
			PM.town(false);
			PM.preTown(false);
			PM.localHall(false);
			PM.localBank(false);
			PM.park(false);
			PM.localPrison(false);
			PM.hospital(false);
			PM.federalPark(false);
			PM.house(false);
			PM.goodBusiness(false);
			PM.serviceBusiness(false);
			PM.setBlocksBack();
			PM.selectCuboid(false);
			PM.selectPolygon(false);
			PM.setPlaceItem(false);
			PM.placesign = false;
			return;
		}
		if (event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ() || (Math.round((event.getFrom().getYaw()/45)) != Math.round((event.getTo().getYaw()/45)))) {
			ClaimCountryLandPlayerListener CLPL = new ClaimCountryLandPlayerListener(plugin, event);
			UnclaimCountryLandPlayerListener UCLPL = new UnclaimCountryLandPlayerListener(plugin, event);
			ClaimTownLandPlayerListener TLPL = new ClaimTownLandPlayerListener(plugin, event);
			UnclaimTownLandPlayerListener UCTLPL = new UnclaimTownLandPlayerListener(plugin, event);
			ClaimFederalParkPlayerListener FPPL = new ClaimFederalParkPlayerListener(plugin, event);
			CLPL.onPlayerMove();
			UCLPL.onPlayerMove();
			TLPL.onPlayerMove();
			FPPL.onPlayerMove();
			UCTLPL.onPlayerMove();
			
			// Tracker
			if (PDI.getIsInCountry()) {
				
				// In Country Park
				PDI.setInFederalPark(false);
				for (Park park : PDI.getCountryIn().getParks()) {
					if (park.isIn(event.getTo())) {
						try {
							if (PDI.getFederalParkIn().equals(park)) {
								PDI.setInFederalPark(true);
								break;
							}
							else {
								PDI.setFederalParkIn(park);
								PDI.setInFederalPark(true);
								player.sendRawMessage("You have entered a Park of " + PDI.getCountryIn().getName() + ".");
								break;
							}
						}
						catch (Exception ex) {
							PDI.setInFederalPark(true);
							PDI.setFederalParkIn(park);
							player.sendRawMessage("You have entered a Park of " + PDI.getCountryIn().getName() + ".");
							break;
						}
					}
				}
				if (!PDI.getIsInFederalPark()) {
					try {
						if (!PDI.getFederalParkIn().equals(null)) {
							player.sendRawMessage("You have exited a Park of " + PDI.getCountryIn().getName() + ".");
						}
					}
					catch (Exception ex) {}
					PDI.setFederalParkIn(null);
				}
				
				// In Town
				if (PDI.getIsInTown()) {
					Town town = PDI.getTownIn();
					
					if (!town.isIn(event.getTo())) {
						PDI.setInTown(false);
						PDI.setTownIn(null);
						PDI.setInCapital(false);
						if (!PM.townDeselect() && !PM.preDeselectTown() && !PM.townSelect() && !PM.townDeselect()) {
							player.sendRawMessage("You have exited the town " + town.getName() + ".");
						}
					}
					
					// Town Hall
					if (town.hasTownHall()) {
						if (town.getTownHall().isIn(event.getTo())) {
							if (!PDI.getIsInLocalHall()) {
								player.sendRawMessage("You have entered the town hall of "  + town.getName() + ".");
							}
							PDI.setInLocalHall(true);
							PDI.setLocalHallIn(town.getTownHall());
							if (town.isCapital()) {
								PDI.setInFederalHall(true);
							}
						}
						else {
							if (PDI.getIsInLocalHall()) {
								player.sendRawMessage("You have exited the town hall of "  + town.getName() + ".");
							}
							PDI.setInLocalHall(false);
							PDI.setLocalHallIn(null);
							PDI.setInFederalHall(false);

						}
					}
					
					// Bank
					if (town.hasBank()) {
						if (town.getBank().isIn(event.getTo())) {
							if (!PDI.getIsInLocalBank()) {
								player.sendMessage("You have entered the bank of " + town.getName() + ".");
							}
							PDI.setInLocalBank(true);
							PDI.setLocalBankIn(town.getBank());
							if (town.isCapital()) {
								PDI.setInFederalBank(true);
							}
						}
						else {
							if (PDI.getIsInLocalBank()) {
								player.sendMessage("You have exited the bank of " + town.getName() + ".");
							}
							PDI.setInLocalBank(false);
							PDI.setLocalBankIn(null);
							PDI.setInFederalBank(false);
						}
					}
					
					// Hospital
					if (town.hasHospital()) {
						if (town.getHospital().isIn(event.getTo())) {
							if (!PDI.getIsInHospital()) {
								player.sendRawMessage("You have entered the hospital of " + town.getName() + ".");
							}
							PDI.setInHospital(true);
							PDI.setHospitalIn(town.getHospital());
						}
						else {
							if (PDI.getIsInHospital()) {
								player.sendRawMessage("You have exited the hospital of " + town.getName() + ".");
							}
							PDI.setInHospital(false);
							PDI.setHospitalIn(null);
						}
					}
					
					// Prison
					if (town.hasPrison()) {
						if (town.getPrison().isIn(event.getTo())) {
							if (!PDI.getIsInLocalPrison()) {
								player.sendRawMessage("You have entered the prison of " + town.getName() + ".");
							}
							PDI.setInLocalPrison(true);
							PDI.setLocalPrisonIn(town.getPrison());
							if (town.isCapital()) {
								PDI.setInFederalPrison(true);
							}
						}
						else {
							if (PDI.getIsInLocalPrison()) {
								player.sendRawMessage("You have exited the prison of " + town.getName() + ".");
							}
							PDI.setInLocalPrison(false);
							PDI.setLocalBankIn(null);
							PDI.setInFederalPrison(false);
						}
					}
					
					// Parks
					PDI.setInLocalPark(false);
					for (Park park : town.getParks()) {
						if (park.isIn(event.getTo())) {
							try {
								if (PDI.getLocalParkIn().equals(park)) {
									PDI.setInLocalPark(true);
									break;
								}
								else {
									PDI.setLocalParkIn(park);
									PDI.setInLocalPark(true);
									player.sendRawMessage("You have entered a Park of " + town.getName() + ".");
									break;
								}
							}
							catch (Exception ex) {
								PDI.setInLocalPark(true);
								PDI.setLocalParkIn(park);
								player.sendRawMessage("You have entered a Park of " + town.getName() + ".");
								break;
							}
						}
					}
					if (!PDI.getIsInLocalPark()) {
						try {
							if (!PDI.getLocalParkIn().equals(null)) {
								player.sendRawMessage("You have exited a Park of " + town.getName() + ".");
							}
						}
						catch (Exception ex) {}
						PDI.setLocalParkIn(null);


					}
					
					// Houses
					PDI.setInHouse(false);
					for (House house : town.getHouses()) {
						if (house.isIn(event.getTo())) {
							try {
								if (PDI.getHouseIn().equals(house)) {
									PDI.setInHouse(true);
									break;
								}
								else {
									PDI.setInHouse(true);
									PDI.setHouseIn(house);
									break;
								}
							}
							catch(Exception ex) {
								PDI.setInHouse(true);
								PDI.setHouseIn(house);
								break;
							}
						}
					}
					if (!PDI.getIsInHouse()) {
						PDI.setHouseIn(null);
					}
					
					// Good Businesses
					PDI.setInGoodBusiness(false);
					for (GoodBusiness business : town.getGoodBusinesses()) {
						if (business.isIn(event.getTo())) {
							try {
								if (PDI.getGoodBusinessIn().equals(business)) {
									PDI.setInGoodBusiness(true);
									break;
								}
								else {
									PDI.setGoodBusinessIn(business);
									PDI.setInGoodBusiness(true);
									break;
								}
							}
							catch(Exception ex) {
								PDI.setGoodBusinessIn(business);
								PDI.setInGoodBusiness(true);
								break;
							}
						}
					}
					if (!PDI.getIsInGoodBusiness()) {
						PDI.setGoodBusinessIn(null);
					}
					
					// Service Business
					PDI.setInServiceBusiness(false);
					for (ServiceBusiness business : town.getServiceBusinesses()) {
						if (business.isIn(event.getTo())) {
							try {
								if (PDI.getServiceBusinessIn().equals(business)) {
									PDI.setInServiceBusiness(true);
									break;
								}
								else {
									PDI.setServiceBusinessIn(business);
									PDI.setInServiceBusiness(true);
								}
							}
							catch (Exception ex) {
								PDI.setServiceBusinessIn(business);
								PDI.setInServiceBusiness(true);
								break;
							}
						}
					}
					if (!PDI.getIsInServiceBusiness()) {
						PDI.setServiceBusinessIn(null);
					}
				}
				
				// In Country, Not in Town
				else {
					for(Town town2 : PDI.getCountryIn().getTowns()) {
						if (town2.isIn(event.getTo())) {
							PDI.setInTown(true);
							PDI.setTownIn(town2);
							if (town2.isCapital()) {
								PDI.setInCapital(true);
							}
							if (!PM.townDeselect() && !PM.preDeselectTown() && !PM.townSelect() && !PM.townDeselect()) {
								player.sendRawMessage("You have entered the town " + town2.getName() + ".");
							}
							break;
						}
					}
				}
				
				if (!PDI.getCountryIn().isIn(event.getTo())) {
					if (!PM.countryDeselect() && !PM.preDeselectCountry() && !PM.countrySelect() && !PM.preCountrySelect()) {
						player.sendRawMessage("You have exited the country " + PDI.getCountryIn().getName() + ".");
					}

					PDI.setInCountry(false);
					PDI.setCountryIn(null);
				}
			}
			
			// Not in Country
			else {
				for (String name: plugin.countrydata.keySet()) {
					Country country = plugin.countrydata.get(name);
					if (country.isIn(event.getTo())) {
						PDI.setInCountry(true);
						PDI.setCountryIn(country);
						if (!PM.countryDeselect() && !PM.preDeselectCountry() && !PM.countrySelect() && !PM.preCountrySelect()) {
							player.sendRawMessage("You have entered the country " + country.getName() + ".");
						}
						break;
					}			
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName().toLowerCase();
		PlayerData PDI = plugin.playerdata.get(playername);
		PlayerModes PM = plugin.playermodes.get(playername);
		
		ClaimLocalHallPlayerListener LHPL = new ClaimLocalHallPlayerListener(plugin, event);
		ClaimLocalBankPlayerListener LBPL = new ClaimLocalBankPlayerListener(plugin, event);
		ClaimParkPlayerListener PPL = new ClaimParkPlayerListener(plugin, event);
		ClaimHospitalPlayerListener HPL = new ClaimHospitalPlayerListener(plugin, event);
		ClaimLocalPrisonPlayerListener LPPL = new ClaimLocalPrisonPlayerListener(plugin, event);
		ClaimFederalParkPlayerListener FPPL = new ClaimFederalParkPlayerListener(plugin, event);
		ClaimHousePlayerListener CHPL = new ClaimHousePlayerListener(plugin, event);
		ClaimServiceBusinessPlayerListener CSBPL = new ClaimServiceBusinessPlayerListener(plugin, event);
		ClaimGoodBusinessPlayerListener CGBPL = new ClaimGoodBusinessPlayerListener(plugin, event);
		ChestShopPlayerListener CSPL = new ChestShopPlayerListener(plugin, event);
		LHPL.onPlayerInteract();
		LBPL.onPlayerInteract();
		PPL.onPlayerInteract();
		HPL.onPlayerInteract();
		LPPL.onPlayerInteract();
		FPPL.onPlayerInteract();
		CHPL.onPlayerInteract();
		CSBPL.onPlayerInteract();
		CGBPL.onPlayerInteract();
		CSPL.onInteractWithChest();
		
/*		if (event.getClickedBlock().getTypeId() == 68 || event.getClickedBlock().getTypeId() == 63) {
			Sign sign = (Sign) event.getClickedBlock().getState();
			plugin.logger.info(sign.getLine(1));
		} */
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName().toLowerCase();
		PlayerData PDI = plugin.playerdata.get(playername);
		PlayerModes PM = plugin.playermodes.get(playername);
		PM.setBlocksBack();
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName().toLowerCase();
		PlayerData PDI = plugin.playerdata.get(playername);
		PlayerModes PM = plugin.playermodes.get(playername);
		PM.setBlocksBack();
	}
	
}
