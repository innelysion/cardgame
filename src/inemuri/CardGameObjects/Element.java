package inemuri.CardGameObjects;

import java.util.ArrayList;

public class Element {
	private String name;
	private int valueBase;
	private int valueAddMod;
	private int valueMultiMod;

	public Element(String n, int vb, int va, int vm) {
		name = n;
		valueBase = vb;
		valueAddMod = va;
		valueMultiMod = vm;
	}

	public Element(String n, int vb) {
		this(n, vb, 0, 1);
	}

	public void addSameTo(ArrayList<Element> list) {
		list.stream().filter(e -> name == e.getName()).findAny().orElseGet(() -> {
			list.add(new Element(name, 0, 0, 1));
			return list.stream().filter(e -> name == e.getName()).findFirst().get();
		}).addValueSet(valueBase, valueAddMod, valueMultiMod);
	}

	public boolean anySameIn(ArrayList<Element> list) {
		return list.stream().anyMatch(e -> name == e.getName());
	}

	public static boolean containSameElement(ArrayList<Element> list1, ArrayList<Element> list2) {
		return list1.stream().anyMatch(e1 -> {
			return list2.stream().filter(e2 -> e1.getName() == e2.getName()).count() > 0;
		});
	}

	public static boolean containSameElement(Element e1, Element e2) {
		return e1.getName() == e2.getName();
	}

	public static boolean containSameElement(ArrayList<Element> list1, Element e1) {
		return list1.stream().anyMatch(e -> e.getName() == e1.getName());
	}

	public static boolean containSameElement(Element e1, ArrayList<Element> list1) {
		return containSameElement(list1, e1);
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
