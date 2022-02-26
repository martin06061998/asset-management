/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;

import java.io.Serializable;
import java.util.HashMap;
import model.Employee;

/**
 *
 * @author Administrator
 */
public class EmployeeDBO {

    private final static String EMPLOYEE_FILE = "database\\employee.dat";

    private EmployeeDBO() {
    }

    public static boolean save(Serializable empMap) {
        boolean ret = true;
        try {
            FileHandlerManager.getInstance().writeBinary(empMap, EMPLOYEE_FILE);
        } catch (Exception ex) {
            ret = false;
        }
        return ret;

    }

    public static  HashMap<String, Employee> load() {
        Serializable ret = null;
        try {
            ret = FileHandlerManager.getInstance().readBinary(EMPLOYEE_FILE);
        } catch (Exception ex) {
            //Logger.getLogger(AssetDBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (HashMap<String, Employee>) ret;

    }
}
