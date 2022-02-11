/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author marti
 */
public class MainThread {
	private static String key;
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		key = AssetManagementMenu.promtForKey();
		int pri = AssetManagementMenu.getPrivilege(key);
		AssetManagementMenu menu = null;
		if(pri <6){
			menu = new EmployeeMenu(key);
			menu.breadth();
		}
		else{
			menu = new ManagerMenu(key);
			menu.breadth();
		}
			
	}
	
}
