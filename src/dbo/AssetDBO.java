/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import model.Asset;
import model.BorrowAsset;

/**
 *
 * @author marti
 */
public class AssetDBO {

    private final static String ASSET_FILE = "database\\asset.dat";
    private final static String DONE_FILE = "database\\borrow.dat";
    private final static String HANDLING_FILE = "database\\request.dat";
    private final static String BORROW_SYSTEM_FILE = "database\\borrowsystem.dat";

    private AssetDBO() {
    }

    public static boolean saveAssets(HashMap<String, Asset> assetMap) {
        boolean ret = true;
        try {
            Objects.requireNonNull(assetMap);
            FileHandlerManager.getInstance().writeBinary(assetMap, ASSET_FILE);
        } catch (NullPointerException | IOException ex) {
            ret = false;
            //Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static HashMap<String, Asset> loadAssets() {
        Serializable savedAssets = null;
        try {
            savedAssets = FileHandlerManager.getInstance().readBinary(ASSET_FILE);
        } catch (Exception ex) {
            //Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (HashMap<String, Asset>) savedAssets;
    }

    public static boolean saveBorrowState(HashMap<Integer, HashMap<Integer, BorrowAsset>> waiting, HashMap<Integer, HashMap<Integer, BorrowAsset>> approved) {
        boolean ret = true;
        try {
            FileHandlerManager.getInstance().writeBinary(approved, DONE_FILE);
            FileHandlerManager.getInstance().writeBinary(waiting, HANDLING_FILE);
        } catch (NullPointerException | IOException ex) {
            ret = false;
            //Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static boolean loadBorrowState(HashMap<Integer, HashMap<Integer, BorrowAsset>> waitingMapDirectory, HashMap<Integer, HashMap<Integer, BorrowAsset>> approvedMapDirectory) {
        boolean ret = false;
        HashMap<Integer, HashMap<Integer, BorrowAsset>> savedWaitingMapDirectory = null;
        HashMap<Integer, HashMap<Integer, BorrowAsset>> savedApprovedMapDirectory = null;
        try {
            savedWaitingMapDirectory = (HashMap<Integer, HashMap<Integer, BorrowAsset>>) FileHandlerManager.getInstance().readBinary(HANDLING_FILE);
            savedApprovedMapDirectory = (HashMap<Integer, HashMap<Integer, BorrowAsset>>) FileHandlerManager.getInstance().readBinary(DONE_FILE);
        } catch (Exception ex) {
            //Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (savedWaitingMapDirectory != null && savedApprovedMapDirectory != null) {
                ret = true;
                waitingMapDirectory.putAll(savedWaitingMapDirectory);
                approvedMapDirectory.putAll(savedApprovedMapDirectory);
            }
        }
        return ret;

    }

    public static boolean saveSystemFile(Serializable borrowMng) {
        boolean ret = true;
        try {
            FileHandlerManager.getInstance().writeBinary(borrowMng, BORROW_SYSTEM_FILE);
        } catch (Exception ex) {
            ret = false;
        }
        return ret;

    }

    public static Serializable loadSystemFile() {
        Serializable ret = null;
        try {
            ret = FileHandlerManager.getInstance().readBinary(BORROW_SYSTEM_FILE);
        } catch (Exception ex) {
            //Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;

    }

}
