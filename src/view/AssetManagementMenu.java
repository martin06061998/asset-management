/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controlller.RequestHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.EncryptionMD5;
import utils.Inputter;
import utils.StringUtilities;

/**
 *
 * @author marti
 */
public abstract class AssetManagementMenu extends Menu<String> {

    final static protected RequestHandler REQUEST_HANDLER = RequestHandler.getInstance();
    private final String key;
    private static final int BLOCK_SIZE = 14;

    protected AssetManagementMenu(String newKey) {
        choiceList = new ArrayList<>();
        loadMainMenu();
        this.key = newKey;
    }

    final static protected String promtForKey() {
        String key = null;
        try {
            String empID = Inputter.inputNotBlankStr("Employee ID: ");
            String pwd = Inputter.inputNotBlankStr("Password: ");
            key = empID + EncryptionMD5.encryptionMD5(pwd);
        } catch (Exception ex) {
            Logger.getLogger(AssetManagementMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }

    protected String getKey() {
        return key;
    }

    protected abstract void loadMainMenu();

    protected abstract void breadth();

    protected static void printMessage(JsonNode node) {
        System.out.println(node.get("message").asText());
    }

    protected static String getStatus(JsonNode node) {
        return node.get("status").asText();
    }

    protected JsonNode searchAssetByName() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode request = mapper.createObjectNode();
        request.put("key", getKey());
        int command = RequestHandler.SEARCH_ASSET_BY_NAME;
        request.put("command", command);
        String name = Inputter.inputNotBlankStr("Please enter name: ");
        request.put("name", name);
        return request;
    }

    final protected String getUserID() {
        return key.substring(0, 7);
    }

    final protected String getPassword() {
        return key.substring(7);
    }

    /*
	* Send request to server	*
     */
    protected JsonNode sendRequest(JsonNode request) {
        JsonNode reply = REQUEST_HANDLER.handle(request);
        return reply;
    }

    final static protected int getPrivilege(String key) {
        int answer = REQUEST_HANDLER.getPrivilege(key);
        return answer;
    }

    protected static void printArrayNode(JsonNode node) {
        JsonNode dataElement = node.get("data");
        if (dataElement != null && dataElement.isArray()) {
            if (dataElement.size() == 0) {
                System.out.println("Nothing to show");
            } else {
                LinkedHashMap<String, Integer> keyMap = calculateSize(dataElement);
                int length = 0;
                for (int e : keyMap.values()) {
                    length += e;
                }
                String headerLine = StringUtilities.generateRepeatedString("*", length + (keyMap.size() - 1) * 2 + 2);
                String line = StringUtilities.generateRepeatedString("-", length + (keyMap.size() - 1) * 2 + 2);
                System.out.println(headerLine);
                printHeader(keyMap);
                System.out.println(headerLine);
                StringUtilities.generateRepeatedString("-", length);
                for (JsonNode element : dataElement) {
                    printJsonNode(element, keyMap);
                    System.out.println(line);

                }
            }
        }

    }

    protected static void printJsonNode(JsonNode element, LinkedHashMap<String, Integer> keyMap) {
        Iterator<String> fieldNames = element.fieldNames();
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            int numberOfCharacter = keyMap.get(field);
            String value = element.get(field).asText();
            value = StringUtilities.toPretty(value);
            int padding = (numberOfCharacter - value.length()) / 2;
            String leftAlign = StringUtilities.generateRepeatedString(" ", padding);
            String rightAlign = StringUtilities.generateRepeatedString(" ", numberOfCharacter - padding - value.length());
            String outputString = "|" + leftAlign + value + rightAlign + "|";
            System.out.print(outputString);
        }
        System.out.println();
    }

    protected static void printHeader(LinkedHashMap<String, Integer> keyMap) {
        for (String key : keyMap.keySet()) {
            int numberOfCharacter = keyMap.get(key);
            int padding = (numberOfCharacter - key.length()) / 2;
            String leftAlign = StringUtilities.generateRepeatedString(" ", padding);
            String rightAlign = StringUtilities.generateRepeatedString(" ", numberOfCharacter - padding - key.length());
            String outputString = "|" + leftAlign + key.toUpperCase() + rightAlign + "|";
            System.out.print(outputString);
        }
        System.out.println();
    }

    protected static LinkedHashMap<String, Integer> calculateSize(JsonNode dataElement) {
        LinkedHashMap<String, Integer> keyMap = new LinkedHashMap<>();
        for (JsonNode element : dataElement) {
            Iterator<String> fieldNames = element.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                String value = element.get(key).asText();
                if (keyMap.containsKey(key)) {
                    int newLength = (int) Math.ceil(value.length() / BLOCK_SIZE) * BLOCK_SIZE + BLOCK_SIZE;
                    int oldLength = keyMap.get(key);
                    if (newLength > oldLength) {
                        keyMap.put(key, newLength);
                    }
                } else {
                    int newLength = (int) Math.ceil(value.length() / BLOCK_SIZE) * BLOCK_SIZE + BLOCK_SIZE;
                    if (newLength < key.length()) {
                        newLength = (int) Math.ceil(key.length() / BLOCK_SIZE) * BLOCK_SIZE + BLOCK_SIZE;
                    }
                    keyMap.put(key, newLength);
                }
            }
        }
        return keyMap;
    }
}
