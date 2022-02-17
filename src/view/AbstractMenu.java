/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.List;
import utils.Inputter;
import utils.StringUtilities;

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
		int size = choiceList.size();
		for (int i = 0; i < size; i++) {
			System.out.println(i + 1 + ". " + choiceList.get(i));
		}
	}

	@Override
	public boolean confirmYesNo(String welcome) {
		boolean result = Inputter.confirmYesNo(welcome);
		return result;
	}


	
	@Override
	public int getChoice() {
		System.out.println(StringUtilities.generateRepeatedString("*", 65));
		for (int i = 0; i < choiceList.size(); i++) {
			System.out.printf("%-3d.  %s\n", i + 1, choiceList.get(i));
		}
		System.out.println(StringUtilities.generateRepeatedString("*", 65));
		int response = 0;
		do {
			response = Inputter.inputInteger("**(Note: Your options from 1 - " + choiceList.size() + ")");
		} while (response < 0 || response > choiceList.size());
		return response;
	}

	protected E getItem(int choice) {
		return choiceList.get(choice);
	}
}
