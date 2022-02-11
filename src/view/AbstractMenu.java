/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.List;
import utils.Utils;

/**
 *
 * @author marti
 * @param <E> the element type in the choice list
 */
public abstract class AbstractMenu<E> implements I_Menu<E> {

	protected List<E> choiceList;

	@Override
	public void addItem(E item) {
		choiceList.add(item);
	}

	@Override
	public void showMenu() {
		// do not delete this comment, your code here:
		int size = choiceList.size();
		for (int i = 0; i < size; i++) {
			System.out.println(i + 1 + ". " +choiceList.get(i));
		}
	}

	@Override
	public boolean confirmYesNo(String welcome) {
		boolean result = Utils.confirmYesNo(welcome);
		return result;
	}
	
	@Override
	public int getChoice() {
		return Utils.getInt("Input your choice:", 1, choiceList.size());
	}
	
	protected E getItem(int choice){
		return choiceList.get(choice);
	}
}
