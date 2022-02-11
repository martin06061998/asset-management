/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 *
 * @author marti
 */
public interface I_Controller<E>  {

	List<E> getAll() throws Exception;
	
	public boolean save();
	
	public boolean load();
	
	public boolean add(JsonNode item) throws Exception;

	public boolean delete(JsonNode target) throws Exception;

	public E get(int pos) throws Exception;
	
}
