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
class Asset_Factory extends I_Factory<Asset> {

	Asset_Factory() {
		super();
		regexMap.put("id", "[aA]\\d{3}");
		regexMap.put("name", "(?=^[A-Za-z0-9\\s]{2,25}$)[A-Za-z0-9]+(\\s[A-Za-z0-9]+)*");
		regexMap.put("price", "\\d{1,18}");
		regexMap.put("color", "(?=^[A-Za-z\\s]{3,25}$)[A-Za-z]+(\\s[A-Za-z]+)*");
		regexMap.put("weight", "\\d{1,15}|(\\d{1,15}\\.\\d{1,15})");
		regexMap.put("quantity", "\\d{1,8}");
	}

	@Override
	Asset createInstance(JsonNode item) throws IllegalArgumentException {
		boolean isValidFormat = checkPattern(item, true);
		if (isValidFormat) {
			String id = item.get("id").asText();
			String name = item.get("name").asText();
			String color = item.get("color").asText();
			String price = item.get("price").asText();
			String weight = item.get("weight").asText();
			String quantity = item.get("quantity").asText();
			return new Asset(id, name, color, Long.parseLong(price), Double.parseDouble(weight), Integer.parseInt(quantity));
		} else {
			throw new IllegalArgumentException("Format is invalid");
		}
	}
	
	
	


}
