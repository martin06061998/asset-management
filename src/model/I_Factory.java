/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author marti
	 */
abstract class I_Factory<T> {

	HashMap<String, String> regexMap;

	I_Factory() {
		regexMap = new HashMap<>();
	}

	abstract T createInstance(JsonNode target);
	
	final boolean checkPattern(JsonNode data,boolean checkNumberOfKeys) {
		boolean isValidFormat = true;
		int numberOfKeys = 0;
		String regex, value, key;
		Iterator<String> keys = data.fieldNames();
		while (keys.hasNext()) {
			key = keys.next();
			if (!regexMap.containsKey(key)) {
				isValidFormat = false;
				break;
			}
			regex = regexMap.get(key);
			value = data.get(key).asText();
			if (!value.matches(regex)) {
				isValidFormat = false;
				break;
			}
			numberOfKeys++;
		}
		if (checkNumberOfKeys && (numberOfKeys < regexMap.size())) {
			isValidFormat = false;
		}
		return isValidFormat;
	}
}
