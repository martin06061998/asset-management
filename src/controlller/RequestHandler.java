/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			handler.controllers = new HashMap();
			handler.controllers.put("asset", AssetController.getInstance());
			handler.controllers.put("emp", EmpController.getInstance());
		}
		return handler;
	}

	public JsonNode handle(JsonNode request) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode reply = mapper.createObjectNode();
		boolean errorFree = true;
		String message = null;
		try {
			String key = request.get("key").asText();
			String command = request.get("command").asText();
			AssetController controller = (AssetController) controllers.get("asset");
			boolean isAccepted = controller.isAcceptable(key, Integer.valueOf(command));
			if (isAccepted) {
				int commandId = Integer.valueOf(command);
				switch (commandId) {
					case 6:
						JsonNode data = request.get("data");
						if(data.isArray())
							handler.controllers.get("asset").add(request.get("data"));
						else
							errorFree = false;
						break;
					case 7:
						break;
					case 8:
						break;
					case 9:
						break;
					default:
						break;
				}
				if(errorFree)
					message = "successfully";
			}
			else{
				message = "faild due to invalid opearion";
			}

		} catch (Exception ex) {
			message = "faild";
			Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		reply.put("message", message);
		return reply;
	}

	public int addAssetCommandID() {
		return 6;
	}

	public int getPrivilege(String key) {
		return ((EmpController) controllers.get("emp")).getPrivilege(key);
	}
}
