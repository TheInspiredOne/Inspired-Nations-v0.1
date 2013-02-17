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
package com.github.InspiredOne.InspiredNations.Economy;

public class Functions {

	public Functions() {
		
	}
	public double basebase(double x, double a, double b, double c, double d, double f, double g, double h, double j, double k) {
		double answer = 0;
		double e = Math.E;
		double pi = Math.PI;
		answer = (a-k+((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b))));
		answer = answer*Math.pow(e, (((x-c)/(-c))/Math.log((((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b)))*(a-k)+(Math.pow(((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b))), 2))/(a-k)))));
		answer = answer/((a-k+((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b))))+Math.pow(e, (((x-c)/(-c))/Math.log(((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b)))*(a-k)+(Math.pow(((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b))), 2))/(a-k)))));
		answer = answer-((a-k)*Math.pow(e, -c/d))/((a-k+Math.pow(e, -c/b)));
		answer = answer-Math.abs((d*Math.pow(e, (x-c-g)/f)/(d+Math.pow(e, (x-c-g)/f)))*(Math.sin((pi*x)/(h*(Math.pow(.98, (x-c-g)/j))))*Math.cos((pi*x)/(h*(Math.pow(.96, (x-c-g)/j))))))+k;
		answer = Math.abs(answer);
		return answer;
	}
	
	public double basebuy(double x, double l, double m, double n, double y) {
		double answer = 0;
		double e = Math.E;
		answer = ((l-y+((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m))));
		answer = answer*Math.pow(e, ((x-n)/-n)/Math.log((((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m))*(l-y)+(Math.pow(((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m)), 2)))/(l-y)));
		answer = answer/(l-y+((l-y+((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m))))+Math.pow(e, ((x-n)/-n)/Math.log((((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m))*(l-y)+(Math.pow(((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m)), 2)))/(l-y))));
		answer = answer-((l-y+((l-y)*Math.pow(e, -n/m))/(l-y+Math.pow(e, -n/m))))+y;
		return answer;
	}
	
	public double basesell(double x, double p, double q, double r, double s, double y) {
		double answer = 0;
		double e = Math.E;
		answer = ((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q))));
		answer = answer*Math.pow(e, ((x-r)/-x)/Math.log(((((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q))))*(s*y-p))+Math.pow(((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q)))), 2))/(s*y-p)));
		answer = answer/(((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q))))+Math.pow(e, ((x-r)/-x)/Math.log(((((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q))))*(s*y-p))+Math.pow(((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q)))), 2))/(s*y-p))));
		answer = s*y-answer+((s*y-p+((s*y-p)*Math.pow(e, -r/q))/(s*y-p+Math.pow(e, -r/q))));
		return answer;
	}
	
	public double npcspend(double x, double h, double j, double k, double m, double n){
		double answer = 0;
		double e = Math.E;
		answer = ((x*m*e)/n)*Math.pow(e, -x/n)*Math.pow(e, (-(Math.pow(x-n, 2))/(2*Math.pow(n, 2))))*(1-(h/(j*k)));
		return answer;
	}
	
	public double npcbuy(double x, double h, double j, double k, double m, double n) {
		double answer = 0;
		double e = Math.E;
		answer = ((m*e)/n)*Math.pow(e, -x/n)*Math.pow(e, (-(Math.pow(x-n, 2))/(2*Math.pow(n, 2))))*(1-(h/(j*k)));
		return answer;
	}
	
	public double base(double x, double a, double b, double c, double d, double f, double g, double h, double j, double k, double H, double E) {
		double answer = 0;
		answer = basebase(x/H, a, b, c, d, f, g, h, j, k)*E;
		return answer;
	}
	
	public double buy(double x, double l, double m, double n, double y, double H, double E) {
		double answer = 0;
		answer = basebuy(x/H, l*E, m, n, y);
		return answer;
	}
	
	public double sell(double x, double p, double q, double r, double s, double y, double H, double E) {
		double answer = 0;
		answer = basesell(x/H, p*E, q, r, s, y);
		return answer;
	}
	
	public double percentsell(double E, double p, double T, double y) {
		double answer = 0;
		answer = (T*(y-E*p)+E*p)/y;
		return answer;
	}
	
}
