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
import utils.Inputter;

/**
 *
 * @author marti
 */
public class EmployeeMenu extends AssetManagementMenu {

    protected EmployeeMenu(String newKey) {
        super(newKey);
    }

    @Override
    protected void loadMainMenu() {
        addItem("Search asset by name");
        addItem("Borrow Asset");
        addItem("Cancel Asset");
        addItem("Return Asset");
        addItem("Quit");
    }

    JsonNode borrowAsset() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode request = mapper.createObjectNode();
        ArrayNode data = mapper.createArrayNode();
        request.put("key", getKey());
        int command = RequestHandler.BORROW_ASSET;
        request.put("command", String.valueOf(command));
        System.out.println("Welcome To Borrow Asset Menu");

        boolean isContinue = true;
        while (isContinue) {
            String assetID = Inputter.inputNotBlankStr("Please enter id: ");
            int quantity = Inputter.inputInteger("Please enter quantity: ");
            ObjectNode item = mapper.createObjectNode();
            item.put("assetID", assetID);
            item.put("quantity", String.valueOf(quantity));
            data.add(item);
            isContinue = confirmYesNo("Do you want to continue[y/n]: ");
        }

        request.set("data", data);
        return request;
    }

    JsonNode cancelRequest() {
        JsonNode handling = getWaitingRequests();
        int size = Integer.parseInt(handling.get("size").asText());
        if (size == 0) {
            System.out.println("Nothing to cancel");
            return null;
        }
        printArrayNode(handling);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode request = mapper.createObjectNode();
        ArrayNode data = mapper.createArrayNode();
        request.put("key", getKey());
        int command = RequestHandler.CANCEL_REQUEST;
        request.put("command", String.valueOf(command));
        System.out.println("Welcome To Cancel Request Menu");

        boolean isContinue = true;
        while (isContinue) {
            String id = Inputter.inputNotBlankStr("Please enter id of the request: ");
            ObjectNode item = mapper.createObjectNode();
            item.put("id", id);
            data.add(item);
            isContinue = confirmYesNo("Do you want to continue[y/n]: ");
        }

        request.set("data", data);
        return request;
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
                    request = borrowAsset();
                    break;
                case 3:
                    request = cancelRequest();
                    break;
                case 4:
                    System.out.println("Not supported yet");
                    break;
                case 5:
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
