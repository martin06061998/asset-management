/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author marti
 */
class AssetFactory extends I_Factory<Asset> {

    AssetFactory() {
        super();
        regexMap.put("id", "[aA]\\d{3}");
        regexMap.put("name", "(?=^[A-Za-z0-9\\s]{2,25}$)[A-Za-z0-9]+(\\s[A-Za-z0-9]+)*");
        regexMap.put("price", "\\d{1,18}");
        regexMap.put("color", "(?=^[A-Za-z\\s]{3,25}$)[A-Za-z]+(\\s[A-Za-z]+)*");
        regexMap.put("weight", "\\d{1,15}|(\\d{1,15}\\.\\d{1,15})");
        regexMap.put("qty", "\\d{1,8}");
        regexMap.put("currentqty", "\\d{1,8}");
    }

    @Override
    Asset createInstance(JsonNode item) throws IllegalArgumentException {
        boolean isValidFormat = checkPattern(item, true);
        if (isValidFormat) {
            return New_Asset(item);
        } else {
            throw new IllegalArgumentException("Format is invalid");
        }

    }

    @Override
    Asset reforge(JsonNode item) {
        boolean isValidFormat = checkPattern(item, false);
        if (isValidFormat) {
            return New_Asset(item);
        } else {
            throw new IllegalArgumentException("Format is invalid");
        }
    }

    private Asset New_Asset(JsonNode item) throws IllegalArgumentException {
        String id = item.get("id").asText();
        String name = item.get("name").asText();
        String color = item.get("color").asText();
        String price = item.get("price").asText();
        String weight = item.get("weight").asText();
        String quantity = item.get("qty").asText();
        String currentQty = item.get("currentqty").asText();
        return new Asset(id, name, color, Long.parseLong(price), Double.parseDouble(weight), Integer.parseInt(quantity), Integer.parseInt(currentQty));
    }
}
