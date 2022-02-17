/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.StringUtilities;

/**
 *
 * @author marti
 */
final public class RequestHandler {

	private static RequestHandler handler = null;
	private HashMap<String, I_Controller> controllers;

	public static RequestHandler getInstance() {
		if (handler == null) {
			handler = new RequestHandler();
			handler.controllers = new HashMap<>();
			handler.controllers.put("asset", AssetController.getInstance());
			handler.controllers.put("emp", EmpController.getInstance());
		}
		return handler;
	}

	public JsonNode handle(JsonNode request) {
		JsonNode reply = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			String key = request.get("key").asText();
			String command = request.get("command").asText();
			AssetController controller = (AssetController) controllers.get("asset");
			boolean isAccepted = controller.isAcceptable(key, Integer.valueOf(command));
			if (isAccepted) {
				String jsonString = StringUtilities.toLowerCasse(request.toString());
				JsonNode standard = mapper.readTree(jsonString);
				int commandId = Integer.valueOf(command);
				switch (commandId) {
					case 2:
						String name = standard.get("name").asText();
						reply = AssetController.getInstance().searchByName(name);
						break;
					case 6:
						JsonNode data = standard.get("data");
						if (data.isArray()) {
							reply = AssetController.getInstance().add(data);
						}
						break;
					case 7:
						data = standard.get("data");
						reply = AssetController.getInstance().edit(data);
						break;
					case 8:
						data = standard.get("data");
						reply = AssetController.getInstance().approve(data);
						break;
					case 9:
						reply = AssetController.getInstance().getBorrowList();
						break;
					case 10:
						String id = standard.get("id").asText();
						reply = (JsonNode) handler.controllers.get("asset").get(id);
						break;
					case 11:
						reply = (JsonNode) AssetController.getInstance().handlingRequest();
						break;
					default:
						break;
				}

			} else {
				ObjectNode err = mapper.createObjectNode();
				err.put("status", "not accepted");
				err.put("message", "something wrong");
				reply = err;
			}

		} catch (Exception ex) {
			Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return reply;
	}

	public int searchAssetsByName() {
		return 2;
	}

	public int addAssetCommandID() {
		return 6;
	}

	public int updateAssetCommandID() {
		return 7;
	}

	public int approveBorrowCommandID() {
		return 8;
	}

	public int getBorrowListCommandID() {
		return 9;
	}

	public int getAssetCommandID() {
		return 10;
	}

	public int getHandlingRequestsCommandID() {
		return 11;
	}

	public int getAllAssets() {
		return 2;
	}

	public int getPrivilege(String key) {
		return ((EmpController) controllers.get("emp")).getPrivilege(key);
	}
}
