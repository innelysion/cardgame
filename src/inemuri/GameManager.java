package inemuri;

import java.util.ArrayList;
import java.util.Collections;

public class GameManager {

	//private String deck = "1,15;";
	private ArrayList<Cards> library, hand, stack, discarded, exiled;

	private int drawCardTimes = 2;
	private int HP = 50;
	private int ATK = 5;

	GameManager() {
		library = new ArrayList<Cards>();
		hand = new ArrayList<Cards>();
		stack = new ArrayList<Cards>();
		discarded = new ArrayList<Cards>();
		exiled = new ArrayList<Cards>();


		for (int i = 1; i < 18; i++)
		{
			library.add(new Cards(i));
			library.add(new Cards(i));
			library.add(new Cards(i));
		}
		shuffle(library);

		for (Cards c : library){
			System.out.print(c.getName() + ",");
		}
	}

	public void mainUpdate() {

		if (Input.keyRe.Z) {

			for (int i = 0; i < drawCardTimes; i++) {
				if (library.isEmpty()) {
					shuffleInto(discarded, library);
				}
				hand.add(library.remove(0));
			}
			printdebug();
		}
		if (Input.keyRe.X) {
			stack.addAll(hand);
			hand.clear();
			printdebug();
		}
		if (Input.keyRe.C) {
			shuffleInto(discarded, library);
			printdebug();
		}

		stackProcess();
	}

	private void printdebug() {

	}

	private void stackProcess() {
		if (!stack.isEmpty()) {

			discarded.addAll(stack);
			stack.clear();
		}
	}

	private <T> void shuffle(ArrayList<T> list) {
		Collections.shuffle(list);
	}

	private <T> void shuffleInto(ArrayList<T> somelist, ArrayList<T> target) {
		target.addAll(somelist);
		shuffle(target);
		somelist.clear();
	}

	public void firstUpdate() {
	}

	public void lateUpdate() {

	}

}
