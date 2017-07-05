package inemuri.CardGameObjects;

import inemuri.CardGameObjects.Enum.*;

public class Card extends BaseObject {
	// 战斗用
	private Type type; // 类型
	private Zone zone; // 当前位置

	public Card(int i, Party p) {
		super();
		party = p;
		name = CardsPool.name[i];
		description = CardsPool.description[i];
		type = CardsPool.type[i];
		zone = Zone.DEFAULT;
	}

	// Gets
	public boolean isIn(Zone z) {
		return z == zone;
	}

	public boolean isType(Type t) {
		return t == type;
	}

	// 以上Gets
	// Sets
	public void putInto(Zone z) {
		zone = z;
	}
	
	// 以上Sets
}

class CardsPool {
	static String[] name = { "DefaultName", "灵", "魔", "妖", "神", "幻", "源", "心", "无", "盾", "集", "二重结界", "御币", "塞钱征收",
			"迷你八卦炉", "光尘", "魔力吸收", "蛇蛙发饰" };
	static String[] description = { "DefaultRuleText", // 默认
			"【灵】", "【魔】", "【妖】", "【神】", "【幻】", "【源】", "【心】", "【无】", "【盾】", "【集】", // 基础卡
			"【盾】增强1", // 二重结界
			"【灵】【盾】", // 御币
			"如成功对敌方队伍造成HP伤害,下回合抽牌阶段额外抽三张卡", // 塞钱征收
			"【魔】【魔】", // 迷你八卦炉
			"【魔】增强1", // 光尘
			"敌方取得先手且敌方本回合使用过【魔】的话,【魔】增强3", // 魔力吸收
			"【盾】【集】 / 从手牌进入弃牌区时，己方全体恢复10点HP" }; // 蛇蛙发饰
	static Type[] type = { Type.DEFAULT, Type.ATTACK, Type.ATTACK, Type.ATTACK, Type.ATTACK, Type.ATTACK, Type.ATTACK,
			Type.ATTACK, Type.ATTACK, Type.DEFENCE, Type.FOCUS, Type.SEAL, Type.EQUIPMENT, Type.EVENT, Type.EQUIPMENT,
			Type.MAGIC, Type.MAGIC, Type.EQUIPMENT };

}