package view;

/**
 *
 * @author Hoa Doan
 */
/* Interface for menu */
 public interface I_Menu<E> {
   // add a menu item--> add text to menu
   void addItem(E s);
   // get user choice( user input their choice)
   int getChoice();
   // show menu for user choice
   void showMenu();
   // confirm yes/ no (Y/N)
   boolean confirmYesNo(String welcome);
   
   
}
