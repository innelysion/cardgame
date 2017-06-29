package inemuri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class GameManager {

	private String deck;
	private ArrayList<Character> library, hand, stack, discarded, exiled;

	private int drawCardTimes = 2;
	private int HP = 50;
	private int ATK = 5;

	GameManager() {
		deck = "剑剑剑剑剑剑剑剑剑剑盾盾盾盾盾集集集集集斧斧斧斧斧斧斧斧斧斧盾盾盾盾盾集集集集集枪枪枪枪枪枪枪枪枪枪盾盾盾盾盾集集集集集";
		library = new ArrayList<Character>();
		hand = new ArrayList<Character>();
		stack = new ArrayList<Character>();
		discarded = new ArrayList<Character>();
		exiled = new ArrayList<Character>();

		for (char c : deck.toCharArray()) {
			library.add(c);
		}
		shuffle(library);
	}

	public void mainUpdate() {

		if (Input.keyRe.Z) {

			for (int i = 0; i < drawCardTimes; i++) {
				if (library.isEmpty()) {
					shuffleInto(discarded, library);
				}
				hand.add(library.remove(0));
			}
			Collections.sort(hand);
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
		System.out.println("\r\n\r\n\r\n\r\n\r\n\r\n");
		System.out.println("牌库:" + library.size() + " " + library);
		System.out.println("手牌:" + hand.size() + " " + hand);
		System.out.println("弃牌:" + discarded.size() + " " + discarded);

	}

	private void stackProcess() {
		if (!stack.isEmpty()) {
			int pervHP = HP;

			// stack.forEach(c -> this.HP = c == '剑' ? this.HP - 3 : c == '斧' ||
			// c == '枪' ? this.HP - 1 : this.HP);
			Predicate<Character> f1 = c -> c == '剑';
			Predicate<Character> f2 = c -> c == '斧';
			Predicate<Character> f3 = c -> c == '枪';

			stack.stream().forEach(c -> HP -= f1.test(c) ? 3 : f2.or(f3).test(c) ? 1 : 0);

			System.out.println("打出了:" + stack);
			System.out.println("对手造成" + (pervHP - HP) + "点伤害　剩余血量:" + HP);
			discarded.addAll(stack);
			stack.clear();
		}
	}

	private <T> void shuffle(ArrayList<T> list) {
		System.out.println(list);
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
