package inemuri;

import java.util.ArrayList;

public class Cards {

	private String name; // 名称s
	private String imageName; // 卡牌图片名
	private String ruleText; // 卡牌说明文
	private Family family; // 基础类型
	private ArrayList<Types> types; // 细分类型列表

	private int id; // 序号
	private int cost; // 消耗
	private int value; // 价值
	private int rarity; // 稀有度

	// TODO 看看怎么将自身的处理逻辑传给外面
	// private ArrayList<> //判断条件容器
	// private ArrayList<> //行为容器

	Cards() {
		name = "";
		imageName = "";
		ruleText = "";
		family = Family.DEFAULT;
		types = new ArrayList<Types>();
		types.add(Types.DEFAULT);

		id = 0;
		cost = 0;
		value = 0;
		rarity = 0;
	}
}

// 基础类型
enum Family {
	// 默认, 行动, 效果, 事件, 非玩家可用, 其他
	DEFAULT, ACTIONS, EFFECTS, EVENTS, SCENARIOS, OTHERS
}

// 细分类型
enum Types {
	// 默认
	DEFAULT,
	// (行动)攻击, 防御, 集中
	ATTACK, DEFENCE, FOCUS,
	// (效果)装备, 结界, 法阵, 负面效果
	EQUIP, SEAL, MAGIC, NEGATIVE,
	// (事件)事件, 命运
	EVENT, FATE,
	// (非玩家可用)触发器
	TRIGGER,
	// (其他)
	OTHER,
}
