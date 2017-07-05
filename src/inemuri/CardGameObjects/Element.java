package inemuri.CardGameObjects;

import java.util.ArrayList;

// 用于攻击/防御/集中计算的属性类
public class Element {
	private String name; // 名称
	private int valueBase; // 基础值
	private int valueAddMod; // 加减法修正(默认为0则什么都不做)
	private int valueMultiMod; // 乘除法修正(默认为1则什么都不做)

	public Element(String n, int vb, int va, int vm) {
		name = n;
		valueBase = vb;
		valueAddMod = va;
		valueMultiMod = vm;
	}

	// 只带基础值的初始化
	public Element(String n, int vb) {
		this(n, vb, 0, 1);
	}

	// 如果目标集合中已有重名属性，则将现有值加上去，否则初始化一个空的同名属性，拿出来再加上去
	public void addSameTo(ArrayList<Element> list) {
		list.stream().filter(e -> name == e.getName()).findAny().orElseGet(() -> {
			list.add(new Element(name, 0, 0, 1));
			return list.stream().filter(e -> name == e.getName()).findFirst().get();
		}).addValueSet(valueBase, valueAddMod, valueMultiMod);
	}

	// 判断目标集合中有无重名属性
	public boolean anySameIn(ArrayList<Element> list) {
		return list.stream().anyMatch(e -> name == e.getName());
	}

	// 四个静态方法，都是判断是否有重名属性
	public static boolean anySame(ArrayList<Element> list1, ArrayList<Element> list2) {
		return list1.stream().anyMatch(e1 -> {
			return list2.stream().filter(e2 -> e1.getName() == e2.getName()).count() > 0;
		});
	}

	public static boolean anySame(Element e1, Element e2) {
		return e1.getName() == e2.getName();
	}

	public static boolean anySame(ArrayList<Element> list1, Element e1) {
		return list1.stream().anyMatch(e -> e.getName() == e1.getName());
	}

	public static boolean anySame(Element e1, ArrayList<Element> list1) {
		return anySame(list1, e1);
	}

	// Get
	public String getName() {
		return name;
	}

	public int getValueBase() {
		return valueBase;
	}

	public int getValueAddMod() {
		return valueAddMod;
	}

	public int getValueMultiMod() {
		return valueMultiMod;
	}

	public void addValueSet(int vb, int va, int vm) {
		valueBase += vb;
		valueAddMod += va;
		valueMultiMod += (vm - 1);
	}
}
