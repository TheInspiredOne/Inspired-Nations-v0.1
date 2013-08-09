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
package com.github.InspiredOne.InspiredNations.Regions;

import java.awt.Point;
import java.awt.Polygon;
import org.bukkit.Location;

public class polygonPrism {
	private Polygon polygon = new Polygon();
	private int[] x;
	private int[] z;
	private int ymin = 0;
	private int ymax = 256;
	private String world;

	// Make a Polygon Prism with the locations selected. The base and roof y components are selected by the lowest and highest locations selected
	
	public polygonPrism(String worldtemp) {
		polygon.reset();
		ymin = 0;
		ymax = 256;
		world = worldtemp;
	}
	
	public polygonPrism(Point[] points, String worldtemp) {
		polygon.reset();
		ymin = 0;
		ymax = 256;
		world = worldtemp;
		for (int i = 0; i < points.length; i++) {
			polygon.addPoint(points[i].x, points[i].y);
		}
	}
	
	public polygonPrism(Location[] points, String worldtemp) {
		polygon.reset();
		ymin = 0;
		ymax = 256;
		world = worldtemp;
		ymin = points[0].getBlockY();
		ymax = ymin;
		for (int i = 0; i < points.length; i++) {
			if (points[i].getBlockY() < ymin) {
				ymin = points[i].getBlockY();
			}
			if (points[i].getBlockY() > ymax) {
				ymax = points[i].getBlockY();
			}
			x[i] = points[i].getBlockX();
			z[i] = points[i].getBlockZ();
		}
		polygon = new Polygon(x, z, points.length);
	}
	
	// Determines if a location is inside of the polygon
	public boolean isIn(Location tile) {
		tile.getBlockX();
		if (tile.getWorld().getName().equalsIgnoreCase(world)) {
			if (polygon.contains(tile.getBlockX(), tile.getBlockZ())) {// || polygon.contains(tile.getBlockX() + .5, tile.getBlockZ() + .5) || polygon.contains(tile.getBlockX() - .5, tile.getBlockZ() + .5) || polygon.contains(tile.getBlockX() + .5, tile.getBlockZ() - .5)) {
				if (tile.getBlockY() <= ymax && tile.getBlockY() >= ymin) {
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}
	
    public int Area() {
    	double sum = 0.0;
    	for (int i = 0; i < polygon.npoints; i++) {
    		if (!((i + 1) < polygon.npoints)) {
    			sum = sum + ((polygon.xpoints[i] * polygon.ypoints[0]) - (polygon.ypoints[i] * polygon.xpoints[0]));
    		}
    		else {
    			sum = sum + ((polygon.xpoints[i] * polygon.ypoints[i + 1]) - (polygon.ypoints[i] * polygon.xpoints[i + 1]));
    		}
    	}
    	return (int) Math.abs(.5 * sum);
    }
    
    // Finds the volume of the polygon prism
    public int Volume() {
    	int height = ymax - ymin + 1;
    	return Area() * height;
    }
    
    // Finds the Perimeter of the polygon
    public int Perimeter() {
    	double length = 0.0;
    	for (int i = 0; i < polygon.npoints - 1; i++) {
    		length = length + Point.distance(polygon.xpoints[i], polygon.ypoints[i], polygon.xpoints[i +1], polygon.ypoints[i+1]);
    	}
    	return (int) length;
    }
    
    
    // Setters
    public void setYMax(int max) {
    	ymax = max;
    }
    
    public void setYMin(int min) {
    	ymin = min;
    }
    
    public void addVertex(Point corner) {
    	polygon.addPoint(corner.x, corner.y);
    }
    
    public void addVertex(Point corner, int y) {
    	polygon.addPoint(corner.x, corner.y);
    	if (polygon.npoints < 2) {
    		setYMax(y);
    		setYMin(y);
    	}
    	else if(y > ymax) {
    		setYMax(y);
    	}
    	else if (y < ymin) {
    		setYMin(y);
    	}
    }
    
    public void setPolygon(Polygon poly) {
    	polygon = poly;
    }
    // Getters
    public int getYMax() {
    	return ymax;
    }
    
    public int getYMin() {
    	return ymin;
    }
    
    public String getWorld() {
    	return world;
    }
    
    public Polygon getPolygon() {
    	return polygon;
    }
}
