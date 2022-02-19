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
import utils.StringUtilities;

/**
 *
 * @author marti
 */
final public class RequestHandler {

    public final static int SEARCH_ASSET_BY_NAME = 2;
    public final static int BORROW_ASSET = 3;
    public final static int ADD_ASSET = 6;
    public final static int UPDATE_ASSET = 7;
    public final static int APPROVE_BORROW_REQUEST = 8;
    public final static int GET_ALL_APPROVED_REQUESTS = 9;
    public final static int GET_ASSET = 10;
    public final static int GET_ALL_WAITING_REQUESTS = 11;

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
                    case 3:
                        JsonNode data = standard.get("data");
                        reply = AssetController.getInstance().borrowAsset(data);
                        break;
                    case 6:
                        data = standard.get("data");
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
                        reply = AssetController.getInstance().getAllApprovedRequests();
                        break;
                    case 10:
                        String id = standard.get("id").asText();
                        reply = (JsonNode) handler.controllers.get("asset").get(id);
                        break;
                    case 11:
                        reply = (JsonNode) AssetController.getInstance().getAllWaitingRequests();
                        break;
                    default:
                        break;
                }

            } else {
                ObjectNode err = mapper.createObjectNode();
                err.put("status", "not accepted");
                err.put("message", "bad request");
                reply = err;
            }

        } catch (Exception ex) {

        }
        return reply;
    }

    public int getPrivilege(String key) {
        return ((EmpController) controllers.get("emp")).getPrivilege(key);
    }
}
