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
 * The root class of the entity hierarchy hierarchy
 */
public class Employee {
	public enum Sex {
		male,
		female
	}
	final String empID;
	String empName;
	Date birthDate;
	String role;
	Sex sex;
	String password;

	public Employee(String employeeID, String name, Date birthDate, String role, Sex sex, String password) {
		this.empID = employeeID;
		this.empName = name;
		this.birthDate = birthDate;
		this.role = role;
		this.sex = sex;
		this.password = password;
	}
	public String getEmpID(){
		return empID;
	}
	
	public String getName() {
		return empName;
	}

	public void setName(String name) {
		this.empName = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
