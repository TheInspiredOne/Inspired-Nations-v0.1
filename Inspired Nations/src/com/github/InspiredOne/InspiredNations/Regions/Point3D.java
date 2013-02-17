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

public class Point3D {

	public String world;
	public int x;
	public int y;
	public int z;
	
	public Point3D(String worldtemp, int xtemp, int ytemp, int ztemp) {
		world = worldtemp;
		x = xtemp;
		y = ytemp;
		z = ztemp;
	}
	
	public void setWorld(String worldtemp) {
		world = worldtemp;
	}
	
	public void setX(int xtemp) {
		x = xtemp;
	}
	
	public void setY(int ytemp) {
		y = ytemp;
	}
	
	public void setZ(int ztemp) {
		z = ztemp;
	}
	
	public String getWorld() {
		return world;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
}
