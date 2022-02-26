/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import utils.StringUtilities;

/**
 *
 * @author marti The root class of the entity hierarchy hierarchy
 */
public class Employee implements Serializable {
	final String empID;
	String name;
	Date birthDate;
	String role;
	boolean sex; // true if male
	String password;

	public Employee(String employeeID, String name, Date birthDate, String role, boolean sex, String password)  {
		if (employeeID == null || name == null || birthDate == null || role == null || password == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.empID = StringUtilities.toLowerCasse(employeeID);;
		this.name = StringUtilities.toLowerCasse(name);
		this.role = StringUtilities.toLowerCasse(role);
		this.birthDate = birthDate;
		this.sex = sex;
		this.password = password;
	}

	public String getEmpID() {
		return empID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.name = StringUtilities.toLowerCasse(name);

	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		if (birthDate == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.birthDate = birthDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if (role == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.role = StringUtilities.toLowerCasse(role);
	}

	public boolean getSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
		this.password = password;
	}

}
