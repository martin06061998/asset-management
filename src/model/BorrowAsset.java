/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import utils.StringUtilities;

/**
 *
 * @author marti
 */
public class BorrowAsset implements Serializable {

	private static final long serialVersionUID = 4536821300566461231L;
	private final String bID;
	private final String assetID;
	private final String employeeID;
	private final int offeredQty;
	private final Date requestDateTime;
	private Date borrowDateTime;
	private int approvedQty;

	private BorrowAsset(String bID, String assetID, String employeeID, int offeredQty) {
		this.bID = StringUtilities.toLowerCasse(bID);
		this.assetID = StringUtilities.toLowerCasse(assetID);
		this.employeeID = StringUtilities.toLowerCasse(employeeID);
		this.offeredQty = offeredQty;
		this.requestDateTime = new Date();
	}

	public static BorrowAsset createNewRequest(String bID, String assetID, String employeeID, int offeredQty) {
		return new BorrowAsset(bID, assetID, employeeID, offeredQty);
	}

	public String rID() {
		return Map_bID_toRID(bID);
	}

	final static public String Map_rID_toBID(String rID) {
		return rID.replace("r", "b");
	}

	final static public String Map_bID_toRID(String bID) {
		return bID.replace("b", "r");
	}

	public String bID() {
		return bID;
	}

	public int approvedQty() {
		return approvedQty;
	}

	public String employeeID() {
		return employeeID;
	}

	public Date requestDateTIme() {
		return requestDateTime;
	}

	public int offeredQty() {
		return offeredQty;
	}

	public void setBorrowDateTime(Date borrowDateTime) {
		this.borrowDateTime = borrowDateTime;
	}

	public void setApprovedQty(int approvedQty) {
		this.approvedQty = approvedQty;
	}

	public String assetID() {
		return assetID;
	}

	public boolean isFinished() {
		return (offeredQty - approvedQty) == 0;
	}

	public Date getBorrowDateTime() {
		return borrowDateTime;
	}
}
