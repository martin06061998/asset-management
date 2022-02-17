/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.HashMap;
import model.Employee;
import model.Employee.Sex;
import model.Manager;

/**
 *
 * @author marti
 */
final class EmpController implements I_EmployeeController {

	private static EmpController controller = null;
	private HashMap<String, Employee> empMap = null;

	private EmpController() {
	}

	static EmpController getInstance() {
		if (controller == null) {
			controller = new EmpController();
			controller.empMap = new HashMap<>();
			controller.empMap.putAll(seedData());

		}
		return controller;
	}

	private static HashMap<String, Employee> seedData() {
		HashMap<String, Employee> ret = new HashMap<>();
		Employee emp1 = new Employee("E160001", "Nguyen Hong Hiep", new Date(2000 - 1900, 06, 12), "EM", Sex.female, "12345678");
		Employee emp2 = new Employee("E160240", "Tran Dinh Khanh", new Date(2002 - 1900, 07, 15), "EM", Sex.male, "12345678");
		Employee emp3 = new Employee("E140449", "Le Buu Nhan", new Date(2002 - 1900, 07, 10), "EM", Sex.male, "12345678");
		Employee emp4 = new Employee("E160798", "Truong Le Minh", new Date(2002 - 1900, 12, 03), "EM", Sex.male, "12345678");
		Employee emp5 = new Manager("E000000", "Hoa Doan", new Date(1990 - 1900, 06, 05), "MA", Sex.male, "12345678");

		ret.put(emp1.getEmpID() + emp1.getPassword(), emp1);
		ret.put(emp2.getEmpID() + emp2.getPassword(), emp2);
		ret.put(emp3.getEmpID() + emp3.getPassword(), emp3);
		ret.put(emp4.getEmpID() + emp4.getPassword(), emp4);
		ret.put(emp5.getEmpID() + emp5.getPassword(), emp5);
		return ret;
	}

	@Override
	public Employee login(String userName, String passWord) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public JsonNode getAll() throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public JsonNode add(JsonNode item) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public JsonNode delete(JsonNode target) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public JsonNode get(String key) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	int getPrivilege(String key) {
		Integer ret = null;
		if (key == null || key.length() < 15) {
			ret = 0;
		} else {
			String standardKey = key.substring(0, 8).toLowerCase() + key.substring(8, key.length());
			Employee e = empMap.get(standardKey);
			if (e == null) {
				ret = 0;
			} else if (e.getClass() == Employee.class) {
				ret = 5;
			} else {
				ret = 3000;
			}
		}
		return ret;
	}
}
