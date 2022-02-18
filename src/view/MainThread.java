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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int counter = 0;
        while (true) {
            if (counter == 5) {
                System.exit(0);
            }
            AssetManagementMenu menu;
            String key = AssetManagementMenu.promtForKey();
            int pri = AssetManagementMenu.getPrivilege(key);
            if (pri == 0) {
                System.out.println("Wrong username or password");
                counter++;
            } else if (pri < 6) {
                menu = new EmployeeMenu(key);
                menu.breadth();
            } else {
                menu = new ManagerMenu(key);
                menu.breadth();
            }
            boolean isContinue = utils.Inputter.confirmYesNo("Do you want to continue[y/n]");
            if (!isContinue) {
                break;
            }
        }
        System.out.println("good bye");
    }
}
