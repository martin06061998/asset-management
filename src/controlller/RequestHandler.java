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
    public final static int CANCEL_REQUEST = 4;
    public final static int ADD_ASSET = 6;
    public final static int UPDATE_ASSET = 7;
    public final static int APPROVE_BORROW_REQUEST = 8;
    public final static int GET_ALL_APPROVED_REQUESTS = 1;
    public final static int GET_ASSET = 10;
    public final static int GET_ALL_WAITING_REQUESTS = 0;

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
        ObjectMapper mapper = new ObjectMapper();
        try {

            String key = request.get("key").asText();
            String command = request.get("command").asText();
            AssetController controller = (AssetController) controllers.get("asset");
            boolean isAccepted = controller.isAcceptable(key, Integer.valueOf(command));
            if (isAccepted) {
                final String empID = key.substring(0, 7).toLowerCase();
                String jsonString = StringUtilities.toLowerCasse(request.toString());
                JsonNode standard = mapper.readTree(jsonString);
                int commandId = Integer.valueOf(command);
                switch (commandId) {
                    case 0:
                        int privilege = getPrivilege(key);
                        reply = (JsonNode) AssetController.getInstance().getAllWaitingRequests(privilege, empID);
                        break;
                    case 1:
                        privilege = getPrivilege(key);
                        reply = AssetController.getInstance().getAllApprovedRequests(privilege, empID);
                        break;
                    case 2:
                        String name = standard.get("name").asText();
                        reply = AssetController.getInstance().searchByName(name);
                        break;
                    case 3:
                        JsonNode data = standard.get("data");
                        reply = AssetController.getInstance().borrowAsset(empID, data);
                        break;
                    case 4:
                        data = standard.get("data");
                        reply = AssetController.getInstance().cancelRequest(empID, data);
                        break;
                    case 6:
                        data = standard.get("data");
                        reply = AssetController.getInstance().add(data);
                        break;
                    case 7:
                        data = standard.get("data");
                        reply = AssetController.getInstance().edit(data);
                        break;
                    case 8:
                        data = standard.get("data");
                        reply = AssetController.getInstance().approve(data);
                        break;
                    case 10:
                        String id = standard.get("id").asText();
                        reply = (JsonNode) handler.controllers.get("asset").get(id);
                        break;
                    default:
                        break;
                }

            } else {
                ObjectNode err = mapper.createObjectNode();
                err.put("status", "not accepted");
                err.put("message", "wrong username or password");
                err.set("data", mapper.createArrayNode());
                reply = err;
            }

        } catch (Exception ex) {
            ObjectNode err = mapper.createObjectNode();
            err.put("status", "not accepted");
            err.put("message", "bad request");
            err.set("data", mapper.createArrayNode());
            reply = err;
        }
        return reply;
    }

    public int getPrivilege(String key) {
        return ((EmpController) controllers.get("emp")).getPrivilege(key);
    }
}
