/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import model.Asset;

/**
 *
 * @author nguye
 */
public interface I_AssetController<E> extends I_Controller<Asset> {

	public boolean isAcceptable(String key, int command);
}
