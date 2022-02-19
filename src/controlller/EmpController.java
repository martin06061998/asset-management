/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
		HashMap<String, Employee> ret = null;
		ret = new HashMap<>();
		Employee emp1 = new Employee("E160001", "Nguyen Hong Hiep", new GregorianCalendar(2000, Calendar.JUNE, 12).getTime(), "EM", Sex.female, "e10adc3949ba59abbe56e057f20f883e");
		Employee emp2 = new Employee("E160240", "Tran Dinh Khanh", new GregorianCalendar(2000, Calendar.JULY, 15).getTime(), "EM", Sex.male, "e10adc3949ba59abbe56e057f20f883e");
		Employee emp3 = new Employee("E140449", "Le Buu Nhan", new GregorianCalendar(2002, Calendar.JULY, 10).getTime(), "EM", Sex.male, "e10adc3949ba59abbe56e057f20f883e");
		Employee emp4 = new Employee("E160798", "Truong Le Minh", new GregorianCalendar(2002, Calendar.SEPTEMBER, 12).getTime(), "EM", Sex.male, "e10adc3949ba59abbe56e057f20f883e");
		Employee emp5 = new Manager("E000000", "Hoa Doan", new GregorianCalendar(1990, Calendar.JUNE, 5).getTime(), "MA", Sex.male, "e10adc3949ba59abbe56e057f20f883e");

		ret.put(emp1.getEmpID(), emp1);
		ret.put(emp2.getEmpID(), emp2);
		ret.put(emp3.getEmpID(), emp3);
		ret.put(emp4.getEmpID(), emp4);
		ret.put(emp5.getEmpID(), emp5);

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

	boolean isEmpIDValid(String empID) {
		final String regex = "e\\d{6}";
		return !(empID == null || !empID.matches(regex));
	}

	Employee getEmployeeByID(String empID) {
		if (!isEmpIDValid(empID)) {
			return null;
		}
		return empMap.get(empID);
	}

	boolean isEmployeeExist(String empID){
		return getEmployeeByID(empID) != null;
	}
	
	int getPrivilege(String key) {
                final String regex = "[Ee]\\d{6}.{1,35}";
		int ret = 0;
		if (key == null || !key.matches(regex)) {
			ret = 0;
		} else {
			final String userName = key.substring(0, 7).toLowerCase();
                        final String password = key.substring(7);
			final Employee e = empMap.get(userName);
			if (e == null || !e.getPassword().equals(password)) {
				ret = 0;
			} else if(e.getClass() == Manager.class)
                            ret = 3000;
                        else
                            ret = 5;
		}
		return ret;
	}
}
