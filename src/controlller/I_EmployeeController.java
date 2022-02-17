/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import model.Employee;

/**
 *
 * @author nguye
 */
public interface I_EmployeeController extends I_Controller<JsonNode,JsonNode> {

    public Employee login(String userName, String passWord) throws Exception;


}
