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
import controlller.RequestHandler;
import java.util.Iterator;
import utils.Inputter;

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
		int command = RequestHandler.ADD_ASSET;
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
			isContinue = confirmYesNo("Do you want to continue[y/n]: ");
		}

		request.set("data", data);
		return request;
	}

	JsonNode getAsset() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		request.put("key", getKey());
		int command = RequestHandler.GET_ASSET;
		request.put("command", command);
		String id = Inputter.inputNotBlankStr("Please enter the id of the asset you want to update");
		request.put("id", id);
		return request;
	}

	JsonNode updateAsset() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		ArrayNode data = mapper.createArrayNode();
		request.put("key", getKey());
		request.put("command", RequestHandler.GET_ASSET);
		JsonNode searchRequest = getAsset();
		JsonNode response = sendRequest(searchRequest);
		String msg = response.get("message").asText();
		if (!msg.equals("found")) {
			return null;
		}
		ObjectNode asset = (ObjectNode) response.get("data").get(0);
		Iterator<String> keys = asset.fieldNames();
		String key;
		while (keys.hasNext()) {
			key = keys.next();
			if (key.contains("id")) {
				continue;
			}
			String value = asset.get(key).asText();
			boolean makeChange = confirmYesNo("Do you you want to change " + key + " ( Old value: " + value + " ) [y/n]: ");
			if (makeChange) {
				String newValue = Inputter.inputNotBlankStr("Please enter new " + key + ": ");
				asset.put(key, newValue);
			}
		}
		data.add(asset);
		request.set("data", data);
		return request;
	}

	JsonNode approve() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		ArrayNode data = mapper.createArrayNode();
		request.put("key", getKey());
		int command = RequestHandler.APPROVE_BORROW_REQUEST;
		request.put("command", command);
		JsonNode handling = getHandlingRequest();
		int size = Integer.parseInt(handling.get("size").asText());
		if (size == 0) {
			System.out.println("Nothing to approve");
			return null;
		}
		printArrayNode(handling);
		boolean isContinue = true;
		while (isContinue) {
			String rID = Inputter.inputNotBlankStr("Please enter the request id");
			data.add(rID);
			isContinue = confirmYesNo("Do you want to continue[y/n]: ");
		}
		request.set("data", data);
		return request;
	}

	JsonNode showBorrowList() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		request.put("key", getKey());
		int command = RequestHandler.GET_ALL_APPROVED_REQUESTS;
		request.put("command", command);
		return request;
	}

	private JsonNode getHandlingRequest() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		request.put("key", getKey());
		int command = RequestHandler.GET_ALL_WAITING_REQUESTS;
		request.put("command", command);
		return sendRequest(request);
	}


	@Override
	protected void breadth() {
		boolean isContinue = true;
		while (isContinue) {
			JsonNode request = null;
			int choice = getChoice();
			switch (choice) {
				case 1:
					request = searchAssetByName();
					break;
				case 2:
					request = addAsset();
					break;
				case 3:
					request = updateAsset();
					break;
				case 4:
					request = approve();
					break;
				case 5:
					request = showBorrowList();
					break;
				case 6:
					isContinue = false;
					break;
				default:
					break;
			}
			if (request != null) {
				JsonNode reply = sendRequest(request);
				printMessage(reply);
				printArrayNode(reply);

			}
			System.out.println();
		}
	}
}
