/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author marti
 */
public class Asset {

	final String assetID;
	String assetName;
	String color;
	long price;
	double weight;
	int quantity;
	int curQty;

	Asset(String assetID, String name, String color, long price, double weight, int quantity) {
		this.assetID = assetID;
		this.assetName = name;
		this.color = color;
		this.price = price;
		this.weight = weight;
		this.quantity = quantity;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String name) {
		this.assetName = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCurQty() {
		return curQty;
	}

	public void setCurQty(int curQty) {
		this.curQty = curQty;
	}
}
