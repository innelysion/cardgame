package com.inemuri.CardGameObjects;

import java.util.ArrayList;
import java.util.HashMap;

import com.inemuri.CardGameObjects.Enum.Party;

public class Girl extends GameBaseObject {
	// 战斗用
	private int MAXHP; // 最大耐久
	private int HP; // 耐久
	private int SHD; // 护盾
	private int ATK; // 攻击力
	private int DEF; // 防御力
	private int position; // 队伍中位置
	private ArrayList<Card> mainDeck; // 主卡组

	// TODO 成长 
	private int level; // 等级
	private int exp; // 当前经验
	private ArrayList<Integer> expRequest; // 升级所需经验表
	private int HPGrowth; // 每级耐久成长率
	private HashMap<Integer, Integer> ATKGrowth; // 特定等级攻击成长值
	private HashMap<Integer, Integer> DEFGrowth; // 特定等级防御成长值

	public Girl(int i, Party p) {
		super();
		id = i;
		party = p;
		name = GirlsPool.name[i];
		MAXHP = GirlsPool.maxhp[i];
		HP = MAXHP;
		SHD = 0;
		ATK = GirlsPool.atk[i];
		DEF = GirlsPool.def[i];
		position = -1;
		mainDeck = new ArrayList<Card>();
		GirlsPool.addDeck(mainDeck, i);
		GirlsPool.addElements(elements, i);
		GirlsPool.addBuffs(buffs, i, party);
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

	public ArrayList<Card> getMainDeck() {
		mainDeck.stream().forEach(c -> c.setBelong(party));
		return mainDeck;
	}

	// 以上Gets
	// Sets
	public void hpDamage(int i) {
		int result = i;
		if (i > 0 && SHD > 0) {
			if (SHD >= result) {
				SHD -= result;
				System.out.println(name + "受到护盾伤害★" + result + "点★");
				result = 0;
				return;
			} else {
				result -= SHD;
				System.out.println(name + "受到护盾伤害★" + SHD + "点★");
				SHD = 0;
			}
		}
		System.out.println(name + "受到耐久伤害★" + result + "点★");
		HP -= result;
	}

	public void shieldDamage(int i) {
		System.out.println(name + "在自身周围张开了" + -i + "点护盾★");
		SHD -= i;
	}

	public void setPosition(int i) {
		position = i;
	}
	// 以上Sets

}

// TODO 角色数据库
class GirlsPool {
	static String[] name = { "DEFAULT_NAME", "博丽灵梦", "雾雨魔理沙", "东风谷早苗", "琪露诺", "莉格露", "露米娅" };
	static int[] maxhp = { 1, 600, 600, 600, 450, 550, 700 };
	static int[] atk = { 1, 6, 7, 5, 4, 5, 4 };
	static int[] def = { 1, 6, 5, 7, 9, 9, 9 };

	static void addDeck(ArrayList<Card> deck, int i) {
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
			// c = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 9,
			// 9, 9, 10, 10 };
			c = new int[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 };
			break;
		case 5: // 虫
			// c = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 9,
			// 9, 9, 10, 10 };
			c = new int[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 };
			break;
		case 6: // 7
			// c = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9,
			// 9, 9, 10, 10 };
			c = new int[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 };
			break;
		}
		for (int id : c) {
			deck.add(new Card(id, Party.DEFAULT));
		}
	}

	static void addElements(ArrayList<Element> list, int i) {
		switch (i) {
		case 1:// 城管
			list.add(new Element("灵", 0));
			list.add(new Element("无", 0));
			break;
		case 2:// 黑白
			list.add(new Element("魔", 0));
			break;
		case 3: // 糟苗
			list.add(new Element("灵", 0));
			list.add(new Element("神", 0));
			break;
		case 4:// 9
			list.add(new Element("源", 0));
			break;
		case 5:// 虫
			list.add(new Element("源", 0));
			break;
		case 6:// 7
			list.add(new Element("妖", 0));
			break;
		}
	}

	public static void addBuffs(ArrayList<Buff> buffs, int i, Party p) {
		switch (i) {
		case 5:
			buffs.add(new Buff(4, p));
			break;

		}
	}
}