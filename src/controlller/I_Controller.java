/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

/**
 *
 * @author marti
 */
public interface I_Controller<E,R>  {

	R getAll() throws Exception;
	
	//public boolean save();
	
	//public boolean load();
	
	public R add(E item) throws Exception;

	public R delete(E target) throws Exception;
	
	public R get(String key) throws Exception;
}
