package inemuri.CardGameObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import inemuri.CardGameObjects.Enum.Party;

public abstract class BaseObject {
	// 固有信息
	protected int id; // 序号
	protected String name; // 名称
	protected String nameIcon; // 图标文件名
	protected String description; // 说明文
	// 战斗用
	protected Party party; // 所属
	protected boolean disabled; // 不可用的情况
	protected ArrayList<Buff> buffs; // 状态
	protected ArrayList<Element> elements; // 主属性
	protected HashMap<String, Object> gameData; // 对局数据
	// 效果较为复杂时使用的过滤器和修正器
	protected int priority; // 处理优先级
	protected Predicate<Card> cardSelector;
	protected Consumer<Card> cardModifier;
	protected Predicate<Girl> girlSelector;
	protected Consumer<Girl> girlModifier;

	BaseObject() {
		id = 0;
		name = "";
		nameIcon = "";
		description = "";
		party = Party.NEUTRAL;
		disabled = false;
		buffs = new ArrayList<Buff>();
		elements = new ArrayList<Element>();
		priority = 0;
		cardSelector = c -> false;
		cardModifier = c -> {
		};
		girlSelector = g -> false;
		girlModifier = g -> {
		};
	}

	// Gets
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public boolean belongs(Party p) {
		return p == party;
	}

	public boolean isDead() {
		return disabled;
	}

	// 以上Gets
	// Sets
	public void setDeadOrNot(boolean b) {
		disabled = b;
	}

	public void addBuff(int buffId) {
	}

	public void removeBuff(int buffId) {

	}
	public void setBelong(Party p){
		party = p;
	}

	public void reciveGameData(HashMap<String, Object> data) {
		gameData = data;
	}
	// 以上Sets
}
