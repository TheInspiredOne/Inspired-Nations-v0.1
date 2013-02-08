package com.github.InspiredOne.InspiredNations;

import java.math.BigDecimal;

import com.github.InspiredOne.InspiredNations.Town.Town;


public class TownMethods {
	
	InspiredNations plugin;
	Town town;
	
	public TownMethods(InspiredNations instance, Town towntemp) {
		plugin = instance;
		town = towntemp;
	}
	
	public BigDecimal getTaxAmount() {
		BigDecimal amount = new BigDecimal(town.getArea()*town.getProtectionLevel()*town.getNationTax()/100);
		for (int i = 0; i < town.getParks().size(); i++) {
			amount = amount.add(town.getMoneyMultiplyer().multiply((new BigDecimal(town.getParks().get(i).Volume() * town.getParks().get(i).getProtectionLevel() * town.getProtectionLevel() * town.getNationTax()/10000 * plugin.getConfig().getDouble("park_tax_multiplyer")))));
		}
		return cut(amount);
	}
	
	public BigDecimal getTaxAmount(int level) {
		BigDecimal amount = new BigDecimal(town.getArea()*level*town.getNationTax()/100);
		for (int i = 0; i < town.getParks().size(); i++) {
			amount = amount.add(town.getMoneyMultiplyer().multiply((new BigDecimal(town.getParks().get(i).Volume() * town.getParks().get(i).getProtectionLevel() * town.getNationTax()/10000 * plugin.getConfig().getDouble("park_tax_multiplyer")*level))));
		}
		return cut(amount);
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
}
