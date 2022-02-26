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

    abstract T reforge(JsonNode target);

    final boolean checkPattern(JsonNode data, boolean equalNumOfKeys) {
        boolean isValid = data.has("id");
        if (!isValid) {
            return false;
        }
        int numberOfKeys = 0;
        String regex, value, key;
        Iterator<String> keys = data.fieldNames();
        while (keys.hasNext()) {
            key = keys.next();
            if (!regexMap.containsKey(key)) {
                isValid = false;
                break;
            }
            regex = regexMap.get(key);
            value = data.get(key).asText();
            if (!value.matches(regex)) {
                isValid = false;
                break;
            }
            numberOfKeys++;
        }
        if (isValid && equalNumOfKeys) {
            isValid = numberOfKeys == regexMap.size();
        } else if (isValid && !equalNumOfKeys) {
            isValid = numberOfKeys >= regexMap.size();
        }
        return isValid;
    }
}
