/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.Inputter;
import utils.Utils;

/**
 *
 * @author marti
 */
public class ManagerMenu extends AssetManagementMenu {

	protected ManagerMenu(String newKey) {
		super(newKey);
	}

	@Override
	protected void loadMainMenu() {
		addItem("Search asset by name");
		addItem("Create new asset");
		addItem("Updating asset's information");
		addItem("Approve the request of employee");
		addItem("Show list of borrow asset");
		addItem("Quit");
	}

	/*
	*Create a request to add assets
	*
	 */
	JsonNode addAsset() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		ArrayNode data = mapper.createArrayNode();
		request.put("key", getKey());
		int command = rHandler.addAssetCommandID();
		request.put("command", String.valueOf(command));
		System.out.println("Welcome To Asset Add Form: ");

		boolean isContinue = true;
		while (isContinue) {
			String id = Inputter.inputNotBlankStr("Please enter id: ");
			String name = Inputter.inputNotBlankStr("Please enter name: ");
			String color = Inputter.inputNotBlankStr("Please enter color: ");
			long price = Inputter.inputLong("Please enter price: ");
			double weight = Inputter.inputDouble("Please enter weight: ");
			int quantity = Inputter.inputInteger("Please enter quantity: ");

			ObjectNode item = mapper.createObjectNode();
			item.put("id", id);
			item.put("name", name);
			item.put("price", String.valueOf(price));
			item.put("color", color);
			item.put("weight", String.valueOf(weight));
			item.put("quantity", String.valueOf(quantity));
			data.add(item);
			isContinue = Utils.confirmYesNo("Do you want to continue[y/n]: ");
		}

		request.set("data", data);
		return request;
	}

	void loadSearchMenu() {
		String name = Utils.getString("Enter the name you want to search: ");
	}

	@Override
	protected void searchAssetByName() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	/*
	* Send request to server	*
	 */
	@Override
	protected void sendRequest(JsonNode request) {
		JsonNode reply = rHandler.handle(request);
		printMessage(reply);
	}

	@Override
	protected void breadth() {
		JsonNode request = null;
		showMenu();
		int choice = getChoice();
		switch (choice) {
			case 2:
				request = addAsset();
				break;
			default:
				break;
		}
		sendRequest(request);
	}
}
