/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import utils.EncryptionMD5;
import utils.StringUtilities;

/**
 *
 * @author marti The root class of the entity hierarchy hierarchy
 */
public class Employee {

	public enum Sex {
		male,
		female
	}
	final String empID;
	String name;
	Date birthDate;
	String role;
	Sex sex;
	String password;

	public Employee(String employeeID, String name, Date birthDate, String role, Sex sex, String password)  {
		if (employeeID == null || name == null || birthDate == null || role == null || sex == null || password == null) {
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

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		if (sex == null) {
			throw new IllegalArgumentException("arguments should not be null");
		}
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
