/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;

/**
 *
 * @author marti
 */
final public class EnityFactory {

	private HashMap<Class<? extends Asset>, I_Factory<?>> factoryList;
	private static EnityFactory factory;

	private EnityFactory() {

	}

	public static EnityFactory getInstane() {
		if (factory == null) {
			factory = new EnityFactory();
			factory.factoryList = new HashMap<>();
			factory.factoryList.put(Asset.class, new Asset_Factory());
		}
		return factory;
	}


	public Asset createAsset(JsonNode item)  {
		return (Asset) factory.factoryList.get(Asset.class).createInstance(item);
	}
	
}
