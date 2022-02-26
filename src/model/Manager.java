/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author marti
 */
public class Manager extends Employee{

	public Manager(String employeeID, String name, Date birthDate, String role, boolean sex, String password)  {
		super(employeeID, name, birthDate, role, sex, password);
	}
	
}
