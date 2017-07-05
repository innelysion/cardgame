package inemuri.CardGameObjects;

import java.util.ArrayList;
import inemuri.CardGameObjects.Enum.*;

public class Girl extends BaseObject {
	// 战斗用
	private int MAXHP; // 最大耐久
	private int HP; // 耐久
	private int SHD; // 护盾
	private int ATK; // 攻击力
	private int DEF; // 防御力
	private int position; // 队伍中位置
	private ArrayList<Card> mainDeck; // 主卡组

	public Girl(int i, Party p) {
		super();
		party = p;
		name = GirlsPool.name[i];
		MAXHP = GirlsPool.maxhp[i];
		HP = MAXHP;
		SHD = 0;
		ATK = GirlsPool.atk[i];
		DEF = GirlsPool.def[i];
		position = -1;
		mainDeck = GirlsPool.deck(i);
	}

	// Gets
	public int getHp() {
		return HP;
	}

	public int getMaxHp() {
		return MAXHP;
	}

	public int getShield() {
		return SHD;
	}

	public int getAtk() {
		return ATK;
	}

	public int getDef() {
		return DEF;
	}

	public int getPosition() {
		return position;
	}

	public ArrayList<Card> getMainDeck(Party p) {
		mainDeck.stream().forEach(c -> c.setBelong(p));
		return mainDeck;
	}

	// 以上Gets
	// Sets
	public void hpDamage(int i) {
		HP += i;
	}

	public void shieldDamage(int i) {
		SHD += i;
	}

	public void setPosition(int i) {
		position = i;
	}
	// 以上Sets

}

class GirlsPool {
	static String[] name = { "DEFAULT_NAME", "博丽灵梦", "雾雨魔理沙", "东风谷早苗", "琪露诺", "莉格露", "露米娅" };
	static int[] maxhp = { 1, 100, 100, 100, 70, 70, 100 };
	static int[] atk = { 1, 6, 7, 5, 4, 5, 4 };
	static int[] def = { 1, 6, 5, 7, 3, 2, 4 };

	static ArrayList<Card> deck(int i) {
		ArrayList<Card> deck = new ArrayList<Card>();
		int[] c = null;
		switch (i) {
		case 1: // 城管
			c = new int[] { 1, 1, 1, 1, 1, 8, 8, 8, 6, 6, 9, 9, 9, 10, 10, 10, 10, 11, 12, 13 };
			break;
		case 2: // 黑白
			c = new int[] { 2, 2, 2, 2, 2, 2, 7, 7, 7, 8, 8, 8, 10, 10, 10, 10, 14, 15, 15, 16 };
			break;
		case 3: // 糟苗
			c = new int[] { 1, 1, 1, 4, 4, 4, 5, 5, 5, 9, 9, 9, 9, 9, 9, 10, 10, 12, 17 };
			break;
		case 4: // 9
			c = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 9, 9, 9, 10, 10 };
			break;
		case 5: // 虫
			c = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 9, 9, 9, 10, 10 };
			break;
		case 6: // 7
			c = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 10, 10 };
			break;
		}
		for (int id : c) {
			deck.add(new Card(id, Party.DEFAULT));
		}
		return deck;
	}
}