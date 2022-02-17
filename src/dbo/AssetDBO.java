/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;
import java.io.IOException;
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
	
	public static boolean saveAssets(HashMap<String, Asset> assetMap) {
		boolean ret = true;
		try {
			Objects.requireNonNull(assetMap);
			FileHandlerManager.getInstance().writeBinary(assetMap, ASSET_FILE);
		} catch (NullPointerException | IOException ex) {
			ret = false;
			Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;
	}
	
	public static boolean saveBorrowState(HashMap<String, BorrowAsset> handling, HashMap<String, BorrowAsset> done) {
		boolean ret = true;
		try {
			Objects.requireNonNull(handling);
			Objects.requireNonNull(done);
			FileHandlerManager.getInstance().writeBinary(done, DONE_FILE);
			FileHandlerManager.getInstance().writeBinary(handling, HANDLING_FILE);
		} catch (NullPointerException | IOException ex) {
			ret = false;
			//Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;
	}
	
	public static boolean loadBorrowState(HashMap<String, BorrowAsset> handling, HashMap<String, BorrowAsset> done) {
		boolean ret = true;
		HashMap<String, BorrowAsset> savedHandling = null;
		HashMap<String, BorrowAsset> savedDone = null;
		try {
			Objects.requireNonNull(handling);
			Objects.requireNonNull(done);
			savedHandling = (HashMap<String, BorrowAsset>) FileHandlerManager.getInstance().readBinary(HANDLING_FILE);
			savedDone = (HashMap<String, BorrowAsset>) FileHandlerManager.getInstance().readBinary(DONE_FILE);
			if (savedDone != null && savedHandling != null) {
				handling.putAll(savedHandling);
				done.putAll(savedDone);
			}
		} catch (Exception ex) {
			ret = false;
			//Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;
		
	}
	
}
