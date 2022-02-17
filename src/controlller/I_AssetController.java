/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author nguye
 */
public interface I_AssetController extends I_Controller<JsonNode,JsonNode> {

	public boolean isAcceptable(String key, int command);
}
