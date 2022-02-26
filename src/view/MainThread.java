/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;

/**
 *
 * @author marti
 */
public class MainThread extends Menu<String> {

    private MainThread() {
        choiceList = new ArrayList<>();
        this.addItem("Sign in");
        this.addItem("Logout");
    }

    private boolean breadth() {
        AssetManagementMenu menu;
        String key = AssetManagementMenu.promtForKey();
        int pri = AssetManagementMenu.getPrivilege(key);
        if (pri == 0) {
            System.out.println("Incorrect id or password");
            return false;
        } else if (pri < 6) {
            menu = new EmployeeMenu(key);
        } else {
            menu = new ManagerMenu(key);
        }
        System.out.println("Successfully");
        menu.breadth();
        return true;
    }

    private void logout() {
        System.out.println("good bye");
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainThread kernel = new MainThread();
        boolean isContinue = true;
        int count = 0;
        while (isContinue) {
            int choice = kernel.getChoice();
            switch (choice) {
                case 1:
                    boolean success = kernel.breadth();
                    if (!success) {
                        count++;
                    } else {
                        count = 0;
                    }
                    break;
                case 2:
                    kernel.logout();
                    break;
                default:
                    break;
            }
            if (count == 5) {
                System.out.println("To many fail");
                kernel.logout();
            }
        }
    }
}
