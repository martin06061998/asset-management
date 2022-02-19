/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.Serializable;
import utils.StringUtilities;

/**
 *
 * @author marti
 */
public class Asset implements Serializable {

	private static final long serialVersionUID = 4573101304386461231L;
	final String assetID;
	String name;
	String color;
	long price;
	double weight;
	int quantity;
	int curQty;

	public Asset(String assetID, String name, String color, long price, double weight, int quantity) {
		if (assetID == null || name == null || color == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		if (weight <= 0) {
			throw new IllegalArgumentException("weight should not be zero or negagive");
		}
		if (price <= 0) {
			throw new IllegalArgumentException("price should not be zero or negagive");
		}
		if (quantity <= 0) {
			throw new IllegalArgumentException("quantity should not be zero or negagive");
		}
		this.assetID = StringUtilities.toLowerCasse(assetID);
		this.name = StringUtilities.toLowerCasse(name);
		this.color = StringUtilities.toLowerCasse(color);
		this.price = price;
		this.weight = weight;
		this.quantity = quantity;
		this.curQty = quantity;
	}

	public String getAssetID() {
		return assetID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.name = StringUtilities.toLowerCasse(name);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if (color == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.color = StringUtilities.toLowerCasse(color);
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		if (price <= 0) {
			throw new IllegalArgumentException("price should not be zero or negagive");
		}
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		if (weight <= 0) {
			throw new IllegalArgumentException("weight should not be zero or negagive");
		}
		this.weight = weight;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("quantity should not be zero or negagive");
		}
		this.quantity = quantity;
	}

	public int getCurQty() {
		return curQty;
	}

	public void setCurQty(int curQty) {
		if (curQty < 0) {
			throw new IllegalArgumentException("current quantity should not be negagive");
		}
		this.curQty = curQty;
	}

	public boolean isEnough(int offeredQty) {
		return curQty > offeredQty;
	}

	public JsonNode toJsonNode() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode answer = mapper.createObjectNode();
		answer.put("id", assetID);
		answer.put("name", name);
		answer.put("color", color);
		answer.put("price", String.valueOf(price));
		answer.put("weight", String.valueOf(weight));
		answer.put("quantity", String.valueOf(quantity));
                answer.put("Current quantity", String.valueOf(curQty));
		return answer;
	}
}
