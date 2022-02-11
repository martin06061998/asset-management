/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import model.Employee;

/**
 *
 * @author nguye
 * @param <E>
 */
public interface I_EmployeeController<E> extends I_Controller<Employee> {

    public Employee login(String userName, String passWord) throws Exception;


}
