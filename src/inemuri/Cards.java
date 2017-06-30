package inemuri;

import java.util.ArrayList;
import java.util.HashMap;

public class Cards {

	private String name; // 名称s
	private int rarity; // 稀有度
	private String imageName; // 卡牌图片名
	private String ruleText; // 卡牌说明文
	private Family family; // 基础类型
	private ArrayList<Types> types; // 细分类型列表

	private int id; // 序号
	private int cost; // 消耗
	private int value; // 价值

	private boolean isLocked;
	private boolean isReversed;
	private boolean isPhantom;

	// TODO 看看怎么将自身的处理逻辑传给外面
	// private ArrayList<> //判断条件容器
	// private ArrayList<> //行为容器

	Cards() {
		name = "";
		rarity = 0;
		imageName = "";
		ruleText = "";
		family = Family.DEFAULT;
		types = new ArrayList<Types>();
		types.add(Types.DEFAULT);

		id = 0;
		cost = 0;
		value = 0;

		isReversed = false;
	}

	Cards(int index) {
		this();
		id = index;
		loadCardData(id);
	}

	private void loadCardData(int index) {
		CardData data = new CardData();
		name = data.tempdata_name.get(index);
		rarity = data.tempdata_rarity.get(index);
		// imageName = data.tempdata_imageName.get(index).;
		ruleText = data.tempdata_ruleText.get(index);
		family = data.tempdata_family.get(index);
		types = data.tempdata_types.get(index);
		cost = data.tempdata_cost.get(index);
		value = data.tempdata_value.get(index);
	}

	public String getName() {
		return name;
	}

	private static class CardData {
		HashMap<Integer, String> tempdata_name = new HashMap<Integer, String>();
		HashMap<Integer, Integer> tempdata_rarity = new HashMap<Integer, Integer>();
		// HashMap<Integer, String> tempdata_imageName = new HashMap<Integer,
		// String>();
		HashMap<Integer, String> tempdata_ruleText = new HashMap<Integer, String>();
		HashMap<Integer, Family> tempdata_family = new HashMap<Integer, Family>();
		HashMap<Integer, ArrayList<Types>> tempdata_types = new HashMap<Integer, ArrayList<Types>>();
		HashMap<Integer, Integer> tempdata_value = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> tempdata_cost = new HashMap<Integer, Integer>();

		CardData() {
			String element = "灵魔妖神幻源心无";
			int start = 1;
			int end = 9;
			int tid = 0;
			for (int i = start; i < end; i++) {
				tempdata_name.put(i, element.substring(i - 1, i) + "1");
				tempdata_rarity.put(i, 1);
				// tempdata_imageName
				tempdata_ruleText.put(i, element.substring(i - 1, i) + "1");
				tempdata_family.put(i, Family.ACTIONS);
				tempdata_types.put(i, new ArrayList<Types>() {
					{
						add(Types.ATTACK);
					}
				});
				tempdata_value.put(i, 1);
				tempdata_cost.put(i, 1);
			}

			//////////////////////////////////
			tid = 9;
			tempdata_name.put(tid, "盾1");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "盾1");
			tempdata_family.put(tid, Family.ACTIONS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.DEFENCE);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 10;
			tempdata_name.put(tid, "集1");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "集1");
			tempdata_family.put(tid, Family.ACTIONS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.FOCUS);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 11;
			tempdata_name.put(tid, "二重结界");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "盾结算+1");
			tempdata_family.put(tid, Family.EFFECTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.SEAL);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 12;
			tempdata_name.put(tid, "御币");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "灵1盾1");
			tempdata_family.put(tid, Family.EFFECTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.EQUIP);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 13;
			tempdata_name.put(tid, "塞钱征收");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "下回合抽牌阶段额外抽两张牌");
			tempdata_family.put(tid, Family.EVENTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.EVENT);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);


			//////////////////////////////////
			tid = 14;
			tempdata_name.put(tid, "迷你八卦炉");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "魔2");
			tempdata_family.put(tid, Family.EFFECTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.EQUIP);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 15;
			tempdata_name.put(tid, "魔力增幅");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "魔结算+1");
			tempdata_family.put(tid, Family.EFFECTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.MAGIC);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 16;
			tempdata_name.put(tid, "魔力吸收");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "如被敌方先手攻击命中且敌方本回合内使用过【魔】的话，魔结算+3");
			tempdata_family.put(tid, Family.EFFECTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.MAGIC);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);

			//////////////////////////////////
			tid = 17;
			tempdata_name.put(tid, "蛇蛙发饰");
			tempdata_rarity.put(tid, 1);
			// tempdata_imageName
			tempdata_ruleText.put(tid, "盾1集1，从手牌进入弃牌区时，己方全体恢复10点HP");
			tempdata_family.put(tid, Family.EFFECTS);
			tempdata_types.put(tid, new ArrayList<Types>() {
				{
					add(Types.EQUIP);
				}
			});
			tempdata_value.put(tid, 1);
			tempdata_cost.put(tid, 1);


		}
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
