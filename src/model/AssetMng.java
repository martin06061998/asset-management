/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author marti
 * 
 */
public class AssetMng {
	final String requestID;
	
	/*
	*The employee who request to borrow the asset
	*/
	final Employee emp;
	
	/*
	*The asset to be borrowed
	*/
	final Asset asset;
	
	/*
	*The offered quantity
	*/
	int qty;
	
	/*
	*The request date
	*/
	final Date rDate;

	public AssetMng(String requestID, Employee emp, Asset asset, int qty, Date rDate) {
		this.requestID = requestID;
		this.emp = emp;
		this.asset = asset;
		this.qty = qty;
		this.rDate = rDate;
	}

	public String getRequestID() {
		return requestID;
	}

	public Employee getEmp() {
		return emp;
	}

	public Asset getAsset() {
		return asset;
	}

	public int getQty() {
		return qty;
	}

	public Date getrDate() {
		return rDate;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
}
