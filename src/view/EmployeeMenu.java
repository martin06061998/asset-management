/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.fasterxml.jackson.databind.JsonNode;



/**
 *
 * @author marti
 */
public class EmployeeMenu extends AssetManagementMenu {
	protected EmployeeMenu(String newKey){
		super(newKey);
	}

	@Override
	protected void loadMainMenu() {
		addItem("");
	}

	@Override
	protected void searchAssetByName() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	protected void sendRequest(JsonNode request) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void breadth() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
