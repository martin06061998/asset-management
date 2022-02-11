/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import dbo.FileHandlerManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Asset;
import model.EnityFactory;

/**
 *
 * @author marti
 */
class AssetController implements I_AssetController {

	private static AssetController controller = null;
	private ArrayList<Asset> assetList;

	private AssetController() {
	}

	static AssetController getInstance() {
		if (controller == null) {
			controller = new AssetController();
			controller.assetList = new ArrayList();
			controller.load();
			for(Asset e : controller.assetList){
				System.out.println(e);
			}
		}
		return controller;
	}

	@Override
	public boolean add(JsonNode dataElement) {
		for (JsonNode element : dataElement) {
			try {
				Asset newAsset = EnityFactory.getInstane().createAsset(element);
				assetList.add(newAsset);
			} catch (IllegalArgumentException ex) {

			}
		}
		save();
		return true;
	}

	@Override
	public boolean delete(JsonNode target) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<Asset> getAll() throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isAcceptable(String key, int command) {
		if (command < 2 || command > 9) {
			return false;
		}
		int privilidge = EmpController.getInstance().getPrivilege(key);
		return privilidge - command >= 0;
	}

	@Override
	public JsonNode get(int pos) throws Exception {
		return null;
	}

	@Override
	public boolean save() {
		boolean ret = true;
		try {
			FileHandlerManager.getInstance().writeBinary(assetList);
		} catch (IOException ex) {
			ret = false;
			Logger.getLogger(AssetController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;
	}

	@Override
	public boolean load() {
		boolean ret = true;
		try {
			assetList = (ArrayList<Asset>) FileHandlerManager.getInstance().readBinary();
		} catch (Exception ex) {
			ret = false;
			Logger.getLogger(AssetController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ret;
	}

}
