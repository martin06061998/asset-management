/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import utils.StringUtilities;

/**
 *
 * @author marti
 */
public class BorrowAsset implements Serializable {

    private static final long serialVersionUID = 4536821300566461231L;
    private int bID;
    private final String assetID;
    private final String employeeID;
    private final int offeredQty;
    private final Date requestDateTime;
    private Date borrowDateTime;
    private int approvedQty;

    private BorrowAsset(int bID, String assetID, String employeeID, int offeredQty, Date requestDate, Date borrowDate, int approvedQty) {
        this.bID = bID;
        this.assetID = StringUtilities.toLowerCasse(assetID);
        this.employeeID = StringUtilities.toLowerCasse(employeeID);
        this.offeredQty = offeredQty;
        if (requestDate == null) {
            this.requestDateTime = new Date();
        } else {
            this.requestDateTime = requestDate;
        }
        this.borrowDateTime = borrowDate;
        this.approvedQty = approvedQty;
    }

    public static BorrowAsset createNewRequest(int bID, String assetID, String employeeID, int quantity) {
        return new BorrowAsset(bID, assetID, employeeID, quantity, null, null, 0);
    }

    public static BorrowAsset createNewRequest(int bID, String assetID, String employeeID, int quantity, Date newDate) {
        return new BorrowAsset(bID, assetID, employeeID, quantity, newDate, null, 0);
    }

    public static BorrowAsset createApprovedRequest(int bID, String assetID, String employeeID, int quantity, Date borrowDate) {
        Date requestDate = new GregorianCalendar(2021,GregorianCalendar.JULY,7,7,7,7).getTime();
        return new BorrowAsset(bID, assetID, employeeID, quantity, requestDate, borrowDate, quantity);
    }

    public void setBID(int bID) {
        this.bID = bID;
    }

    public int bID() {
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
