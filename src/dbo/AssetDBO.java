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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Asset;
import model.BorrowAsset;

/**
 *
 * @author marti
 */
public class AssetDBO {

	private final static String ASSET_FILE = "src\\dbo\\asset.dat";
	private final static String DONE_FILE = "src\\dbo\\borrow.dat";
	private final static String HANDLING_FILE = "src\\dbo\\request.dat";
	private final static String BORROW_SYSTEM_FILE = "src\\dbo\\borrowsystem.dat";

	private AssetDBO() {
	}

	;
	
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

	public static boolean loadAssets(HashMap<String, Asset> assetMap) {
		HashMap<String, Asset> savedAssets = null;
		boolean ret = true;
		try {
			Objects.requireNonNull(assetMap);
			savedAssets = (HashMap<String, Asset>) FileHandlerManager.getInstance().readBinary(ASSET_FILE);
			if (savedAssets != null) {
				assetMap.putAll(savedAssets);
			}
		} catch (Exception ex) {
			ret = false;
			//Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;
	}

	public static boolean backupBorrowState(HashMap<Integer, HashMap<String, BorrowAsset>> waiting, HashMap<Integer, HashMap<String, BorrowAsset>> approved) {
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

	public static boolean restoreBorrowState(HashMap<Integer, HashMap<String, BorrowAsset>> handling, HashMap<Integer, HashMap<String, BorrowAsset>> done) {
		boolean ret = true;
		HashMap<Integer, HashMap<String, BorrowAsset>> savedHandling = null;
		HashMap<Integer, HashMap<String, BorrowAsset>> savedDone = null;
		try {
			savedHandling = (HashMap<Integer, HashMap<String, BorrowAsset>>) FileHandlerManager.getInstance().readBinary(HANDLING_FILE);
			savedDone = (HashMap<Integer, HashMap<String, BorrowAsset>>) FileHandlerManager.getInstance().readBinary(DONE_FILE);
		} catch (Exception ex) {
			ret = false;
			//Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (savedDone != null && savedHandling != null) {
				handling.putAll(savedHandling);
				done.putAll(savedDone);
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
			ret = false;
			//Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;

	}

}
